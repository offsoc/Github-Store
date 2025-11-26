package zed.rainxch.githubstore.feature.search.domain.model

enum class SortBy {
    MostStars,
    MostForks,
    BestMatch;

    fun displayText(): String = when (this) {
        MostStars -> "Most Stars"
        MostForks -> "Most Forks"
        BestMatch -> "Best Match"
    }

    fun toGithubParams(): Pair<String?, String> = when (this) {
        MostStars -> "stars" to "desc"
        MostForks -> "forks" to "desc"
        BestMatch -> null to "desc"
    }
}