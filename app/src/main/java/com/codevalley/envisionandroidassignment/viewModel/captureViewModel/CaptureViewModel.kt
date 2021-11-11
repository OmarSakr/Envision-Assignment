package com.codevalley.envisionandroidassignment.viewModel.captureViewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.codevalley.envisionandroidassignment.model.documentModel.DocumentModel
import com.codevalley.envisionandroidassignment.model.library.Library
import com.codevalley.envisionandroidassignment.repositories.homeRepository.HomeRepository
import com.codevalley.envisionandroidassignment.utils.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import retrofit2.Response

class CaptureViewModel(private val homeRepository: HomeRepository) : ViewModel() {
    private val _mutableStateFlowReadDocument: MutableStateFlow<Resource<Response<DocumentModel>>> =
        MutableStateFlow(
            Resource.loading(null)
        )
    val mutableStateFlowReadDocument: MutableStateFlow<Resource<Response<DocumentModel>>> =
        _mutableStateFlowReadDocument

    fun getReadDocument(photo: MultipartBody.Part) {
        viewModelScope.launch(Dispatchers.IO)
        {
            _mutableStateFlowReadDocument.emit(Resource.loading(data = null))
            try {
                _mutableStateFlowReadDocument.emit(
                    Resource.success(
                        data = homeRepository.getReadDocument(photo)
                    )
                )
            } catch (exception: Exception) {
                _mutableStateFlowReadDocument.emit(
                    Resource.error(
                        data = null,
                        message = exception.message ?: "Something went wrong.!"
                    )
                )
            }
        }
    }

    fun addParagraph(library: Library) = viewModelScope.launch {
        homeRepository.addParagraph(library)
    }

}