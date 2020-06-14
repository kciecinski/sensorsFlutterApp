package com.example.wbudy_apka.model

class RangeDouble(var min: Double, var max: Double) {
    fun isInRangeInclusive(value: Double): Boolean {
        return ((min <= value) && (value <= max))
    }
}