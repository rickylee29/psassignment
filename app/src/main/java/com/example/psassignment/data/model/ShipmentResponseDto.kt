package com.example.psassignment.data.model

import com.google.gson.annotations.SerializedName

data class ShipmentResponseDto(
    @SerializedName("shipments")
    val shipments: List<String>,

    @SerializedName("drivers")
    val drivers: List<String>
)