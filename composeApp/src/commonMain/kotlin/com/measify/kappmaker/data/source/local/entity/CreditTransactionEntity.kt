@file:OptIn(ExperimentalTime::class, ExperimentalUuidApi::class)

package com.measify.kappmaker.data.source.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.measify.kappmaker.domain.model.credit.CreditTransaction
import kotlin.time.Clock
import kotlin.time.ExperimentalTime
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid


@Entity(
    tableName = "credit_transaction",
    indices = [Index(value = ["created_at"])]
)
data class CreditTransactionEntity(
    @PrimaryKey @ColumnInfo(name = "id") val id: String = Uuid.random().toString(),
    @ColumnInfo(name = "type") val type: CreditTransaction.Type,
    @ColumnInfo(name = "amount") val amount: Int,
    @ColumnInfo(name = "description") val description: String? = null,
    @ColumnInfo("created_at") val createdAt: Long = Clock.System.now().toEpochMilliseconds(),
) {

    companion object {

        fun of(model: CreditTransaction): CreditTransactionEntity {
            return CreditTransactionEntity(
                id = model.id,
                type = model.type,
                amount = model.amount,
                description = model.description,
                createdAt = model.createdAt,
            )
        }
    }

    fun asDomain(): CreditTransaction {
        return CreditTransaction(
            id = id,
            type = type,
            amount = amount,
            description = description,
            createdAt = createdAt,
        )
    }
}
