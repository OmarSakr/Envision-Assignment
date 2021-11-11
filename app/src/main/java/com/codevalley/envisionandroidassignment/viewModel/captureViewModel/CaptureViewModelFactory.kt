package com.codevalley.envisionandroidassignment.viewModel.captureViewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.codevalley.envisionandroidassignment.dao.LibraryDao
import com.codevalley.envisionandroidassignment.network.ApiHelper
import com.codevalley.envisionandroidassignment.repositories.homeRepository.HomeRepository

@Suppress("UNCHECKED_CAST")
class CaptureViewModelFactory(
    private val apiHelper: ApiHelper,
    private val libraryDao: LibraryDao
) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CaptureViewModel::class.java)) {
            return CaptureViewModel(HomeRepository(apiHelper, libraryDao)) as T
        }
        throw IllegalArgumentException("Unknown class name")
    }
}