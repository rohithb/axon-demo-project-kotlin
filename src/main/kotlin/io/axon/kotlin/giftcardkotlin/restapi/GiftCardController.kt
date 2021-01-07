package io.axon.kotlin.giftcardkotlin.restapi

import io.axon.kotlin.giftcardkotlin.coreapi.giftcard.GiftCardSummaryQuery
import io.axon.kotlin.giftcardkotlin.coreapi.giftcard.IssueCardCmd
import io.axon.kotlin.giftcardkotlin.coreapi.giftcard.RedeemCardCmd
import io.axon.kotlin.giftcardkotlin.coreapi.giftcard.ReimburseCardCmd
import io.axon.kotlin.giftcardkotlin.query.entity.GiftCardSummary
import org.axonframework.commandhandling.gateway.CommandGateway
import org.axonframework.messaging.responsetypes.ResponseTypes
import org.axonframework.queryhandling.QueryGateway
import org.springframework.web.bind.annotation.*
import java.util.*
import java.util.concurrent.CompletableFuture

@RestController
class GiftCardController(
    private val commandGateway: CommandGateway,
    private val queryGateway: QueryGateway
) {

    @PostMapping("/card")
    fun issueCard(@RequestBody request: IssueCardRequest): IssueCardResponse {
        // TODO: Request body validation
        val id = UUID.randomUUID()
        commandGateway.sendAndWait<Any>(IssueCardCmd(id, request.amount))
        return IssueCardResponse(id)
    }


    @PostMapping("/card/{cardId}/redeem")
    fun redeem(@PathVariable cardId: UUID, @RequestBody request: RedeemRequest): RedeemResponse {
        val transactionID = UUID.randomUUID()
        // TODO: check error handling
        commandGateway.sendAndWait<Any>(RedeemCardCmd(cardId, transactionID, request.amount))
        return RedeemResponse(transactionID)
    }

    @PostMapping("/card/{cardId}/reimburse/{txnId}")
    fun reimburse(@PathVariable cardId: UUID, @PathVariable txnId: UUID) {
        commandGateway.sendAndWait<Any>(ReimburseCardCmd(cardId, txnId))
    }

    @GetMapping("/card/{cardId}")
    fun getGiftCardSummary(@PathVariable cardId: UUID): CompletableFuture<GiftCardSummary?>? {
        return queryGateway.query(
            GiftCardSummaryQuery(cardId),
            ResponseTypes.instanceOf(GiftCardSummary::class.java)
        )
    }
}