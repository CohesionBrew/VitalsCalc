package com.cohesionbrew.healthcalculator.data.source.local.impl

import com.cohesionbrew.healthcalculator.data.source.local.CreditTransactionLocalDataSource
import com.cohesionbrew.healthcalculator.data.source.local.dao.CreditTransactionDao
import com.cohesionbrew.healthcalculator.data.source.local.entity.CreditTransactionEntity
import com.cohesionbrew.healthcalculator.data.source.local.entity.CreditTransactionEntityMapper
import com.cohesionbrew.healthcalculator.domain.model.credit.CreditTransaction
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class CreditTransactionLocalDataSourceImpl(
    private val creditTransactionDao: CreditTransactionDao,
    private val creditTransactionEntityMapper: CreditTransactionEntityMapper = CreditTransactionEntityMapper()
) : BaseRoomLocalDataSource<String, CreditTransactionEntity, CreditTransaction>(
    dao = creditTransactionDao,
    mapper = creditTransactionEntityMapper
), CreditTransactionLocalDataSource {

    override fun getRecentsFlow(limit: Int): Flow<List<CreditTransaction>> =
        creditTransactionDao.getRecentsFlow(limit)
            .map { entities -> entities.map(creditTransactionEntityMapper::toModel) }


    override suspend fun getRecents(limit: Int): List<CreditTransaction> =
        creditTransactionDao.getRecents(limit)
            .map(creditTransactionEntityMapper::toModel)

}