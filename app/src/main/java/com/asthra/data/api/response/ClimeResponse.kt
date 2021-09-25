package com.asthra.data.api.response

class ClimeResponse(val status_code: Int, val message: String, val data: ClimeData)

class ClimeData(val mobile_number: String)


/* {
    "status_code": 200,
    "message": "data stored successfully",
    "data": {
        "_id": "60e5c275a3b13d2b0402e082",
        "mobile_number": "9988776610",
        "climed_petrol": 0,
        "climed_disel": 3,
        "total_petrol": 5.5,
        "total_disel": 17,
        "all_total_fuel": 22.5,
        "details": [
            {
                "vehicle_number": "TN12QW3232",
                "fuel_type": "petrol",
                "litter": 5.5,
                "date": "07-07-2021 15:04:21"
            },
            {
                "vehicle_number": "TN12QW3232",
                "fuel_type": "disel",
                "litter": 4.5,
                "date": "07-07-2021 15:04:38"
            }
        ],
        "updated_at": "2021-07-07 15:24:32",
        "created_at": "2021-07-07 15:04:21",
        "last_redeem_date": "2021-07-07 15:07:42",
        "redeem_value": 3
    }*/