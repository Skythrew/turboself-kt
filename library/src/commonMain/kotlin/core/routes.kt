package core

const val BASE_URL = "https://api-rest-prod.incb.fr/api"

// Authentication endpoints
const val AUTH_LOGIN = "$BASE_URL/v1/auth/login"

// "Host" endpoints
const val HOST = "$BASE_URL/v1/hotes/:hostId:"
const val HOST_BOOK_EVENING = "$BASE_URL/v1/hotes/:hostId:/resa-soir"
const val HOST_BALANCES = "$BASE_URL/v1/comptes/hotes/:hostId:/0"
const val HOST_HOME="$BASE_URL/v2/hotes/:hostId:/accueil"
const val HOST_HISTORY="$BASE_URL/v1/historiques/hotes/:hostId:"