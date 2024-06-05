package com.example.thingy.domain.model

data class Axon(
    val eid: String,
    val uid: String,
    val brain: String,
    val createdAt: String?,
    val description: String,
    val firstSeen: String?,
    val ip: String?,
    val lastSeen: String?,
    val latitude: Double?,
    val longitude: Double?,
    val name: String?,
    val streams: List<String>,
    val tags: String,
    val updatedAt: String?,
    val validity: String?
)
