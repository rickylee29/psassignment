package com.example.psassignment.domain.algorithm

import javax.inject.Inject
import kotlin.math.max
import kotlin.math.min

/**
 * Implementation of the Hungarian Algorithm.
 *
 * pros
 * best accurate solution
 * **maximize the Total Suitability Score.**
 * cons
 * more complicated to implement
 * slower than greedy O(N^3)
 * may freeze or crash with large data
 *
 */
class HungarianAlgorithm @Inject constructor() : RoutingAlgorithm {

    /**
     * This algorithm finds the mathematically optimal assignment in O(N^3) time.
     * It works in 3 Phases:
     * 1. Setup (Flip Score to Cost)
     * 2. Pathfinding (Find a chain of swaps to seat a driver)
     * 3. Price Adjustment (If stuck, lower the "price" of shipments to create new options)
     */
    override fun computeAssignments(costMatrix: Array<DoubleArray>): IntArray {
        println("Hungarian Algorithm")

        val rows = costMatrix.size
        val cols = costMatrix[0].size
        val n = maxOf(rows, cols) // Make matrix square for the algorithm

        // =========================================================================
        // PHASE 1: THE SETUP
        // "We need to normalize the data. The algorithm minimizes cost, but we
        // want to MAXIMIZE score. So I flip the values: Cost = MaxPossible - Score."
        // =========================================================================

        val maxScore = costMatrix.maxOf { row -> row.maxOrNull() ?: 0.0 }
        val costs = Array(n) { DoubleArray(n) }

        for (i in 0 until rows) {
            for (j in 0 until cols) {
                costs[i][j] = maxScore - costMatrix[i][j]
            }
        }

        // =========================================================================
        // VARIABLE MAPPING (The "Business Analogy")
        // =========================================================================

        // "This is the SUBSIDY. It helps a specific Driver afford a shipment."
        val driverPotentials = DoubleArray(n + 1)

        // "This is the SURCHARGE/TAX. It discourages overcrowding at popular shipments."
        val shipmentPotentials = DoubleArray(n + 1)

        // "This is the CURRENT SCHEDULE. It tracks who is currently holding which shipment."
        val shipmentAssignments = IntArray(n + 1)

        // "This remembers 'Who kicked me out?' so we can retrace the swaps later."
        val predecessorShipment = IntArray(n + 1)


        // =========================================================================
        // PHASE 2: THE PATHFINDING LOOP
        // "We iterate through every driver. If their preferred spot is taken,
        // we start a 'Chain of Swaps' negotiation."
        // =========================================================================

        for (currentDriver in 1..n) {

            // Put the current driver in the "Waiting Room" (Index 0)
            shipmentAssignments[0] = currentDriver
            var currentShipment = 0

            // "Slack" = The cost to add a shipment to our current tree. Init with Infinity.
            val minSlack = DoubleArray(n + 1) { Double.MAX_VALUE }
            val visitedShipments = BooleanArray(n + 1)

            // The "Augmenting Path" Search (Similar to Dijkstra's Shortest Path)
            do {
                visitedShipments[currentShipment] = true
                val driverInCurrentSlot = shipmentAssignments[currentShipment]
                var smallestDelta = Double.MAX_VALUE
                var bestNextShipment = 0

                // Scan neighbors to find the cheapest move
                for (shipment in 1..n) {
                    if (!visitedShipments[shipment]) {

                        // CALCULATE REDUCED COST: RealCost - Subsidy - Tax
                        val cost = costs[driverInCurrentSlot - 1][shipment - 1] -
                                driverPotentials[driverInCurrentSlot] -
                                shipmentPotentials[shipment]

                        // Update our "Best Offer" for this shipment
                        if (cost < minSlack[shipment]) {
                            minSlack[shipment] = cost
                            predecessorShipment[shipment] = currentShipment // Drop a breadcrumb
                        }

                        // Track the absolute best option we found so far
                        if (minSlack[shipment] < smallestDelta) {
                            smallestDelta = minSlack[shipment]
                            bestNextShipment = shipment
                        }
                    }
                }

                // =========================================================================
                // PHASE 3: THE PRICE ADJUSTMENT (The "Delta")
                // "If we can't find a 0-cost move (blocked), we adjust the market prices.
                // We lower the cost of unvisited shipments just enough to open a path."
                // =========================================================================

                for (j in 0..n) {
                    if (visitedShipments[j]) {
                        // Increase Subsidy, Increase Tax (Balance the equation)
                        driverPotentials[shipmentAssignments[j]] += smallestDelta
                        shipmentPotentials[j] -= smallestDelta
                    } else {
                        // Reduce the barrier to entry for other shipments
                        minSlack[j] -= smallestDelta
                    }
                }

                // Move focus to the best shipment we found
                currentShipment = bestNextShipment

            } while (shipmentAssignments[currentShipment] != 0) // Repeat until we find an EMPTY spot


            // =========================================================================
            // PHASE 4: THE BACKTRACK
            // "We found an empty chair! Now we retrace our breadcrumbs and
            // execute the chain of swaps."
            // =========================================================================

            do {
                val previousShipment = predecessorShipment[currentShipment]
                shipmentAssignments[currentShipment] = shipmentAssignments[previousShipment]
                currentShipment = previousShipment
            } while (currentShipment != 0)
        }

        // Format result: Convert from "Shipment -> Driver" to "Driver -> Shipment" (0-based)
        val result = IntArray(n)
        for (j in 1..n) {
            if (shipmentAssignments[j] != 0) {
                result[shipmentAssignments[j] - 1] = j - 1
            }
        }
        return result
    }
}