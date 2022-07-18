package dev.davlatov.wallpapers.models.SearchModels

data class SearchHome(
    val total: Long,
    val totalPages: Long,
    val results: List<SearchResult>? = null
)
