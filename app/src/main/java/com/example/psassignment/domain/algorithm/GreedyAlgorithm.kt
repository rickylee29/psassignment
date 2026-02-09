package com.example.psassignment.domain.algorithm

import javax.inject.Inject

/**
 * Strategy: Global Maximum
 * 1. Find the highest score in the entire remaining cost matrix.
 * 2. Assign that driver to that shipment.
 * 3. Lock that row (driver) and column (shipment).
 * 4. Repeat until all drivers are assigned.
 *
 * pros
 * good enough solution
 * simpler to implement
 * faster O(N^2) than hungarian: scan and sort
 * can be used in large data
 * cons
 * ** Not best solution ** because the total of other combination could be higher.
 *
 * improvement - Hungarian Algorithm
 */
class GreedyAlgorithm @Inject constructor(): RoutingAlgorithm {

    override fun computeAssignments(costMatrix: Array<DoubleArray>): IntArray {
        println("Greedy Algorithm")

        val n = costMatrix.size
        val result = IntArray(n) { -1 }

        val driverIsAssigned = BooleanArray(n)
        val shipmentIsAssigned = BooleanArray(n)
        var assignmentsCount = 0

        // We need to make N assignments
        while (assignmentsCount < n) {
            var maxScore = -1.0
            var bestDriverIndex = -1
            var bestShipmentIndex = -1

            // 1. Scan the whole matrix for the best remaining score
            // It looks at every single remaining cell in the matrix.
            // It doesn't care which driver gets the shipment.
            // It only cares about finding the highest number currently left on the board.
            for (driver in 0 until n) {
                if (driverIsAssigned[driver]) continue              // Skip busy drivers

                for (shipment in 0 until n) {
                    if (shipmentIsAssigned[shipment]) continue      // Skip taken shipments

                    val score = costMatrix[driver][shipment]
                    if (score > maxScore) {
                        maxScore = score
                        bestDriverIndex = driver
                        bestShipmentIndex = shipment
                    }
                }
            }

            // 2. Make the assignment if a valid pair was found
            // Once the highest number is found, it permanently assigns that pair.
            // They are removed from the boolean arrays, so they will be skipped in the next pass.
            if (bestDriverIndex != -1 && bestShipmentIndex != -1) {
                result[bestDriverIndex] = bestShipmentIndex
                driverIsAssigned[bestDriverIndex] = true
                shipmentIsAssigned[bestShipmentIndex] = true
                assignmentsCount++
            } else {
                // Should not happen in a square matrix, but good for safety
                break
            }
        }

        return result
    }
}