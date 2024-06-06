package com.strembaba.admin

import android.util.Log
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.strembaba.admin.data.Data
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class MainViewModel : ViewModel() {

    private var _isUploading: MutableStateFlow<Boolean> = MutableStateFlow(false)
    val isUploading: StateFlow<Boolean> = _isUploading.asStateFlow()

    private val _searchdata : MutableStateFlow<Data?> = MutableStateFlow(null)
    val searchdata: StateFlow<Data?> = _searchdata

    private var _isMovieData: MutableStateFlow<Boolean> = MutableStateFlow(true)
    val isMovieData: StateFlow<Boolean> = _isMovieData.asStateFlow()

    private var _isBannerData: MutableStateFlow<Boolean> = MutableStateFlow(true)
    val isBannerData: StateFlow<Boolean> = _isBannerData.asStateFlow()

    private var _isDeleteImage: MutableStateFlow<Boolean> = MutableStateFlow(false)
    val isDeleteImage: StateFlow<Boolean> = _isDeleteImage.asStateFlow()

    private var _isUploadingData: MutableStateFlow<Boolean> = MutableStateFlow(false)
    val isUploadingData: StateFlow<Boolean> = _isUploadingData.asStateFlow()

    private var _isDeletedData: MutableStateFlow<Boolean> = MutableStateFlow(false)
    val isDeletedData: StateFlow<Boolean> = _isDeletedData.asStateFlow()

    private var _downloadUrl: MutableStateFlow<String> = MutableStateFlow("")
    val downloadUrl: StateFlow<String> = _downloadUrl.asStateFlow()

    fun startUpload() {
        _isUploading.value = true
    }

    fun movieData() {
        _isMovieData.value = true
    }
    fun bannerData() {
        _isBannerData.value = true
    }
    fun notbannerData() {
        _isBannerData.value = false
    }

    fun notMoviesData() {
        _isMovieData.value = false
    }

    fun finishUpload() {
        _isUploading.value = false
    }

    fun startDeletingImage() {
        _isDeleteImage.value = true
    }

    fun finishDeletingImage() {
        _isDeleteImage.value = false
    }

    fun startUploadData() {
        _isUploadingData.value = true
    }

    fun finishUploadData() {
        _isUploadingData.value = false
    }

    fun startDeletingData() {
        _isDeletedData.value = true
    }

    fun finishDeletingData() {
        _isDeletedData.value = false
    }

    fun imageValidation(name: String, type: String): Boolean {
        return name.trim().isNotEmpty() && type.trim().isNotEmpty()
    }

    fun dataValidation(
        name: String,
        type: String,
        description: String,
        year: String,
        url: String,
        weburl: String,
        downloadurl: String,
        language: String,
        genres: MutableList<String>,
        rating: String
    ): Boolean {
        return name.isNotBlank() &&
                type.isNotBlank() &&
                description.isNotBlank() &&
                year.isNotBlank() &&
                url.isNotBlank() &&
                weburl.isNotBlank() &&
                downloadurl.isNotBlank() &&
                language.isNotBlank() &&
                genres.isNotEmpty() &&
                rating.isNotBlank()
    }

    fun dataDeleteValidation(
        name: String,
        type: String,
    ): Boolean {
        return name.isNotBlank() &&
                type.isNotBlank()

    }


    fun getLinkOfImageUpdateItem(
        firebaseStorage: FirebaseStorage,
        imageUpdateId: String,
        typeUpdateId: String
    ) {
        val letelsUpdateReference =
            firebaseStorage.reference.child("images").child(typeUpdateId).child(imageUpdateId)
        val downloadurl = letelsUpdateReference.downloadUrl

        downloadurl.addOnSuccessListener { _downloadUrl.value = it.toString() }
            .addOnFailureListener {
                _downloadUrl.value = ""
                Log.d("image f", _downloadUrl.value)
            }
    }


    fun loadData(type: String, name: String) {

            val firestore = Firebase.firestore

                val docRef: DocumentReference = firestore.collection(type).document(name)
                docRef.get()
                    .addOnSuccessListener { document ->
                        val datavlue = document.toObject(Data::class.java)
                        _searchdata.value = datavlue
                        Log.d("datavlue0","$datavlue")
                    }



    }


}