package com.asthra.data.api.response

class RedeemListResponse(val status_code: Int, val message: String, val data: RedeemList)

class RedeemList(val TotalCount: String, val Records: ArrayList<Records>)

class Records(
    val mobile_number: String,
    var total_petrol_ltr: String,
    var total_diesel_ltr: String,
    var total_redeemed_petrol_ltr: String,
    var total_redeemed_diesel_ltr: String,
    var total_points: String
)