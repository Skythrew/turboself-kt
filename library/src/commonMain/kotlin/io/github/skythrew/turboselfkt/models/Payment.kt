package io.github.skythrew.turboselfkt.models

import io.github.skythrew.turboselfkt.dto.RawPayment
import kotlinx.datetime.Instant
import kotlinx.serialization.Serializable

/**
 * A Turboself payment.
 *
 * @property id Payment id
 * @property hostId Host ID of the buyer
 * @property amount Payment amount
 * @property status Payment status
 * @property token Payment token
 * @property url Payment page URL
 * @property cancelUrl URL to redirect to if the payment is cancelled
 * @property returnUrl URL to redirect to if the payment is successful
 * @property date Payment date
 */
@Serializable
data class Payment(
    val id: UInt?,
    val hostId: UInt,
    val amount: UInt,
    val status: String,
    val token: String,
    val url: String?,
    val cancelUrl: String,
    val returnUrl: String,
    val date: Instant
) {
    companion object {
        fun decodeFromRawPayment(hostId: UInt, rawPayment: RawPayment): Payment {
            return Payment(
                rawPayment.id,
                hostId,
                rawPayment.montant,
                rawPayment.statut,
                rawPayment.token,
                null,
                "https://espacenumerique.turbo-self.com/PagePaiementRefuse.aspx?token=${rawPayment.token}",
                "https://espacenumerique.turbo-self.com/PagePaiementValide.aspx?token=${rawPayment.token}",
                rawPayment.date
            )
        }
    }
}
