package com.comsuon.trumtruyentranh.ui.viewmodels

import androidx.lifecycle.*
import com.comsuon.trumtruyentranh.R
import com.comsuon.trumtruyentranh.repo.model.Chapter
import com.comsuon.trumtruyentranh.repo.model.MangaDetails
import com.comsuon.trumtruyentranh.repo.model.MangaSite
import com.comsuon.trumtruyentranh.repo.network.MangaService
import com.comsuon.trumtruyentranh.repo.network.Scraper
import com.comsuon.trumtruyentranh.repo.network.ScraperConfigs
import com.comsuon.trumtruyentranh.ui.viewmodels.NetworkState.*
import kotlinx.coroutines.launch

typealias MangaDetailsResult = Success<MangaDetails>
typealias ImageListResult = Success<List<String>>

class MangaViewModel : ViewModel() {

    private val _networkState = MutableLiveData<NetworkState>()
    val networkState: LiveData<NetworkState> = _networkState

    private val _mangaDetails = MutableLiveData<MangaDetailsResult>()
    val mangaDetails: LiveData<MangaDetailsResult> = _mangaDetails

    private val _currentChapter = MutableLiveData<Chapter>()
    val currentChapter = _currentChapter

    private val _nextChapter = MediatorLiveData<Chapter?>()
    val nextChapter: LiveData<Chapter?> = _nextChapter

    private val _imageList = MutableLiveData<List<String>>()
    val imageList: LiveData<List<String>> = _imageList

    init {
        _nextChapter.addSource(_currentChapter) { newChapter: Chapter ->
            val mangaDetails: MangaDetails? = _mangaDetails.value?.result
            mangaDetails?.let {
                val reverseChapterList = it.chapters.reversed()
                val index = reverseChapterList.indexOf(_currentChapter.value)
                if (index >= mangaDetails.chapters.size - 1) {
                    _nextChapter.postValue(null)
                } else {
                    _nextChapter.postValue(reverseChapterList[index + 1])
                }
            }
        }
    }

    fun getMangaDetails(mangaUrl: String?, scraperConfigs: ScraperConfigs?) {
        if (mangaUrl.isNullOrBlank() || scraperConfigs == null) {
            _networkState.postValue(Failure(errorMessage = R.string.manga_details_incorrect_input))
            return
        }
        _networkState.postValue(Loading)
        viewModelScope.launch {
            val mangaService = MangaService(scraperConfigs)
            val details = mangaService.getMangaDetailsByUrl(mangaUrl)
            _mangaDetails.postValue(Success(details))
        }
    }

    fun setReadChapter(chapter: Chapter) {
        _currentChapter.postValue(chapter)
    }

    fun getImageList(chapter: Chapter, scraperConfigs: ScraperConfigs?) {
        if (chapter.url.isNullOrBlank() || scraperConfigs == null) {
            _networkState.postValue(Failure(errorMessage = R.string.manga_details_incorrect_input))
            return
        }
        viewModelScope.launch {
            val mangaService = MangaService(scraperConfigs)
            val imageList = mangaService.getImagePathsByChapter(chapter.url)
            _imageList.postValue(imageList)
        }
    }
}