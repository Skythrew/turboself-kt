package io.github.skythrew.turboselfkt.models

import io.github.skythrew.turboselfkt.dto.RawEstablishment
import io.github.skythrew.turboselfkt.dto.RawSSOConfiguration
import kotlinx.datetime.Instant
import kotlinx.serialization.Serializable

/**
 * Establishment location
 *
 * @property city Establishment city
 * @property address Establishment address
 * @property postcode Establishment city postcode
 */
@Serializable
data class Location(
    val city: String? = null,
    val address: String? = null,
    val postcode: String? = null
)

/**
 * Permissions allowed to users
 *
 * @property maxReservationsStudentMoney Maximum number of reservations per service for students in money mode
 * @property maxReservationsStudentPackage Maximum number of reservations per service for students in package mode
 * @property maxReservationsCommensalMoney Maximum number of reservations per service for commensals in money mode
 * @property maxReservationsCommensalPackage Maximum number of reservations per service for commensals in package mode
 * @property maxReservationsTraineeMoney Maximum number of reservations per service for trainees in money mode.
 * @property maxReservationsTraineePackage Maximum number of reservations per service for trainees in package mode
 * @property qrCodeStudent Right for the QR Code for students
 * @property qrCodeCommensal Right for the QR Code for commensals
 * @property qrCodeTrainee Right for the QR Code for trainees
 * @property hideHistory Should we hide user history on the home page ?
 */
@Serializable
data class Permissions(
    val maxReservationsStudentMoney: UShort? = null,
    val maxReservationsStudentPackage: UShort? = null,
    val maxReservationsCommensalMoney: UShort? = null,
    val maxReservationsCommensalPackage: UShort? = null,
    val maxReservationsTraineeMoney: UShort? = null,
    val maxReservationsTraineePackage: UShort? = null,
    val qrCodeStudent: Boolean? = null,
    val qrCodeCommensal: Boolean? = null,
    val qrCodeTrainee: Boolean? = null,
    val hideHistory: Boolean? = null
)

/**
 * Contact information of an establishment.
 *
 * @property phoneNumber Establishment phone number
 * @property faxNumber Establishment fax number
 * @property email Establishment email
 * @property website Establishment website
 */
@Serializable
data class Contact(
    val phoneNumber: String? = null,
    val faxNumber: String? = null,
    val email: String? = null,
    val website: String? = null
)

/**
 * Synchronisation information for an establishment
 *
 * @property firstSync First synchronisation date
 * @property lastSync Last synchronisation date
 */
@Serializable
data class Synchronisation(
    val firstSync: Instant?,
    val lastSync: Instant?
)

/**
 * Closure configuration
 *
 * @property id Configuration internal id
 * @property canBook Does the closure concern bookings ?
 * @property canPay Does the closure concern payments ?
 * @property from Closure start date
 * @property to Closure end date
 */
@Serializable
data class Closure(
    val id: UInt,
    val canBook: Boolean? = null,
    val canPay: Boolean? = null,
    val from: Instant? = null,
    val to: Instant? = null
)

@Serializable
data class Establishment(
    val id: Int,
    val name: String,
    val currencySymbol: String? = null,
    val code: Int? = null,
    val logoUrl: String? = null,
    val uai: String? = null,
    val macAddress: String? = null,
    val motd: String,
    val minMealsToCredit: Int? = null,
    val minDebtToCredit: Int? = null,
    val minAmoutToCredit: Int? = null,
    val disabled: Boolean? = null,
    val closures: List<Closure>,
    val location: Location,
    val contact: Contact,
    val permissions: Permissions,
    val sso: RawSSOConfiguration? = null,
    val synchronisation: Synchronisation
) {
    companion object {
        fun decodeFromRawEstablishment(rawEstablishment: RawEstablishment): Establishment {
            return Establishment(
                rawEstablishment.id ?: 0,
                rawEstablishment.nom ?: "N/A",
                rawEstablishment.currencySymbol,
                rawEstablishment.code2p5,
                rawEstablishment.logoUrl,
                rawEstablishment.numEtab,
                rawEstablishment.pcServeur,
                rawEstablishment.configuration?.msgAccueil ?: "",
                rawEstablishment.configuration?.nbRepasMini ?: 0,
                rawEstablishment.configuration?.creanceMini ?: 0,
                rawEstablishment.configuration?.montantCreditMini ?: 0,
                rawEstablishment.desactive ?: false,
                rawEstablishment.configuration?.fermetures?.map { rawClosure -> Closure(
                    rawClosure.id,
                    canBook = if (rawClosure.rsv != null) !rawClosure.rsv else null,
                    canPay = if (rawClosure.paiement != null) !rawClosure.paiement else null,
                    from = rawClosure.du,
                    to = rawClosure.au
                ) } ?: listOf(),
                location = Location(
                    city = rawEstablishment.ville,
                    address = rawEstablishment.adr1 + " " + rawEstablishment.adr2,
                    postcode = rawEstablishment.cp
                ),
                contact = Contact(
                    phoneNumber = rawEstablishment.tel,
                    faxNumber = rawEstablishment.fax,
                    email = rawEstablishment.configuration?.email,
                    website = rawEstablishment.configuration?.url
                ),
                permissions = Permissions(
                    maxReservationsStudentMoney = rawEstablishment.configurationSelf?.nbmultiElvArg,
                    maxReservationsStudentPackage = rawEstablishment.configurationSelf?.nbmultiElvFor,
                    maxReservationsCommensalMoney = rawEstablishment.configurationSelf?.nbmultiComArg,
                    maxReservationsCommensalPackage = rawEstablishment.configurationSelf?.nbmultiComFor,
                    maxReservationsTraineeMoney = rawEstablishment.configurationSelf?.nbmultiStgArg,
                    maxReservationsTraineePackage = rawEstablishment.configurationSelf?.nbmultiStgFor,
                    qrCodeStudent = rawEstablishment.configuration?.autoriseQrCodeEleve,
                    qrCodeCommensal = rawEstablishment.configuration?.autoriseQrCodeCommensal,
                    qrCodeTrainee = rawEstablishment.configuration?.autoriseQrCodeStagiaire,
                    hideHistory = rawEstablishment.configuration?.cacherHistorique
                ),
                sso = rawEstablishment.configuration?.sso,
                synchronisation = Synchronisation(
                    firstSync = rawEstablishment.datePremSynchro,
                    lastSync = rawEstablishment.dateDernSynchro
                )
            )
        }
    }
}