package io.github.skythrew.turboselfkt.models

import io.github.skythrew.turboselfkt.dto.RawHost
import kotlinx.serialization.Serializable

@Serializable
data class HostPerms(
    val payment: Boolean?,
    val reservation: Boolean?,
    val cafeteria: Boolean?,
    val bookWithNegativeBalance: Boolean?,
    val maxPassages: Int?
)

@Serializable
data class Host(
    val id: Int,
    val localId: Int,
    val etabId: Int?,
    val firstName: String,
    val lastName: String,
    val mode: String,
    val quality: String,
    val division: String,
    val lunchPrince: Int,
    val type: Short,
    val cardNumber: Int,
    val cafeteriaUrl: String?,
    val permissions: HostPerms
) {
    companion object {
        fun decodeFromRawHost(rawHost: RawHost): Host {
            return Host(
                rawHost.id,
                rawHost.idOrig,
                rawHost.etab.id,
                rawHost.prenom,
                rawHost.nom,
                rawHost.mode,
                rawHost.qualite,
                rawHost.division,
                rawHost.prixDej,
                rawHost.type,
                rawHost.carteCodee,
                rawHost.urlCafeteria,
                HostPerms(
                    payment = rawHost.droitPaiement ?: false,
                    reservation = rawHost.droitReservation ?: false,
                    cafeteria = rawHost.droitCafeteria ?: false,
                    bookWithNegativeBalance = rawHost.autoriseReservSoldeIns,
                    maxPassages = rawHost.nbMulti
                )
            )
        }
    }
}
