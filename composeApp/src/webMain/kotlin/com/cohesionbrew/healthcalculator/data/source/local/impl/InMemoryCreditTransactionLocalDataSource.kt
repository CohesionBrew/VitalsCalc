package com.cohesionbrew.healthcalculator.data.source.local.impl

import com.cohesionbrew.healthcalculator.data.source.local.CreditTransactionLocalDataSource
import com.cohesionbrew.healthcalculator.domain.model.credit.CreditTransaction
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class InMemoryCreditTransactionLocalDataSource :
    BaseInMemoryLocalDataSource<String, CreditTransaction>({ it.id }),
    CreditTransactionLocalDataSource {

    override fun getRecentsFlow(limit: Int): Flow<List<CreditTransaction>> =
        getAllFlow().map { it.sortedByDescending { tx -> tx.createdAt }.take(limit) }


    override suspend fun getRecents(limit: Int): List<CreditTransaction> =
        getAll().sortedByDescending { it.createdAt }.take(limit)
}
