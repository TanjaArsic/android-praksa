package com.example.thingy.network.model

import com.google.gson.annotations.SerializedName

data class StreamDto(
    @SerializedName("EiD")
    val id: String?,

    @SerializedName("UiD")
    val uid: String?,

    val acl: String?,

    val axon: String?,

    @SerializedName("axon_id")
    val axonId: Int?,

    val brain: String?,

    @SerializedName("brain_id")
    val brainId: Int?,

    val created: String?,

    val data: String?,

    val description: String?,

    val name: String?,

    val type: String?,

    val updated: String?,

    val validity: String?
)
