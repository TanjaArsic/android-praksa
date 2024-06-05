package com.example.thingy.domain.model

data class Stream(
    val eId: String?,
    val uId: String?,
    val acl: String?,
    val axon: String?,
    val axonId: Int?,
    val brain: String?,
    val brainId: Int?,
    val createdAt: String?,
    val airQualityData: AirQualityValues?,
    val description: String?,
    val name: String?,
    val type: StreamType?,
    val updatedAt: String?,
    val validity: String?
)
