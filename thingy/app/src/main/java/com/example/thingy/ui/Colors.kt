package com.example.thingy.ui

import androidx.compose.ui.graphics.Color

enum class Colors(
    val color: Color,
    val description: String?
) {
    GOOD_AQI(Color(0xFF50F0E6), "Good"),
    FAIR_AQI(Color(0xFF50CCAA), "Fair"),
    MODERATE_AQI(Color(0xA1F0E641), "Moderate"),
    POOR_AQI(Color(0xA1FF5050), "Poor"),
    VERY_POOR_AQI(Color(0xA1960032), "Very Poor"),
    EXTREMELY_POOR_AQI(Color(0xA1A15BAD), "Extremely Poor"),
    UNHEALTHY_FOR_SENSITIVE_GROUPS(Color(0xFFF0E641), "Unhealthy for Sensitive Groups"),
    UNHEALTHY(Color(0xFFFF5050), "Unhealthy"),
    VERY_UNHEALTHY(Color(0xFF960032), "Very Unhealthy"),
    HAZARDOUS(Color(0xFFA15BAD), "Hazardous"),

    SHIMMER_SILVER(Color(0xFFEBEBF4), "Silver Shimmer"),
    SHIMMER_LIGHT_BLUE(Color(0xFF74A7DE), "Blue Shimmer"),
    STREAM_ITEM(Color(0xD35999E1), "Stream Item"),
    AQI_BAR(Color(0xFFB3CDE9), "Aqi Bar"),

    FONT(Color(0xFF435321), "Font Color"),
    ERROR(Color(0xED5A4A80), "Error Color");

    companion object {
        fun String.toColor(): Colors {

            return values().firstOrNull { it.description == this } ?: SHIMMER_SILVER
        }
    }
}

