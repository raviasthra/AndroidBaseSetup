package com.asthra.data.api.response

class RedeemHistoryResponse(val status_code: Int, val message: String, val data: RedeemHistoryList)

class RedeemHistoryList(val TotalCount: Int, val Records: ArrayList<HistoryRecords>)

class HistoryRecords(
    val mobile_number: String,
    val fuel_type: Int,
    val total_liters: String,
    val created_at: String,
    val points: String
)