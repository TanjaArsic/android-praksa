package com.example.thingy.network.model

import com.example.thingy.domain.model.Axon

fun AxonDto.toAxon(): Axon {
    return Axon(
        eid = id,
        uid = uid,
        brain = brain,
        createdAt = created,
        description = description,
        firstSeen = firstSeen,
        ip = ip,
        lastSeen = lastSeen,
        latitude = latitude,
        longitude = longitude,
        name = name,
        streams = streams,
        tags = tags,
        updatedAt = updated,
        validity = validity
    )
}


