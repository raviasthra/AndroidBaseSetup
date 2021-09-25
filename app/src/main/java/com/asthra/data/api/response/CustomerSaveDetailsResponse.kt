package com.asthra.data.api.response

class CustomerSaveDetailsResponse(
    val status_code: Int, val message: String,
    val data: ArrayList<CustomerSaveData>
)

class CustomerSaveData(
    val mobile_number: String,
    val total_petrol_ltr: String,
    val total_diesel_ltr: String,
    val total_redeemed_petrol_ltr: String,
    val total_redeemed_diesel_ltr: String,
    val total_points: String
)
