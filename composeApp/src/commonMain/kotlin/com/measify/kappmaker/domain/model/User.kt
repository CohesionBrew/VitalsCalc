package com.measify.kappmaker.domain.model

data class User(
    val id: String = "",
    val email: String? = null,
    val hasPremiumAccess: Boolean = false
)
