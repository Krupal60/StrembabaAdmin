package com.strembaba.admin

import android.content.Context
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.strembaba.admin.component.CircularProgressButton
import com.strembaba.admin.component.DeleteCircularProgressButton
import com.strembaba.admin.component.genresTextfield
import com.strembaba.admin.component.ratingTextfield
import com.strembaba.admin.component.textfield
import com.strembaba.admin.data.Collection
import com.strembaba.admin.data.Data
import com.strembaba.admin.data.EpData
import com.strembaba.admin.ui.theme.StrembabaAdminTheme

class MainActivity : ComponentActivity() {

    private var isLoadingDone = true
    private var isdeletedimageDone = true
    private var isuploadDone = true
    private var isdeletedDone = true
    private val uploadViewModel: MainViewModel by viewModels()

    @OptIn(ExperimentalGlideComposeApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            StrembabaAdminTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    var selectedImageUri by remember {
                        mutableStateOf<Uri?>(null)
                    }
                    var nameTextFieldValue by remember {
                        mutableStateOf("")
                    }
                    var typeTextFieldValue by remember {
                        mutableStateOf("")
                    }
                    var descriptionTextFieldValue by remember {
                        mutableStateOf("")
                    }

                    var isChecked by remember {
                        mutableStateOf(true)
                    }

                    var isBanner by remember {
                        mutableStateOf(false)
                    }
                    var showAddImage by remember {
                        mutableStateOf(false)
                    }
                    var yearTextFieldValue by remember {
                        mutableStateOf("")
                    }
                    var languageTextFiledValue by remember {
                        mutableStateOf("")
                    }
                    var epTextFiledValue by remember {
                        mutableStateOf("")
                    }
                    var webLinkTextFiledValue by remember {
                        mutableStateOf("")
                    }
                    var downloadLinkTextFiledValue by remember {
                        mutableStateOf("")
                    }
                    var ratingTextFiledValue by remember {
                        mutableStateOf("")
                    }
                    val urlText = uploadViewModel.downloadUrl.collectAsState().value

                    val isMovieData = uploadViewModel.isMovieData.collectAsState().value

                    val isBannerData = uploadViewModel.isBannerData.collectAsState().value


                    val searchData: Data? by uploadViewModel.searchdata.collectAsState()


                    var genresTextFiledValue = mutableListOf("")


                    val context = LocalContext.current
                    val singlePhotoPicker =
                        rememberLauncherForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
                            selectedImageUri = uri
                            if (uri != null) {
                                showAddImage = true
                            }
                        }


                    val launcher = rememberLauncherForActivityResult(
                        ActivityResultContracts.PickVisualMedia()
                    ) { uri: Uri? ->
                        selectedImageUri = uri
                    }


