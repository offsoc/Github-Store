package zed.rainxch.githubstore.feature.search.domain.repository

import kotlinx.coroutines.flow.Flow
import zed.rainxch.githubstore.feature.home.domain.repository.PaginatedRepos
import zed.rainxch.githubstore.feature.search.domain.model.PlatformType
import zed.rainxch.githubstore.feature.search.domain.model.SortBy

interface SearchRepository {
    fun searchRepositories(
        query: String,
        sortBy: SortBy,
        platformType: PlatformType,
        page: Int
    ): Flow<PaginatedRepos>
}