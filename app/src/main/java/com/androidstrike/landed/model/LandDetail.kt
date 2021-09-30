package com.androidstrike.landed.model

data class LandDetail(
    var name: String = "",
    var area: String = "",
    var location: String = "",
    var price: Int = 0,
    var agent: String = "",
    var status: String = "",
    var urp: String = "",
    var available: Boolean? = null,
    var owner_email: String = "",
    var owner_name: String = "",
    var owner_uid: String = "",
    var purchase_time: String = ""

)