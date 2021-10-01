package com.comsuon.trumtruyentranh.repo.network

import com.comsuon.trumtruyentranh.repo.model.Manga
import com.comsuon.trumtruyentranh.repo.model.MangaDetails

class MangaService(private val scraperConfigs: ScraperConfigs) : IMangaService {

    private val mScraper: Scraper by lazy {
        ScraperProvider.provideScraper(
            scraperConfigs = scraperConfigs,
            url = scraperConfigs.siteUrl
        )
    }

    override suspend fun getMangaByKeyword(keyword: String): List<Manga> {
        TODO("Not yet implemented")
    }

    override suspend fun getMangaDetailsByUrl(url: String): MangaDetails {
        return mScraper.getDetails(url)
    }

    override suspend fun getImagePathsByChapter(chapterUrl: String): List<String> {
        return when (mScraper) {
            is HamTruyenTranhScraper -> mScraper.getImagesFromChapter(chapterUrl)
            is NetTruyenProScraper -> mScraper.getImagesFromChapter(chapterUrl).map { "http:$it" }
            else -> emptyList()
        }
    }

}
