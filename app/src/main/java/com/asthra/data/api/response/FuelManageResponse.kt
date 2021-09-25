package com.asthra.data.api.response

class FuelManageResponse(
    val status_code: Int,
    val message: String,
    val data: ArrayList<FuelManageData>
)

class FuelManageData(val id: Int, val name: String, val status: Int)
