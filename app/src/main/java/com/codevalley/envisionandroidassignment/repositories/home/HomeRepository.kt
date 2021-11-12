package com.codevalley.envisionandroidassignment.repositories.home

import androidx.annotation.WorkerThread
import com.codevalley.envisionandroidassignment.dao.LibraryDao
import com.codevalley.envisionandroidassignment.model.library.Library
import com.codevalley.envisionandroidassignment.network.ApiHelper
import okhttp3.MultipartBody

class HomeRepository(private val aApiHelper: ApiHelper, private val libraryDao: LibraryDao) {
    suspend fun getReadDocument(
        photo: MultipartBody.Part
    ) =
        aApiHelper.getReadDocument(
            photo
        )

    @WorkerThread
    suspend fun addParagraph(library: Library) {
        libraryDao.insertParagraph(library)
    }

}