package com.comsuon.trumtruyentranh.repo.network

import com.comsuon.trumtruyentranh.repo.model.Chapter
import org.jsoup.nodes.Element
import org.jsoup.select.Elements

class HamTruyenTranhScraper(scraperConfigs: ScraperConfigs) : Scraper(scraperConfigs) {

    override fun getMangaSummary(element: Element): String = element.nextElementSibling().text()

    override fun getMangaThumbnail(element: Element): String {
        return "${scraperConfigs.siteUrl}${super.getMangaThumbnail(element)}"
    }

    override fun getMangaChapter(element: Elements): List<Chapter> {
        return element.map { aTag ->
            val chapterUrl = "${scraperConfigs.siteUrl}${aTag.attr("href")}"
            val chapterTitle = aTag.text()
            Chapter(title = chapterTitle, url = chapterUrl)
        }
    }
}