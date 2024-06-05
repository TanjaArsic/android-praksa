package com.example.thingy.network.model

import com.google.gson.annotations.SerializedName

data class FlowItemDto(
    @SerializedName("timestamp")
    val timestamp: String?,

    @SerializedName("value")
    val value: String?
)
