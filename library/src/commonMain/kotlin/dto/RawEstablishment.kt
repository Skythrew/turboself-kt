package dto

import kotlinx.datetime.Instant
import kotlinx.serialization.Serializable

@Serializable
data class RawClosure(
    val id: UInt,
    val rsv: Boolean? = null,
    val paiement: Boolean? = null,
    val du: Instant? = null,
    val au: Instant? = null,
    val synchro: Boolean? = null
)

@Serializable
data class RawSSOConfiguration(
    val id: UInt? = null,
    val entCode: String? = null,
    val entName: String? = null,
    val serveurCas: String? = null,
    val service: String? = null
)

@Serializable
data class RawEstablishmentSelfConfiguration(
    val id: UInt? = null,
    val nbmultiElvArg: UShort? = null,
    val nbmultiElvFor: UShort? = null,
    val nbmultiComArg: UShort? = null,
    val nbmultiComFor: UShort? = null,
    val nbmultiStgArg: UShort? = null,
    val nbmultiStgFor: UShort? = null,
    val dateDernSynchro: Instant? = null,
    val nbrepasnegElv: UShort? = null,
    val nbrepasnegCom: UShort? = null,
    val nbrepasnegStg: UShort? = null
)

@Serializable
data class RawEstablishmentConfiguration(
    val id: Int? = null,
    val url: String? = null,
    val emailCreationCompte: String? = null,
    val email: String? = null,
    val nbRepasMini: Int? = null,
    val creanceMini: Int? = null,
    val montantCreditMini: Int? = null,
    val nbSemaineReserv: Int? = null,
    val jPlusN: Int? = null,
    val msgAccueil: String? = null,
    val sso: RawSSOConfiguration? = null,
    val autoriseQrCodeCommensal: Boolean? = null,
    val autoriseQrCodeEleve: Boolean? = null,
    val autoriseQrCodeStagiaire: Boolean? = null,
    val cacherHistorique: Boolean? = null,
    val fermetures: List<RawClosure>? = null
)

@Serializable
data class RawEstablishmentBookingsConfiguration(
    val id: UInt? = null,
    val usage: UInt? = null,
    val elecom: UInt? = null,
    val finReserv: String? = null
)

@Serializable
data class RawEstablishment(
    val id: Int? = null,
    val adr1: String? = null,
    val adr2: String? = null,
    val cp: String? = null,
    val ville: String? = null,
    val tel: String? = null,
    val fax: String? = null,
    val logoUrl: String? = null,
    val datePremSynchro: Instant? = null,
    val dateDernSynchro: Instant? = null,
    val ipTeamBox: String? = null,
    val currencySymbol: String? = null,
    val configuration: RawEstablishmentConfiguration? = null,
    val configurationReservation: RawEstablishmentBookingsConfiguration? = null,
    val configurationSelf: RawEstablishmentSelfConfiguration? = null,
    val numEtab: String? = null,
    val desactive: Boolean? = null,
    val pcServeur: String? = null,
    val nbTransactionAutorise: Int? = null,
    val nbReservationsTotal: Int? = null,
    val code2p5: Int? = null,
    val nom: String? = null,
    val versionTS: String? = null
)