                    Column(
                        modifier = Modifier
                            .background(color = MaterialTheme.colorScheme.surface)
                            .fillMaxSize()
                            .verticalScroll(rememberScrollState()),
                        verticalArrangement = Arrangement.Top,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {

                        AnimatedVisibility(visible = showAddImage) {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth(0.5f)
                                    .height(350.dp)
                                    .padding(top = 15.dp)
                            ) {
                                GlideImage(
                                    model = selectedImageUri,
                                    contentScale = ContentScale.Crop,
                                    contentDescription = "image"
                                )
                            }
                        }

                        Row(Modifier.padding(top = 15.dp)) {

                            Button(modifier = Modifier.height(45.dp),
                                onClick = {
                                    when (PackageManager.PERMISSION_GRANTED) {
                                        ContextCompat.checkSelfPermission(
                                            context,
                                            android.Manifest.permission.READ_EXTERNAL_STORAGE
                                        ) -> {

                                            singlePhotoPicker.launch(
                                                PickVisualMediaRequest(
                                                    ActivityResultContracts.PickVisualMedia.ImageOnly
                                                )
                                            )
                                            // Some works that require permission


                                            Log.d("ExampleScreen", "Code requires permission")
                                        }

                                        else -> {
                                            // Asking for permission
                                            launcher.launch( PickVisualMediaRequest(mediaType = ActivityResultContracts.PickVisualMedia.ImageOnly))
                                        }


                                    }
                                }
                            ) {
                                Text("Add Photo")
                            }

                            Spacer(modifier = Modifier.padding(start = 10.dp))


                            CircularProgressButton(Modifier, "Upload Photo", onclick = {
                                if (isLoadingDone) {
                                    if (uploadViewModel.imageValidation(
                                            nameTextFieldValue,
                                            typeTextFieldValue
                                        ) && (selectedImageUri != null)
                                    ) {
                                        isLoadingDone = false
                                        uploadViewModel.startUpload()
                                        uploadPhotoToFirebase(
                                            photoUri = selectedImageUri!!,
                                            typeTextFieldValue,
                                            nameTextFieldValue,
                                            context
                                        )
                                    }
                                }
                            }, uploadViewModel.isUploading.collectAsState().value)

                            Spacer(modifier = Modifier.padding(start = 10.dp))


                            DeleteCircularProgressButton(Modifier, "Delete Photo", onclick = {
                                if (isdeletedimageDone) {
                                    if (nameTextFieldValue.trim()
                                            .isNotEmpty()
                                    ) {
                                        isdeletedimageDone = false
                                        uploadViewModel.startDeletingImage()
                                        deleteUploadedImage(
                                            name = nameTextFieldValue,
                                            type = typeTextFieldValue,
                                            context = context
                                        )
                                    }
                                }
                            }, uploadViewModel.isDeleteImage.collectAsState().value)
                        }

                        Row(
                            modifier = Modifier.padding(start = 16.dp, end = 16.dp, top = 10.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = uploadViewModel.downloadUrl.collectAsState().value,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis,
                                modifier = Modifier.fillMaxWidth(0.75f)
                            )

                            Button(onClick = {
                                if (uploadViewModel.imageValidation(
                                        nameTextFieldValue,
                                        typeTextFieldValue
                                    )
                                ) {
                                    uploadViewModel.loadData(typeTextFieldValue, nameTextFieldValue)
                                    uploadViewModel.getLinkOfImageUpdateItem(
                                        FirebaseStorage.getInstance(),
                                        nameTextFieldValue,
                                        typeTextFieldValue
                                    )
                                }
                            }) {
                                Text("Check")
                            }

                        }

                        AnimatedVisibility(
                            visible = uploadViewModel.downloadUrl.collectAsState().value.trim()
                                .isNotEmpty()
                        ) {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth(0.5f)
                                    .height(350.dp)
                                    .padding(vertical = 15.dp)
                            ) {
                                GlideImage(
                                    model = uploadViewModel.downloadUrl.collectAsState().value,
                                    contentScale = ContentScale.Crop,
                                    contentDescription = "image"
                                )
                            }
                        }

                        Row(
                            modifier = Modifier
                                .padding(start = 16.dp, end = 16.dp, top = 10.dp)
                                .align(Alignment.CenterHorizontally),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row( horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    text = "is Movie Data ? :",
                                    modifier = Modifier.clickable(onClick = { isChecked = !isChecked }))
                                Checkbox(checked = isChecked, onCheckedChange = {
                                    isChecked = it
                                    if (isChecked) {
                                        uploadViewModel.movieData()
                                    } else {
                                        uploadViewModel.notMoviesData()
                                    }
                                })
                            }

