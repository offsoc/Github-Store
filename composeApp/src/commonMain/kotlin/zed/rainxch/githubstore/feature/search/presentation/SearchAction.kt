package zed.rainxch.githubstore.feature.search.presentation

import zed.rainxch.githubstore.core.domain.model.GithubRepoSummary
import zed.rainxch.githubstore.feature.search.domain.model.PlatformType
import zed.rainxch.githubstore.feature.search.domain.model.SortBy

sealed interface SearchAction {
    data class OnPlatformTypeSelected(val platformType: PlatformType) : SearchAction
    data class OnRepositoryClick(val repository: GithubRepoSummary) : SearchAction
    data class OnSearchChange(val query: String) : SearchAction
    data object OnSearchImeClick : SearchAction
    data object OnNavigateBackClick : SearchAction
    data class OnSortBySelected(val sortBy: SortBy) : SearchAction
    data object LoadMore : SearchAction
    data object Retry : SearchAction
}
