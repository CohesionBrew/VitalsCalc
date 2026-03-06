package com.cohesionbrew.healthcalculator.data.source.local

import com.cohesionbrew.healthcalculator.domain.model.credit.CreditTransaction
import kotlinx.coroutines.flow.Flow

interface CreditTransactionLocalDataSource : LocalDataSource<String, CreditTransaction> {
    fun getRecentsFlow(limit: Int): Flow<List<CreditTransaction>>
    suspend fun getRecents(limit: Int): List<CreditTransaction>
}