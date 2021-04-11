package com.comsuon.trumtruyentranh.ui.viewmodels

sealed class NetworkState {
    object Loading : NetworkState()
    data class Failure(val errorMessage: Any) : NetworkState()
    class Success<T>(val result: T) : NetworkState()
}