package com.strembaba.admin.data

import com.google.firebase.firestore.PropertyName

data class HomePageList(
    val name: String,
    var list: List<Data>
)

data class Data @JvmOverloads constructor(
    @get:PropertyName("name") @set:PropertyName("name") var name: String = "",
    @get:PropertyName("type") @set:PropertyName("type") var type: String = "",
    @get:PropertyName("description") @set:PropertyName("description") var description: String = "",
    @get:PropertyName("year") @set:PropertyName("year") var year: Int = 2000,
    @get:PropertyName("photo") @set:PropertyName("photo") var photo: String = "",
    @get:PropertyName("language") @set:PropertyName("language") var language: String = "",
    @get:PropertyName("webLink") @set:PropertyName("webLink") var webLink: String = "",
    @get:PropertyName("downloadLink") @set:PropertyName("downloadLink") var downloadLink: String = "",
    @get:PropertyName("genres") @set:PropertyName("genres") var genres: String = "",
    @get:PropertyName("rating") @set:PropertyName("rating") var rating: String = "0.0",
    @get:PropertyName("isMovie") @set:PropertyName("isMovie") var isMovie: Boolean = true,
    @get:PropertyName("isBanner") @set:PropertyName("isBanner") var isBanner: Boolean = false
)

data class Collection @JvmOverloads constructor(
    @get:PropertyName("Types") @set:PropertyName("Types") var Types: String = ""
)


data class EpData @JvmOverloads constructor(
    @get:PropertyName("name") @set:PropertyName("name") var name: String = "",
    @get:PropertyName("webLink") @set:PropertyName("webLink") var webLink: String = "",
    @get:PropertyName("downloadLink") @set:PropertyName("downloadLink") var downloadLink: String = ""
)


