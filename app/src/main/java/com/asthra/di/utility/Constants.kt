package com.asthra.di.utility

object Pref {
    const val SHARED_PREFERENCES = "asthra_shared_pref"
    const val TOKEN = "Access-token"
    const val VERIFY_STATUS = "verify_status"
    const val IS_LOGGED_IN = "isloggedin"
    const val USER_ID = "user_id"
    const val USER_NAME = "user_name"
    const val USER_EMAIL = "user_email"
}

object ApiUrls {
    const val AUTHORIZATION = "service/auth"
    const val GET_CORE = "getcoreconfig"

}

object Headers {
    const val AUTHORIZATION = "service/auth"
    const val X_TIGNUM_QOT_BUNDLE_IDENTIFIER = "X-Tignum-Qot-Bundle-Identifier"
}

object ImageConstants {
    const val GALLERY = 1
    const val CAMERA = 2
    const val PROFILE_PIC = "ProfilePic"
    const val IMAGE_DIRECTORY = "Proofs"
}
