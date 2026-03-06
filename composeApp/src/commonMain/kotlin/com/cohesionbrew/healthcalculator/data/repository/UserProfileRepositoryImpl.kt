package com.cohesionbrew.healthcalculator.data.repository

import com.cohesionbrew.healthcalculator.data.source.local.UserProfileLocalDataSource
import com.cohesionbrew.healthcalculator.domain.model.UserProfile
import kotlinx.coroutines.flow.Flow

class UserProfileRepositoryImpl(
    private val localDataSource: UserProfileLocalDataSource
) : UserProfileRepository {

    override fun getProfileFlow(id: String): Flow<UserProfile?> {
        return localDataSource.getByIdFlow(id)
    }

    override suspend fun getProfile(id: String): UserProfile? {
        return localDataSource.getById(id)
    }

    override suspend fun saveProfile(profile: UserProfile) {
        localDataSource.upsert(profile)
    }

    override suspend fun deleteProfile(id: String) {
        localDataSource.deleteById(id)
    }
}
