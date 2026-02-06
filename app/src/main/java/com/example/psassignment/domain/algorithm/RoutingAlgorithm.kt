package com.example.psassignment.domain.algorithm

interface RoutingAlgorithm {
    fun computeAssignments(costMatrix: Array<DoubleArray>): IntArray
}