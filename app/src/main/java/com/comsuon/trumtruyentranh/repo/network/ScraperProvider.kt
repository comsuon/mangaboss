package com.comsuon.trumtruyentranh.repo.network

import com.comsuon.trumtruyentranh.repo.model.MangaSite

class ScraperProvider {
    companion object {
        fun provideScraper(url: String, scraperConfigs: ScraperConfigs): Scraper {
            return when {
                url.contains(MangaSite.HAM_TRUYEN_TRANH.host) -> HamTruyenTranhScraper(
                    scraperConfigs
                )
                else -> Scraper(scraperConfigs)
            }
        }
    }
}