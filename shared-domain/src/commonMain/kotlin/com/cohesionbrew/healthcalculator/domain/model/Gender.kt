package com.cohesionbrew.healthcalculator.domain.model

enum class Gender(val key: String) {
    MALE("male"),
    FEMALE("female");

    companion object {
        fun fromKey(key: String): Gender {
            return entries.find { it.key.equals(key, ignoreCase = true) } ?: MALE
        }
    }
}
