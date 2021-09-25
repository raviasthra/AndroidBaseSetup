package com.asthra.data.api.request

class ChangePasswordRequest(
    val id: Int,
    val old_password: String,
    val new_password: String,
    val confirm_password: String
)