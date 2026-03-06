package com.cohesionbrew.healthcalculator.data.source.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import com.cohesionbrew.healthcalculator.data.source.local.entity.UserProfileEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface UserProfileDao : BaseRoomDao<String, UserProfileEntity> {
    @Query("SELECT * FROM user_profile WHERE id = :id")
    override suspend fun getById(id: String): UserProfileEntity?

    @Query("SELECT * FROM user_profile WHERE id = :id")
    override fun getByIdFlow(id: String): Flow<UserProfileEntity?>

    @Query("SELECT * FROM user_profile")
    override fun getAllFlow(): Flow<List<UserProfileEntity>>

    @Query("SELECT * FROM user_profile")
    override suspend fun getAll(): List<UserProfileEntity>

    @Upsert
    override suspend fun upsert(entity: UserProfileEntity)

    @Query("DELETE FROM user_profile WHERE id = :id")
    override suspend fun deleteById(id: String)

    @Delete
    override suspend fun delete(entity: UserProfileEntity)

    @Query("DELETE FROM user_profile")
    override suspend fun deleteAll()
}
