package com.asthra.data.api.response

data class CustomerListResponse(val status_code: Int, val message: String, val data: HistoryData)

data class HistoryData(val TotalCount: Int, val Records: ArrayList<HistoryList>)

data class HistoryList(
    val mobile_number: String, val fuel_type: Int, val total_liters: String,
    val vehicle_number: String, val created_at: String
)