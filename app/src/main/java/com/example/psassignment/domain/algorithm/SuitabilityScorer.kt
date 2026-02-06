package com.example.psassignment.domain.algorithm

import com.example.psassignment.domain.model.Driver
import com.example.psassignment.domain.model.Shipment
import javax.inject.Inject

/**
 * Calculates the Suitability Score (SS) between a Driver and a Shipment
 * based on the proprietary ruleset.
 */
class SuitabilityScorer @Inject constructor() {

    fun calculateScore(driver: Driver, shipment: Shipment): Double {
        val streetName = parseStreetName(shipment.address)
        val driverName = driver.name

        var baseScore = 0.0

        // determine odd or even number
        if (streetName.length % 2 == 0) {
            // Rule: Even length -> Vowels * 1.5
            baseScore = countVowels(driverName) * 1.5
        } else {
            // Rule: Odd length -> Consonants * 1
            baseScore = countConsonants(driverName).toDouble()
        }

        // Rule: Common factors -> Increase by 50%
        if (hasCommonFactor(streetName.length, driverName.length)) {
            baseScore *= 1.5
        }

        return baseScore
    }

    /**
     * Extracts the street name from an address string.
     */
    private fun parseStreetName(address: String): String {
        val parts = address.split(" ", limit = 2)
        return if (parts.size > 1) parts[1] else address
    }

    private fun countVowels(name: String): Int {
        return name.count { it.lowercaseChar() in VOWELS }
    }

    private fun countConsonants(name: String): Int {
        return name.count { char ->
            char.isLetter() && char.lowercaseChar() !in VOWELS
        }
    }

    /**
     * Checks if two numbers share any common factor > 1.
     */
    private fun hasCommonFactor(a: Int, b: Int): Boolean {
        return gcd(a, b) > 1
    }

    private tailrec fun gcd(a: Int, b: Int): Int {
        return if (b == 0) a else gcd(b, a % b)
    }

    companion object {
        private val VOWELS = setOf('a', 'e', 'i', 'o', 'u')
    }
}