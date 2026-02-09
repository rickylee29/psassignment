package com.example.psassignment.domain.repository

import com.example.psassignment.domain.model.Driver
import com.example.psassignment.domain.model.Shipment

data class AppData(
    val drivers: List<Driver>,
    val shipments: List<Shipment>
)

interface DataRepository {
    suspend fun getData(): AppData
}