package com.comsuon.trumtruyentranh.repo.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

data class Manga (
    val title: String,
    val url: String,
    val thumbnailUrl: String
)

data class MangaDetails (
    val title: String,
    val url: String,
    val thumbnailUrl: String,
    val summary: String,
    val chapters: List<Chapter>
)

@Parcelize
data class Chapter (
    val title: String,
    val url: String
): Parcelable
