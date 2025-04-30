package models

import dto.RawBalance
import kotlinx.datetime.Instant
import kotlinx.serialization.Serializable

/**
 * Account balance
 *
 * @property id Balance id
 * @property label Balance label
 * @property amount Balance amount (can be negative ?)
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
            val (whole, day, month, year) =  regex.matchEntire(rawBalance.montantEstimeMsg)!!.groupValues

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
