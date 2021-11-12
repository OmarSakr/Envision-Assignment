package com.codevalley.envisionandroidassignment.viewModel.library

import androidx.lifecycle.ViewModel
import com.codevalley.envisionandroidassignment.model.library.Library
import com.codevalley.envisionandroidassignment.repositories.library.LibraryRepository
import kotlinx.coroutines.flow.Flow

class LibraryViewModel(libraryRepository: LibraryRepository) : ViewModel() {

    val getLibrary: Flow<List<Library>> = libraryRepository.getLibrary

}