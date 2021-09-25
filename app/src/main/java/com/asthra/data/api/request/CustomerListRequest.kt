package com.asthra.data.api.request

data class CustomerListRequest(
    val mobile_number: String,
    val start_date: String,
    val end_date: String,
    val page: Int
)

