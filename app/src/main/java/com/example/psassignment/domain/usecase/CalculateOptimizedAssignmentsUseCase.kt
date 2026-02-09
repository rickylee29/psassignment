package com.example.psassignment.domain.usecase

import com.example.psassignment.domain.algorithm.RoutingAlgorithm
import com.example.psassignment.domain.algorithm.SuitabilityScorer
import com.example.psassignment.domain.algorithm.calculateTotalScore
import com.example.psassignment.domain.model.Assignment
import com.example.psassignment.domain.repository.DataRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

/**
 * This UseCase acts as the central orchestrator for the application's core feature
 * It encapsulates the business logic required to fetch raw data,
 * calculate suitability scores for every possible driver-shipment combination,
 * and determine the mathematically optimal assignment that maximizes the total score.
 */
class CalculateOptimizedAssignmentsUseCase @Inject constructor(
    private val repository: DataRepository,
    private val scorer: SuitabilityScorer,
    private val algorithm: RoutingAlgorithm
) {

    // Run on Default dispatcher (CPU intensive)
    suspend operator fun invoke(): List<Assignment> = withContext(Dispatchers.Default) {
        val data = repository.getData()
        val drivers = data.drivers
        val shipments = data.shipments

        if (drivers.isEmpty() || shipments.isEmpty()) return@withContext emptyList()

        // 1. Build the Score Matrix (N x N)
        val matrix = Array(drivers.size) { r ->
            DoubleArray(shipments.size) { c ->
                scorer.calculateScore(drivers[r], shipments[c])
            }
        }

        // 2. Solve for Optimal Indices
        // Returns array where: value = shipmentIndex, index = driverIndex
        val assignmentIndices = algorithm.computeAssignments(matrix)
        val totalScore = algorithm.calculateTotalScore(matrix, assignmentIndices)
        println("TOTAL SCORE: $totalScore")


        // 3. Map back to Domain Objects
        assignmentIndices.mapIndexed { driverIndex, shipmentIndex ->
            val driver = drivers[driverIndex]
            val shipment = shipments[shipmentIndex]
            val score = matrix[driverIndex][shipmentIndex]

            Assignment(
                driverName = driver.name,
                shipmentAddress = shipment.address,
                suitabilityScore = score
            )
        }
    }
}