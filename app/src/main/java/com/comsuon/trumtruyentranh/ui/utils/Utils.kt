package com.comsuon.trumtruyentranh.ui.utils

import android.content.Context
import com.comsuon.trumtruyentranh.R
import com.comsuon.trumtruyentranh.repo.model.MangaSite
import com.comsuon.trumtruyentranh.repo.network.ScraperConfigs
import com.google.gson.Gson
import java.io.IOException

fun Context.getErrorMessage(errorObject: Any): String = when (errorObject) {
    is String -> errorObject
    is Int -> this.getString(errorObject)
    else -> this.getString(R.string.general_error)
}

fun Context.getScraperConfigsFromUrl(url: String?): ScraperConfigs? {
    if (url.isNullOrBlank()) return null
    val scraperJson = this.loadJSONFromAsset(url.getScraperConfigPathFromUrl())
    return scraperJson?.let {
        Gson().fromJson(it, ScraperConfigs::class.java)
    }
}

fun Context.loadJSONFromAsset(fileName: String?): String? {
    return try {
        fileName?.let {
            this.assets.open(it).bufferedReader().use {
                it.readText()
            }
        }
    } catch (ex: IOException) {
        ex.printStackTrace()
        null
    }
}

fun String.getScraperConfigPathFromUrl(): String? =
    enumValues<MangaSite>().find { site -> this.contains(site.host) }?.json
