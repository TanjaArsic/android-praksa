package com.example.thingy.network.model

import com.google.gson.annotations.SerializedName
data class AxonDto(

    @SerializedName("EiD")
    val id: String,

    @SerializedName("UiD")
    val uid: String,

    val brain: String,

    @SerializedName("created_at")
    val created: String?,

    val description: String,

    @SerializedName("first_seen")
    val firstSeen: String?,

    val ip: String?,

    @SerializedName("last_seen")
    val lastSeen: String?,

    val latitude: Double?,

    val longitude: Double?,

    val name: String?,

    val streams: List<String>,

    val tags: String,

    @SerializedName("updated")
    val updated: String?,

    val validity: String?
)
