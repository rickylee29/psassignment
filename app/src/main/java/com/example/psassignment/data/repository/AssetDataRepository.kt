package com.example.psassignment.data.repository

import android.content.Context
import com.example.psassignment.data.model.ShipmentResponseDto
import com.example.psassignment.domain.model.Driver
import com.example.psassignment.domain.model.Shipment
import com.example.psassignment.domain.repository.AppData
import com.example.psassignment.domain.repository.DataRepository
import com.google.gson.Gson
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.BufferedReader
import java.io.InputStreamReader
import javax.inject.Inject

class AssetDataRepository @Inject constructor(
    @ApplicationContext private val context: Context,
    private val gson: Gson
) : DataRepository {

    override suspend fun getData(): AppData = withContext(Dispatchers.IO) {
        try {
            val inputStream = context.assets.open("data.json")
            val reader = BufferedReader(InputStreamReader(inputStream))
            val dto = gson.fromJson(reader, ShipmentResponseDto::class.java)

            // Map DTOs to Domain Models
            // Note: We use the raw string as the 'name'/'address'
            val drivers = dto.drivers.map { Driver(name = it) }
            val shipments = dto.shipments.map { Shipment(address = it) }

            AppData(drivers, shipments)
        } catch (e: Exception) {
            e.printStackTrace()
            // Return empty data on failure, or rethrow if you want the UI to show an error
            AppData(emptyList(), emptyList())
        }
    }
}