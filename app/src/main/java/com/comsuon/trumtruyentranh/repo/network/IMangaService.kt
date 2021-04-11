package com.comsuon.trumtruyentranh.repo.network

import com.comsuon.trumtruyentranh.repo.model.Chapter
import com.comsuon.trumtruyentranh.repo.model.Manga
import com.comsuon.trumtruyentranh.repo.model.MangaDetails

interface IMangaService {
    suspend fun getMangaByKeyword(keyword: String): List<Manga>

    suspend fun getMangaDetailsByUrl(url: String): MangaDetails

    suspend fun getImagePathsByChapter(chapterUrl: String): List<String>
}