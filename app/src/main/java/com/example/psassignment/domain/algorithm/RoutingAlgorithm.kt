package com.example.psassignment.domain.algorithm

interface RoutingAlgorithm {
    /**
     * The core contract: Given a cost matrix, return the best assignments.
     * Implementation varies (Greedy vs Hungarian).
     */
    fun computeAssignments(costMatrix: Array<DoubleArray>): IntArray
}

// Extension Function: Keeps the interface pure.
/**
 * A shared utility to calculate the total score of any assignment.
 * @param costMatrix The original scoring matrix.
 * @param assignments The result array where index=Driver, value=Shipment.
 * @return The total accumulated score.
 */
fun RoutingAlgorithm.calculateTotalScore(costMatrix: Array<DoubleArray>, assignments: IntArray): Double {
    var total = 0.0
    val rows = costMatrix.size
    val cols = if (rows > 0) costMatrix[0].size else 0

    for (driverIndex in assignments.indices) {
        val shipmentIndex = assignments[driverIndex]

        if (shipmentIndex >= 0 && shipmentIndex < cols) {
            total += costMatrix[driverIndex][shipmentIndex]
        }
    }
    return total
}