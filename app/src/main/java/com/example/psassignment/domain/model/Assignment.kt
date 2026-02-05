package com.example.psassignment.domain.model

/**
 * Represents a final matched assignment between a Driver and a Shipment.
 * calculated by the Optimization Engine.
 */
data class Assignment(
    val driverName: String,
    val shipmentAddress: String,
    val suitabilityScore: Double
)