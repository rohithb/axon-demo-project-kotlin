package io.axon.kotlin.giftcardkotlin.query.repository

import io.axon.kotlin.giftcardkotlin.query.entity.GiftCardSummary
import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

interface GiftCardSummaryRepository: JpaRepository<GiftCardSummary, UUID>