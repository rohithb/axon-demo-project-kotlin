package io.axon.kotlin.giftcardkotlin.command

import io.axon.kotlin.giftcardkotlin.coreapi.giftcard.CardReimbursedEvent
import io.axon.kotlin.giftcardkotlin.coreapi.giftcard.ReimburseCardCmd
import org.axonframework.commandhandling.CommandHandler
import org.axonframework.eventsourcing.EventSourcingHandler
import org.axonframework.modelling.command.AggregateLifecycle
import org.axonframework.modelling.command.EntityId
import java.lang.IllegalArgumentException
import java.util.*

class GiftCardTransaction(
    @EntityId val transactionId: UUID,
    val transactionVal: Int,
    var reimbursed: Boolean = false
) {

    @CommandHandler
    fun handle(cmd: ReimburseCardCmd) {
        if(reimbursed){
            throw IllegalArgumentException("Transaction already reimbursed")
        }
        AggregateLifecycle.apply(CardReimbursedEvent(cmd.cardId, transactionId, transactionVal))
    }

    @EventSourcingHandler
    fun on(event: CardReimbursedEvent){
        if( transactionId == event.transactionId ){
            reimbursed = true
        }
    }
}