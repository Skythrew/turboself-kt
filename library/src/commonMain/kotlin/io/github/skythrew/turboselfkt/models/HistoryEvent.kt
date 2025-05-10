package io.github.skythrew.turboselfkt.models

import io.github.skythrew.turboselfkt.dto.RawHistoryEvent
import kotlinx.datetime.Instant
import kotlinx.serialization.Serializable

/**
 * A Turboself account history event (usually associated with a booking
 * or a payment)
 *
 * @property id History event id
 * @property date History event date
 * @property label History event label
 * @property amount History event amount
 */
@Serializable
data class HistoryEvent(
    val id: UInt,
    val date: Instant,
    val label: String,
    val amount: Int
) {
    companion object {
        fun decodeFromRawHistoryEvent(rawHistoryEvent: RawHistoryEvent): HistoryEvent {
            return HistoryEvent(
                rawHistoryEvent.id,
                rawHistoryEvent.date,
                rawHistoryEvent.detail,
                (rawHistoryEvent.credit ?: 0) - (rawHistoryEvent.debit ?: 0)
            )
        }

        fun decodeRawHistory(rawHistory: List<RawHistoryEvent>): List<HistoryEvent> {
            return rawHistory.map { rawHistoryEvent -> decodeFromRawHistoryEvent(rawHistoryEvent) }
        }
    }
}
