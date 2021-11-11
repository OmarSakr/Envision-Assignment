package com.codevalley.envisionandroidassignment.network

import okhttp3.MultipartBody

class ApiHelper(private val apiService: AppServices) {
    suspend fun getReadDocument(
        photo: MultipartBody.Part
    ) =
        apiService.getReadDocument(
            photo
        )
}