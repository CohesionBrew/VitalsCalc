package com.measify.kappmaker.data.source.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import com.measify.kappmaker.data.source.local.entity.CreditTransactionEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface CreditTransactionDao {
    @Query("SELECT * FROM credit_transaction WHERE id = :id")
    suspend fun getById(id: String): CreditTransactionEntity?

    @Query("SELECT * FROM credit_transaction WHERE id = :id")
    fun getByIdFlow(id: String): Flow<CreditTransactionEntity?>

    @Query("SELECT * FROM credit_transaction ORDER BY created_at DESC")
    fun getAllFlow(): Flow<List<CreditTransactionEntity>>

    @Query("SELECT * FROM credit_transaction")
    suspend fun getAll(): List<CreditTransactionEntity>

    @Query("SELECT * FROM credit_transaction ORDER BY created_at DESC LIMIT :limit")
    fun getRecentsFlow(limit: Int): Flow<List<CreditTransactionEntity>>

    @Query("SELECT * FROM credit_transaction ORDER BY created_at DESC LIMIT :limit")
    suspend fun getRecents(limit: Int): List<CreditTransactionEntity>

    //Insert or update if exists
    @Upsert
    suspend fun upsert(entity: CreditTransactionEntity)

    @Query("DELETE FROM credit_transaction WHERE id = :id")
    suspend fun deleteById(id: String)

    @Delete
    suspend fun delete(entity: CreditTransactionEntity)

    @Query("DELETE FROM credit_transaction")
    suspend fun deleteAll()
}