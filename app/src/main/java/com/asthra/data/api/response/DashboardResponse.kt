package com.asthra.data.api.response

class DashboardResponse(val status_code: Int, val message: String, val data: SettingData)

class SettingData(val _id: String, val petrol: Int, val disel:Int, val data_type: Int)