package com.example.thingy.network.model

import com.example.thingy.domain.model.AirQualityValues
import com.example.thingy.domain.model.Stream
import com.example.thingy.domain.model.StreamType
import com.example.thingy.domain.model.StreamType.*

fun StreamDto.toStream(): Stream {

    return Stream(
        eId = id,
        uId = uid,
        acl = acl,
        axon = axon,
        axonId = axonId,
        brain = brain,
        brainId = brainId,
        createdAt = created,
        description = description,
        name = name,
        type = name?.toType(),
        airQualityData = name?.toType()?.toAirQualityValues(),
        updatedAt = updated,
        validity = validity
    )
}
fun String.toType(): StreamType{

    return when (this) {
        "Temperature" -> TEMPERATURE
        "Humidity" -> HUMIDITY
        "FPM" -> FPM
        "RPM" -> RPM
        "Pressure" -> PRESSURE
        "Noise" -> NOISE
        "CO" -> CO
        "NH3" -> NH3
        "NO2" -> NO2
        "EAQI" -> EAQI
        else -> NOISE
    }
}
fun StreamType.toAirQualityValues(): AirQualityValues {

    return when (this) {
        TEMPERATURE -> AirQualityValues(-40.0, 50.0, 20.0, 25.0, 30.0, 40.0, 50.0)
        HUMIDITY    -> AirQualityValues(0.0, 100.0, 30.0, 50.0, 85.0, 95.0, 99.99)
        FPM         -> AirQualityValues(0.0, 500.0, 50.0, 100.0, 150.0, 200.0, 300.0)
        RPM         -> AirQualityValues(0.0, 500.0, 54.0, 154.0, 254.0, 354.0, 424.0)
        PRESSURE    -> AirQualityValues(870.0, 1083.8, 980.0, 1013.0, 1083.8, 1083.8, 1083.8)
        NOISE       -> AirQualityValues(0.0, 130.0, 30.0, 60.0, 85.0, 120.0, 130.0)
        CO          -> AirQualityValues(0.0, 1280.0, 5.0, 9.0, 35.0, 800.0, 1280.0)
        NH3         -> AirQualityValues(0.0, 80.0, 25.0, 26.0, 26.0, 50.0, 80.0)
        NO2         -> AirQualityValues(0.0, 2.05, 0.053, 0.100, 0.360, 0.649, 1.249)
        EAQI        -> AirQualityValues(0.0, 500.0, 50.0, 89.0, 100.0, 200.0, 300.0)
    }
}
