package io.axon.kotlin.giftcardkotlin.coreapi.giftcard

import org.axonframework.modelling.command.TargetAggregateIdentifier
import java.util.*

data class IssueCardCmd(
    @TargetAggregateIdentifier val cardId: UUID,
    val amount: Int
)

data class RedeemCardCmd(
    @TargetAggregateIdentifier val cardId: UUID,
    val transactionId: UUID,
    val amount: Int
)

data class ReimburseCardCmd(
    @TargetAggregateIdentifier val cardId: UUID,
    val transactionId: UUID
)