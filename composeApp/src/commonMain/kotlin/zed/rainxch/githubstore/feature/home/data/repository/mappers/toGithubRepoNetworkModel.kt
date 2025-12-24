package zed.rainxch.githubstore.feature.home.data.repository.mappers

import zed.rainxch.githubstore.core.data.model.GithubOwnerNetworkModel
import zed.rainxch.githubstore.core.data.model.GithubRepoNetworkModel
import zed.rainxch.githubstore.feature.home.data.repository.dto.GitLabProjectNetworkModel

fun GitLabProjectNetworkModel.toGithubRepoNetworkModel(): GithubRepoNetworkModel {
    val ownerLogin = pathWithNamespace.substringBeforeLast("/")
    val ownerAvatarUrl = avatarUrl ?: ""

    return GithubRepoNetworkModel(
        id = id.toLong(),
        name = name,
        fullName = pathWithNamespace,
        owner = GithubOwnerNetworkModel(
            id = (namespace?.id ?: 0).toLong(),
            login = ownerLogin,
            avatarUrl = ownerAvatarUrl,
            htmlUrl = "https://gitlab.com/$ownerLogin"
        ),
        description = description,
        htmlUrl = webUrl,
        stargazersCount = starCount,
        forksCount = forksCount,
        topics = topics.takeIf { it.isNotEmpty() },
        language = null,
        defaultBranch = "main",
        releasesUrl = "$webUrl/-/releases",
        updatedAt = lastActivityAt,
    )
}