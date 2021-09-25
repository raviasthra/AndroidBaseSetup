package com.asthra.data.api.response

class CustomerHistoryResponse(
    val status_code: Int,
    val message: String,
    val data: CustomerHistoryData
)

class CustomerHistoryData(
    val _id: String,
    val mobile_number: String,
    val climed_petrol: String,
    val climed_disel: Double,
    val total_petrol: Double,
    val total_disel: Double,
    val all_total_fuel: Int,
    val details: Details,
    val updated_at: String,
    val created_at: String,
    val last_redeem_date: String,
    val redeem_value: Int
)

class Details(
    val vehicle_number: String,
    val fuel_type: String,
    val litter: Double,
    val date: String
)
