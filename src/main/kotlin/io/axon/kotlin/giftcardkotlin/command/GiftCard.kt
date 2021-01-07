package io.axon.kotlin.giftcardkotlin.command

import io.axon.kotlin.giftcardkotlin.coreapi.giftcard.*
import org.axonframework.commandhandling.CommandHandler
import org.axonframework.eventsourcing.EventSourcingHandler
import org.axonframework.modelling.command.AggregateIdentifier
import org.axonframework.modelling.command.AggregateLifecycle
import org.axonframework.modelling.command.AggregateMember
import org.axonframework.spring.stereotype.Aggregate
import java.util.*
import kotlin.collections.ArrayList
import kotlin.properties.Delegates

@Aggregate
internal class GiftCard {
    // https://kotlinlang.org/docs/reference/properties.html#late-initialized-properties-and-variables
    @AggregateIdentifier
    private lateinit var id: UUID
    private var remainingValue by Delegates.notNull<Int>()
    @AggregateMember
    private lateinit var transactions: ArrayList<GiftCardTransaction>

    constructor()

    @CommandHandler
    constructor(cmd: IssueCardCmd) {
        require(cmd.amount > 0) { "Amount <=0" }
        AggregateLifecycle.apply(CardIssuedEvent(cmd.cardId, cmd.amount))
    }

    @CommandHandler
    fun handle(cmd: RedeemCardCmd) {
        require(cmd.amount > 0) { "Amount <=0" }
        require(cmd.amount <= remainingValue) { "amount > remaining" }
        check(
            !transactions.stream().map<Any>(GiftCardTransaction::transactionId)
                .anyMatch(cmd.transactionId::equals)
        ) { "TransactionId must be unique" }
        AggregateLifecycle.apply(CardRedeemedEvent(cmd.cardId, cmd.transactionId, cmd.amount))
    }

    @EventSourcingHandler
    fun on(event: CardIssuedEvent) {
        id = event.cardId
        remainingValue = event.amount
        transactions = ArrayList()
    }

    @EventSourcingHandler
    fun on(event: CardRedeemedEvent) {
        remainingValue -= event.amount
        transactions.add(GiftCardTransaction(event.transactionId, event.amount))
    }

    @EventSourcingHandler
    fun on(event: CardReimbursedEvent) {
        remainingValue += event.amount
    }
}