package com.measify.kappmaker.data.source.local

import com.measify.kappmaker.domain.model.credit.CreditTransaction
import kotlinx.coroutines.flow.Flow

interface CreditTransactionLocalDataSource : LocalDataSource<String, CreditTransaction> {
    fun getRecentsFlow(limit: Int): Flow<List<CreditTransaction>>
    suspend fun getRecents(limit: Int): List<CreditTransaction>
}