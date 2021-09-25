package com.asthra.data.api.request

class CustomerSaveDetailsRequest(
    val mobile_number: String, val vehicle_number: String,
    val litters: String, val fuel_type: Int
)