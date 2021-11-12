package com.codevalley.envisionandroidassignment.repositories.library

import com.codevalley.envisionandroidassignment.dao.LibraryDao
import com.codevalley.envisionandroidassignment.model.library.Library
import kotlinx.coroutines.flow.Flow

class LibraryRepository(libraryDao: LibraryDao) {
    val getLibrary: Flow<List<Library>> = libraryDao.getLibrary()
}