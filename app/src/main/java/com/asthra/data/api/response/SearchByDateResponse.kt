package com.asthra.data.api.response

class SearchByDateResponse(
    val status_code: Int,
    val message: String,
    val data: SearchByDateData
)

class SearchByDateData(
    val _id: String,
    val date: String,
    val total_fuel: Double,
    val petrol: Double,
    val disel: Double,
    val details: SearchByDateDetails,
    val updated_at: String,
    val created_at: String,
    val redeem_value: String
)

class SearchByDateDetails(
    val mobile_number: String,
    val vehicle_number: String,
    val fuel_type: String,
    val litter: Double,
    val date: String
)
