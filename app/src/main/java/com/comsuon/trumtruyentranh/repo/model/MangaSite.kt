package com.comsuon.trumtruyentranh.repo.model

enum class MangaSite(val host: String, val json: String) {
    TRUYEN_TRANH_TUAN(host = "truyentranhtuan.com", json = "truyen_tranh_tuan.json"),
    HAM_TRUYEN_TRANH(host = "hamtruyentranh.net", json = "ham_truyen_tranh.json")
}