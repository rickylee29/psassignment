package com.example.psassignment.domain.algorithm

import org.junit.Assert.assertArrayEquals
import org.junit.Assert.assertEquals
import org.junit.Test

class HungarianAlgorithmTest {

    private val algorithm = HungarianAlgorithm()

    @Test
    fun `computeAssignments finds optimal assignment for MAX SCORE`() {
        // --- GIVEN ---
        // We want to verify the algorithm MAXIMIZES the total score.
        // Row 0 (Driver 1): Best score is 12.0 at Index 1
        // Row 1 (Driver 2): Best score is 10.0 at Index 0
        // Row 2 (Driver 3): Best score is 11.0 at Index 2
        val scoreMatrix = arrayOf(
            doubleArrayOf(5.0, 12.0, 3.0),
            doubleArrayOf(10.0, 6.0, 5.0),
            doubleArrayOf(4.0, 8.0, 11.0)
        )

        // --- WHEN ---
        val result = algorithm.computeAssignments(scoreMatrix)

        // --- THEN ---
        // Expected: [1, 0, 2]
        // Driver 0 -> Shipment 1 (Score 12)
        // Driver 1 -> Shipment 0 (Score 10)
        // Driver 2 -> Shipment 2 (Score 11)
        val expected = intArrayOf(1, 0, 2)

        assertArrayEquals("Algorithm should pick indices with the highest scores", expected, result)
    }

    @Test
    fun `computeAssignments handles equal scores correctly`() {
        // --- GIVEN ---
        // Edge Case: If every driver has the EXACT SAME score for every shipment (e.g., 10.0),
        // any valid unique assignment is optimal.
        // The algorithm typically defaults to the diagonal (0->0, 1->1, etc.) purely based on index order.
        val n = 5
        val matrix = Array(n) { DoubleArray(n) { 10.0 } } // All scores are 10.0

        // --- WHEN ---
        val result = algorithm.computeAssignments(matrix)

        // --- THEN ---
        // 1. Verify we got exactly N assignments (5)
        assertEquals("Should return exactly 5 assignments", 5, result.size)

        // 2. Verify all assigned shipments are unique (No two drivers share a shipment)
        // We do this by converting the result array to a Set. If size is still 5, all were unique.
        val uniqueShipments = result.toSet()
        assertEquals("Every driver must be assigned a unique shipment", 5, uniqueShipments.size)
    }

    @Test
    fun `computeAssignments is fast enough for N=100`() {
        // --- GIVEN ---
        // Performance Sanity Check: A 100x100 matrix of random scores.
        // This simulates a "heavy" but realistic load for the app.
        val n = 100
        val matrix = Array(n) { DoubleArray(n) { Math.random() * 100 } }

        // --- WHEN ---
        val start = System.currentTimeMillis()
        algorithm.computeAssignments(matrix)
        val end = System.currentTimeMillis()

        // --- THEN ---
        val duration = end - start
        println("Execution time for N=$n: ${duration}ms")

        // Fail if it takes longer than 200ms.
        // (On a modern laptop, this usually runs in < 50ms)
        assert(duration < 200) { "Algorithm is too slow! Took ${duration}ms" }
    }
}