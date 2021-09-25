package com.asthra.data.api.response

data class RedeemRequest(
    val mobile_number: String,
    val fuel_type: Int,
    val litters: Float,
    val points: String
)