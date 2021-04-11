package com.comsuon.trumtruyentranh.repo.network

data class ScraperConfigs(
    val siteUrl: String,
    val titleTag: String,
    val urlTag: String,
    val summaryTag: String,
    val thumbnailTag: String,
    val chapterListTag: String,
    val chapterTitleTag: String?,
    val chapterUrlTag: String?,
    val imagesTag: String
)