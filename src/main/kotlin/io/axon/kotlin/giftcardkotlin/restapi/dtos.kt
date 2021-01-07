package io.axon.kotlin.giftcardkotlin.restapi

import java.util.*

data class IssueCardRequest(
    val amount: Int
)

data class RedeemRequest(
    val amount: Int
)

data class IssueCardResponse(
    val cardId: UUID
)

data class RedeemResponse(
    val transactionId: UUID
)

