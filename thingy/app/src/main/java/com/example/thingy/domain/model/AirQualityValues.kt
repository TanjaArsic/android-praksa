package com.example.thingy.domain.model
data class AirQualityValues(
    val minValue: Double = 0.0,
    val maxValue: Double,
    val good: Double,
    val moderate: Double,
    val unhealthyFSG: Double,
    val unhealthy: Double,
    val veryUnhealthy: Double
)
