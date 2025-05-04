package core

const val BASE_URL = "https://api-rest-prod.incb.fr/api"

// Authentication endpoints
const val AUTH_LOGIN = "$BASE_URL/v1/auth/login"

// "Host" endpoints
const val HOST = "$BASE_URL/v1/hotes/:hostId:"
const val HOST_BALANCES = "$BASE_URL/v1/comptes/hotes/:hostId:/0"
const val HOST_HOME = "$BASE_URL/v2/hotes/:hostId:/accueil"
const val HOST_HISTORY = "$BASE_URL/v1/historiques/hotes/:hostId:"
const val HOST_PAYMENT = "$BASE_URL/v1/paiements-payline/"

const val ESTABLISHMENT_BY_CODE = "$BASE_URL/v1/etablissements?code2p5="
const val ESTABLISHMENT_BY_ID = "$BASE_URL/v1/etablissements/etabId/"