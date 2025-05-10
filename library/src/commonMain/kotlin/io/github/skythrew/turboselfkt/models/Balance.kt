package io.github.skythrew.turboselfkt.models

import io.github.skythrew.turboselfkt.dto.RawBalance
import kotlinx.datetime.Instant
import kotlinx.serialization.Serializable

/**
 * Account balance
 *
 * @property id Balance id
 * @property amount Balance amount (can be negative ?)
 * @property estimatedAmountLabel Estimated balance label
 * @property estimatedAmount Estimated amount taking into account user bookings
 * @property estimatedDate Estimated balance date
 */
@Serializable
data class Balance(
    val id: UInt,
    val amount: Int,
    val estimatedAmountLabel: String,
    val estimatedAmount: Int,
    val estimatedDate: Instant
) {
    companion object {
        fun decodeFromRawBalance(rawBalance: RawBalance): Balance {
            val regex = """.*(\d{2})/(\d{2})/(\d{4})""".toRegex()
            val (_, day, month, year) =  regex.matchEntire(rawBalance.montantEstimeMsg)!!.groupValues

            val date = Instant.parse("$year-$month-${day}T00:00:00Z")

            return Balance(
                rawBalance.id,
                rawBalance.montant,
                rawBalance.montantEstimeMsg,
                rawBalance.montantEstime,
                date
            )
        }
    }
}
