package io.axon.kotlin.giftcardkotlin.query.entity

import java.util.*
import javax.persistence.Entity
import javax.persistence.Id

@Entity
data class GiftCardSummary(
    @Id
    val id: UUID,
    var remainingValue: Int,
    val initialValue: Int
)