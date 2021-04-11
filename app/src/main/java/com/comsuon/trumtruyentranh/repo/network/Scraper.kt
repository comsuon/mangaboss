package com.comsuon.trumtruyentranh.repo.network

import com.comsuon.trumtruyentranh.repo.model.Chapter
import com.comsuon.trumtruyentranh.repo.model.MangaDetails
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.jsoup.Jsoup
import org.jsoup.nodes.Element
import org.jsoup.select.Elements

open class Scraper(val scraperConfigs: ScraperConfigs) {

    suspend fun getDetails(mangaUrl: String): MangaDetails =
        withContext(Dispatchers.IO) {
            val doc = Jsoup.connect(mangaUrl).get()

            println(doc.toString())
            val mangaTitle = getMagaTitle(doc.select(scraperConfigs.titleTag).first())
            val mangaSummary = getMangaSummary(doc.select(scraperConfigs.summaryTag).first())
            val thumbnail = getMangaThumbnail(doc.select(scraperConfigs.thumbnailTag).first())
            val chapters = getMangaChapter(doc.select(scraperConfigs.chapterListTag))

            MangaDetails(
                title = mangaTitle,
                summary = mangaSummary,
                thumbnailUrl = thumbnail,
                url = mangaUrl,
                chapters = chapters
            )

        }

    open fun getMagaTitle(element: Element) = element.text()

    open fun getMangaSummary(element: Element) = element.text()

    open fun getMangaThumbnail(element: Element) = element.attr("src")

    open fun getMangaChapter(element: Elements): List<Chapter> {
        return element.map { aTag ->
            val chapterUrl = aTag.attr("href")
            val chapterTitle = aTag.text()
            Chapter(title = chapterTitle, url = chapterUrl)
        }
    }

    suspend fun getImagesFromChapter(chapterUrl: String): List<String> =
        withContext(Dispatchers.IO) {
            val doc = Jsoup.connect(chapterUrl).get()

            doc.select(scraperConfigs.imagesTag).map { imgTag ->
                imgTag.attr("src")
            }
        }

}