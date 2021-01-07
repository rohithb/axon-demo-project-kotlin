package io.axon.kotlin.giftcardkotlin.coreapi.giftcard

import java.util.*

data class CardIssuedEvent(
    val cardId: UUID,
    val amount: Int
)

data class CardRedeemedEvent(
    val cardId: UUID,
    val transactionId: UUID,
    val amount: Int
)

data class CardReimbursedEvent(
    val cardId: UUID,
    val transactionId: UUID,
    val amount: Int
)

