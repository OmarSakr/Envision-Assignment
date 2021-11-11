package com.codevalley.envisionandroidassignment.viewModel.libraryViewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.codevalley.envisionandroidassignment.dao.LibraryDao
import com.codevalley.envisionandroidassignment.repositories.libraryRepository.LibraryRepository

@Suppress("UNCHECKED_CAST")
class LibraryViewModelFactory(private val libraryDao: LibraryDao) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(LibraryViewModel::class.java)) {
            return LibraryViewModel(LibraryRepository(libraryDao)) as T
        }
        throw IllegalArgumentException("Unknown class name")
    }
}