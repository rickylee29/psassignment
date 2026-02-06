package com.example.psassignment.domain.algorithm

import javax.inject.Inject
import kotlin.math.max
import kotlin.math.min

/**
 * Implementation of the Hungarian Algorithm.
 *
 * pros
 * best solution
 * **maximize the Total Suitability Score.**
 * cons
 * more complicated to implement
 * slower than greedy O(N^3)
 *
 */
class HungarianAlgorithm @Inject constructor() : RoutingAlgorithm {

    /**
     * Computes the maximum weight matching for a square matrix.
     * @param costMatrix A 2D array where costMatrix[i][j] is the score of worker i performing task j.
     * @return An IntArray where index is the row (Driver) and value is the column (Shipment).
     */
    override fun computeAssignments(costMatrix: Array<DoubleArray>): IntArray {
        val rows = costMatrix.size
        val cols = costMatrix[0].size

        val n = max(rows, cols)


        // Extract results
        val result = IntArray(n)

        return result
    }
}