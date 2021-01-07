package com.example.giftcard.command

import io.axon.kotlin.giftcardkotlin.command.GiftCard
import io.axon.kotlin.giftcardkotlin.coreapi.giftcard.CardIssuedEvent
import io.axon.kotlin.giftcardkotlin.coreapi.giftcard.CardRedeemedEvent
import io.axon.kotlin.giftcardkotlin.coreapi.giftcard.IssueCardCmd
import io.axon.kotlin.giftcardkotlin.coreapi.giftcard.RedeemCardCmd
import org.axonframework.test.aggregate.AggregateTestFixture
import org.axonframework.test.aggregate.FixtureConfiguration
import org.axonframework.test.aggregate.ResultValidator
import org.axonframework.test.aggregate.TestExecutor
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.lang.IllegalArgumentException
import java.util.*

internal class GiftCardTest {
    private lateinit var fixture: FixtureConfiguration<GiftCard>
    private lateinit var cardId: UUID
    private lateinit var txnId: UUID
    @BeforeEach
    fun setup() {
        fixture = AggregateTestFixture(GiftCard::class.java)
        cardId = UUID.randomUUID()
        txnId = UUID.randomUUID()
    }

    @Test
    fun shouldIssueGiftCard() {
        val cardId = UUID.randomUUID()
        fixture.givenNoPriorActivity()
            .WHEN(IssueCardCmd(cardId, 100))
            .expectEvents(CardIssuedEvent(cardId, 100))
    }

    @Test
    fun shouldRedeemGiftCard() {
        fixture.GIVEN(CardIssuedEvent(cardId, 100))
            .WHEN(RedeemCardCmd(cardId, txnId, 20))
            .expectEvents(CardRedeemedEvent(cardId, txnId, 20))
    }

    @Test
    fun shouldNotRedeemWithNegativeAmount() {
        fixture.GIVEN(CardIssuedEvent(cardId, 100))
            .WHEN(RedeemCardCmd(cardId, txnId, -10))
            .expectException(IllegalArgumentException::class.java)
    }

    @Test
    fun shouldNotRedeemWhenThereIsNotEnoughMoney() {
        fixture.GIVEN(CardIssuedEvent(cardId, 100))
            .WHEN(RedeemCardCmd(cardId, txnId, 110))
            .expectException(IllegalArgumentException::class.java)
    }
}

fun <T> FixtureConfiguration<T>.GIVEN(vararg event: Any): TestExecutor<T> = this.given(*event)
fun <T> TestExecutor<T>.WHEN(command: Any): ResultValidator<T> = this.`when`(command)