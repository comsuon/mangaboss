package com.comsuon.trumtruyentranh.repo.network

import com.comsuon.trumtruyentranh.repo.model.Chapter
import org.jsoup.nodes.Element
import org.jsoup.select.Elements

class NetTruyenProScraper (scraperConfigs: ScraperConfigs): Scraper(scraperConfigs) {
    override fun getMagaTitle(element: Element): String {
        return super.getMagaTitle(element)
    }

    override fun getMangaSummary(element: Element): String {
        return super.getMangaSummary(element)
    }

    override fun getMangaThumbnail(element: Element): String {
        return super.getMangaThumbnail(element)
    }

    override fun getMangaChapter(element: Elements): List<Chapter> {
        return super.getMangaChapter(element)
    }
}