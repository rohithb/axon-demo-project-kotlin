package io.axon.kotlin.giftcardkotlin.query.projector

import io.axon.kotlin.giftcardkotlin.coreapi.giftcard.CardIssuedEvent
import io.axon.kotlin.giftcardkotlin.coreapi.giftcard.CardRedeemedEvent
import io.axon.kotlin.giftcardkotlin.coreapi.giftcard.CardReimbursedEvent
import io.axon.kotlin.giftcardkotlin.coreapi.giftcard.GiftCardSummaryQuery
import io.axon.kotlin.giftcardkotlin.query.entity.GiftCardSummary
import io.axon.kotlin.giftcardkotlin.query.repository.GiftCardSummaryRepository
import lombok.extern.slf4j.Slf4j
import org.axonframework.eventhandling.EventHandler
import org.axonframework.queryhandling.QueryHandler
import org.springframework.stereotype.Component
import java.util.*

@Component
class SummaryProjector(
    val repo: GiftCardSummaryRepository
) {

    @EventHandler
    fun on(event: CardIssuedEvent) {
        repo.save(GiftCardSummary(event.cardId, event.amount, event.amount))
    }

    @EventHandler
    fun on(event: CardRedeemedEvent){
        repo.findById(event.cardId).ifPresent {
            summary: GiftCardSummary -> summary.remainingValue = summary.remainingValue - event.amount
        }
    }

    @EventHandler
    fun on(event: CardReimbursedEvent){
        repo.findById(event.cardId).ifPresent {
                summary: GiftCardSummary -> summary.remainingValue = summary.remainingValue + event.amount
        }
    }

    @QueryHandler
    fun handle(query: GiftCardSummaryQuery): Optional<GiftCardSummary> {
        return repo.findById(query.id)
    }
}