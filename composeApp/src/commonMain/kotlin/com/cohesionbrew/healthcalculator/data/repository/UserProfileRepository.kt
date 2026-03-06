package com.cohesionbrew.healthcalculator.data.repository

import com.cohesionbrew.healthcalculator.domain.model.UserProfile
import kotlinx.coroutines.flow.Flow

interface UserProfileRepository {
    fun getProfileFlow(id: String = "default"): Flow<UserProfile?>
    suspend fun getProfile(id: String = "default"): UserProfile?
    suspend fun saveProfile(profile: UserProfile)
    suspend fun deleteProfile(id: String = "default")
}
