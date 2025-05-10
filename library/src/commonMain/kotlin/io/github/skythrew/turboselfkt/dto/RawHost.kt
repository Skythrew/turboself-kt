package io.github.skythrew.turboselfkt.dto

import kotlinx.serialization.Serializable

@Serializable
data class RawHost(
    val id: Int,
    val idOrig: Int,
    val etab: RawEstablishment,
    val nom: String,
    val prenom: String,
    val mode: String,
    val qualite: String,
    val division: String,
    val prixDej: Int,
    val type: Short,
    val nbMulti: Int? = null,
    val droitPaiement: Boolean? = null,
    val droitReservation: Boolean? = null,
    val droitCafeteria: Boolean? = null,
    val urlCafeteria: String? = null,
    val dateDernSynchro: String,
    val desactive: Boolean,
    val mdpPrive: Boolean,
    val autoriseReservSoldeIns: Boolean,
    val profilForfaitModule: Int,
    val carteCodee: Int
)
