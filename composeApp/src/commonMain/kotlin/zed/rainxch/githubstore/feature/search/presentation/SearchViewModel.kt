package zed.rainxch.githubstore.feature.search.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import co.touchlab.kermit.Logger
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import zed.rainxch.githubstore.feature.search.domain.repository.SearchRepository

class SearchViewModel(
    private val searchRepository: SearchRepository
) : ViewModel() {

    private var hasLoadedInitialData = false
    private var currentSearchJob: Job? = null
    private var currentPage = 1
    private var searchDebounceJob: Job? = null

    private val _state = MutableStateFlow(SearchState())
    val state = _state.asStateFlow()

    private fun performSearch(isInitial: Boolean = false) {
        currentSearchJob?.cancel()

        if (isInitial) {
            currentPage = 1
        }

        currentSearchJob = viewModelScope.launch {
            _state.update {
                it.copy(
                    isLoading = isInitial,
                    isLoadingMore = !isInitial,
                    errorMessage = null,
                    repositories = if (isInitial) emptyList() else it.repositories
                )
            }

            try {
                searchRepository.searchRepositories(
                    query = _state.value.search,
                    sortBy = _state.value.selectedSortBy,
                    platformType = _state.value.selectedPlatformType,
                    page = currentPage
                ).catch { e ->
                    if (e !is CancellationException) {
                        throw e
                    }
                }.collect { paginatedRepos ->
                    if (isActive) {
                        _state.update { currentState ->
                            // Merge results in an idempotent way to avoid duplicates
                            val merged = if (isInitial) {
                                paginatedRepos.repos
                            } else {
                                currentState.repositories + paginatedRepos.repos
                            }
                            val updatedRepos = merged.distinctBy { it.id }

                            currentState.copy(
                                repositories = updatedRepos,
                                isLoading = false,
                                isLoadingMore = false,
                                hasMorePages = paginatedRepos.hasMore,
                                totalCount = paginatedRepos.totalCount,
                                errorMessage = if (updatedRepos.isEmpty() && !paginatedRepos.hasMore) {
                                    "No repositories found"
                                } else null
                            )
                        }
                    }
                }
            } catch (e: CancellationException) {
                Logger.d { "Search cancelled (expected): ${e.message}" }
            } catch (e: Exception) {
                Logger.e { "Search failed: ${e.message}" }
                _state.update {
                    it.copy(
                        isLoading = false,
                        isLoadingMore = false,
                        errorMessage = e.message ?: "Search failed"
                    )
                }
            }
        }
    }

    fun onAction(action: SearchAction) {
        when (action) {
            is SearchAction.OnPlatformTypeSelected -> {
                if (_state.value.selectedPlatformType != action.platformType) {
                    _state.update {
                        it.copy(selectedPlatformType = action.platformType)
                    }
                    currentPage = 1
                    searchDebounceJob?.cancel()
                    performSearch(isInitial = true)
                }
            }

            is SearchAction.OnRepositoryClick -> { }
            SearchAction.OnNavigateBackClick -> { }

            is SearchAction.OnSearchChange -> {
                _state.update { it.copy(search = action.query) }

                searchDebounceJob?.cancel()

                if (action.query.isBlank()) {
                    searchDebounceJob = viewModelScope.launch {
                        delay(300)
                        currentPage = 1
                        performSearch(isInitial = true)
                    }
                } else {
                    searchDebounceJob = viewModelScope.launch {
                        try {
                            delay(500)
                            currentPage = 1
                            performSearch(isInitial = true)
                        } catch (e: CancellationException) {
                            Logger.d { "Debounce cancelled (expected)" }
                        }
                    }
                }
            }

            SearchAction.OnSearchImeClick -> {
                searchDebounceJob?.cancel()
                currentPage = 1
                performSearch(isInitial = true)
            }

            is SearchAction.OnSortBySelected -> {
                if (_state.value.selectedSortBy != action.sortBy) {
                    _state.update {
                        it.copy(selectedSortBy = action.sortBy)
                    }
                    currentPage = 1
                    searchDebounceJob?.cancel()
                    performSearch(isInitial = true)
                }
            }

            SearchAction.LoadMore -> {
                if (!_state.value.isLoadingMore && _state.value.hasMorePages) {
                    currentPage++
                    performSearch(isInitial = false)
                }
            }

            SearchAction.Retry -> {
                currentPage = 1
                searchDebounceJob?.cancel()
                performSearch(isInitial = true)
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        currentSearchJob?.cancel()
        searchDebounceJob?.cancel()
    }
}