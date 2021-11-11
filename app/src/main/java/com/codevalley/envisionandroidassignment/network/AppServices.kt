package com.codevalley.envisionandroidassignment.network

import com.codevalley.envisionandroidassignment.model.documentModel.DocumentModel
import okhttp3.MultipartBody
import retrofit2.Response
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part


interface AppServices {
    @Multipart
    @POST(AppUrls.readDocument)
    suspend fun getReadDocument(
        @Part photo: MultipartBody.Part,
    ): Response<DocumentModel>

}