                            Row( horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    text = "is Banner Data ? :",
                                    modifier = Modifier.clickable(onClick = { isBanner = !isBanner }))
                                Checkbox(checked = isBanner, onCheckedChange = {
                                    isBanner = it
                                    if (isBanner) {
                                        uploadViewModel.bannerData()
                                    } else {
                                        uploadViewModel.notbannerData()
                                    }
                                })
                            }
                        }

                        val nameValue = textfield(
                            Modifier,
                            "Enter name",
                            KeyboardType.Text,
                            searchData?.name ?: ""
                        )
                        nameTextFieldValue = nameValue

                        val typeValue = textfield(Modifier, "Enter type", KeyboardType.Text, searchData?.type ?: "")
                        typeTextFieldValue = typeValue

                        val descriptionValue =
                            textfield(
                                Modifier, "Enter Description", KeyboardType.Text,
                                searchData?.description ?: ""
                            )
                        descriptionTextFieldValue = descriptionValue

                        val yearValue = textfield(
                            Modifier, "Enter year", KeyboardType.Number,
                            searchData?.year?.toString() ?: ""
                        )
                        yearTextFieldValue = yearValue

                        val languageValue = textfield(
                            Modifier,
                            "Enter language",
                            KeyboardType.Text,
                            searchData?.language ?: ""
                        )
                        languageTextFiledValue = languageValue


                        AnimatedVisibility(visible = !isMovieData) {
                            val epValue =
                                textfield(
                                    Modifier,
                                    "Enter Episode Name",
                                    KeyboardType.Text,
                                    ""
                                )
                            epTextFiledValue = epValue
                        }

                        val webLinkValue =
                            textfield(
                                Modifier,
                                "Enter Play Weblink",
                                KeyboardType.Text,
                                if (isMovieData) searchData?.webLink ?: "" else ""
                            )
                        webLinkTextFiledValue = webLinkValue


                        val downloadLinkValue =
                            textfield(
                                Modifier,
                                "Enter Download Weblink",
                                KeyboardType.Text,
                                if (isMovieData) searchData?.downloadLink?: "" else ""
                            )
                        downloadLinkTextFiledValue = downloadLinkValue


                        val genresValue =
                            genresTextfield(
                                Modifier,
                                "Enter genres",
                                KeyboardType.Text,
                                searchData?.genres ?: ""
                            )
                        genresTextFiledValue.add(
                            genresValue
                        )
                        genresTextFiledValue =
                            genresTextFiledValue.subList(1, genresTextFiledValue.size)
                        Log.d("data genresTextFiledValue", "$genresTextFiledValue")

                        val ratingValue =
                            ratingTextfield(
                                Modifier,
                                "Enter rating",
                                KeyboardType.Decimal,
                                searchData?.rating ?: ""
                            )
                        ratingTextFiledValue = ratingValue

                        Row(Modifier.padding(top = 15.dp, bottom = 15.dp)) {

                            CircularProgressButton(Modifier, "Upload Data", onclick = {
                                if (isuploadDone) {
                                    val validation = uploadViewModel.dataValidation(
                                        name = nameTextFieldValue,
                                        typeTextFieldValue,
                                        descriptionTextFieldValue,
                                        yearTextFieldValue,
                                        urlText,
                                        if (isMovieData) webLinkTextFiledValue else "..",
                                        if (isMovieData) downloadLinkTextFiledValue else "..",
                                        languageTextFiledValue,
                                        genresTextFiledValue,
                                        ratingTextFiledValue
                                    )
                                    if (validation) {
                                        isuploadDone = false
                                        uploadViewModel.startUploadData()
                                        uploadData(
                                            fireStore = Firebase.firestore,
                                            Type = typeTextFieldValue,
                                            Data = Data(
                                                name = nameTextFieldValue,
                                                type = typeTextFieldValue,
                                                description = descriptionTextFieldValue,
                                                year = yearTextFieldValue.toInt(),
                                                photo = urlText,
                                                language = languageTextFiledValue,
                                                webLink = if (isMovieData) webLinkTextFiledValue else "",
                                                downloadLink = if (isMovieData) downloadLinkTextFiledValue else "",
                                                genres = genresTextFiledValue.toString(),
                                                rating = ratingTextFiledValue,
                                                isMovie = isMovieData,
                                                isBanner = isBannerData
                                            ),
                                            epData = EpData(
                                                name = epTextFiledValue,
                                                webLink = webLinkTextFiledValue,
                                                downloadLink = downloadLinkTextFiledValue
                                            ),
                                            epName = epTextFiledValue,
                                            name = nameTextFieldValue,
                                            isMovie = isMovieData,
                                            context = context
                                        )
                                    }
                                }
                            }, uploadViewModel.isUploadingData.collectAsState().value)

                            Spacer(modifier = Modifier.padding(start = 10.dp))

                            CircularProgressButton(Modifier, "Delete Data", onclick = {
                                if (isdeletedDone) {
                                    val dataDeletevalidation = uploadViewModel.dataDeleteValidation(
                                        nameTextFieldValue,
                                        typeTextFieldValue
                                    )
                                    if (dataDeletevalidation) {
                                        isdeletedDone = false
                                        uploadViewModel.startDeletingData()
                                        deleteUploadedData(
                                            fireStore = Firebase.firestore,
                                            Type = typeTextFieldValue,
                                            name = nameTextFieldValue,
                                            context = context
                                        )
                                    }
                                }
                            }, uploadViewModel.isDeletedData.collectAsState().value)

                        }

                    }

                }
            }
        }
    }

    private fun uploadPhotoToFirebase(photoUri: Uri, type: String, name: String, context: Context) {
        // Create a reference to the Firebase Storage bucket.
        val storageRef = FirebaseStorage.getInstance().getReference("images")
        // Create a reference to the specific photo in the bucket.
        val photoRef = storageRef.child(type).child(name)
        // Upload the photo to the bucket.
        photoRef.putFile(photoUri)
            .addOnSuccessListener {
                // The photo has been uploaded successfully.
                isLoadingDone = true
                uploadViewModel.finishUpload()
                Toast.makeText(context, "upload successfully ", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener {
                // The photo upload failed.
                isLoadingDone = true
                uploadViewModel.finishUpload()
                Toast.makeText(context, "upload failed ", Toast.LENGTH_SHORT).show()
            }
    }

    private fun uploadData(
        fireStore: FirebaseFirestore,
        Type: String,
        name: String,
        Data: Data,
        epData: EpData,
        epName: String,
        isMovie: Boolean,
        context: Context
    ) {
        Log.d("upload Data", "$Data")
        val documentReference = fireStore.collection(Type).document(name)
        if (Type != "Banner") {
            val collectionNames = fireStore.collection("collectionNames")
            val query = collectionNames.whereEqualTo("Types", Type)
            query.get().addOnSuccessListener {
                if (it.isEmpty) {
                    val TypeReference = fireStore.collection("collectionNames").document()
                    TypeReference.set(Collection(Type))
                        .addOnSuccessListener { Log.d("uploaded", "upload data success") }
                }
            }

        }
        if (isMovie) {
            documentReference.set(Data).addOnSuccessListener {
                isuploadDone = true
                uploadViewModel.finishUploadData()
                Log.d("uploaded", "upload data success")
                Toast.makeText(context, "uploaded successfully", Toast.LENGTH_SHORT).show()
            }
                .addOnFailureListener {
                    isuploadDone = true
                    uploadViewModel.finishUploadData()
                    Toast.makeText(context, "upload failed", Toast.LENGTH_SHORT).show()
                }
        } else {

            documentReference.set(Data).addOnSuccessListener {
                val epdocumentReference = fireStore.collection(Type)
                    .document(name)
                    .collection(name)
                    .document(epName)

                epdocumentReference.set(epData).addOnSuccessListener {
                    isuploadDone = true
                    uploadViewModel.finishUploadData()
                    Toast.makeText(context, "uploaded Data successfully", Toast.LENGTH_SHORT).show()
                }
                    .addOnFailureListener {
                        isuploadDone = true
                        uploadViewModel.finishUploadData()
                        Toast.makeText(context, "upload Ep failed", Toast.LENGTH_SHORT).show()
                    }
            }
                .addOnFailureListener {
                    isuploadDone = true
                    uploadViewModel.finishUploadData()
                    Toast.makeText(context, "upload Data failed", Toast.LENGTH_SHORT).show()
                }
        }
    }


    private fun deleteUploadedData(
        fireStore: FirebaseFirestore,
        Type: String,
        name: String,
        context: Context
    ) {
        val documentReference = fireStore.collection(Type).document(name)
        documentReference.delete().addOnSuccessListener {
            val storageRef = FirebaseStorage.getInstance().getReference("images")
            val photoRef = storageRef.child(Type).child(name)
            photoRef.delete().addOnSuccessListener {
                isdeletedDone = true
                uploadViewModel.finishDeletingData()
                Log.d("delete", "delete data success")
                Toast.makeText(context, "deleted successfully", Toast.LENGTH_SHORT).show()
            }
                .addOnFailureListener {
                    isdeletedDone = true
                    uploadViewModel.finishDeletingData()
                    Toast.makeText(context, "delete failed", Toast.LENGTH_SHORT).show()
                }
        }
            .addOnFailureListener {
                isdeletedDone = true
                uploadViewModel.finishDeletingData()
                Toast.makeText(context, "delete failed", Toast.LENGTH_SHORT).show()
            }
    }

    private fun deleteUploadedImage(
        name: String,
        type: String,
        context: Context
    ) {
        val storageRef = FirebaseStorage.getInstance().getReference("images")
        val photoRef = storageRef.child(type).child(name)
        photoRef.delete().addOnSuccessListener {
            isdeletedimageDone = true
            uploadViewModel.finishDeletingImage()
            Log.d("delete", "Delete Image success")
            Toast.makeText(context, "Deleted successfully", Toast.LENGTH_SHORT).show()
        }
            .addOnFailureListener {
                isdeletedimageDone = true
                uploadViewModel.finishDeletingImage()
                Toast.makeText(context, "Delete failed", Toast.LENGTH_SHORT).show()
            }
    }

}

