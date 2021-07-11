package com.pnhung.icarchecking.view.api.model.entities

import com.google.gson.annotations.SerializedName
import java.io.Serializable

class LocationInfoEntity : Serializable {
    @SerializedName("id")
    var id: Int? = null

    @SerializedName("car_id")
    var carId: Int? = null

    @SerializedName("address")
    var address: String? = null

    @SerializedName("speed")
    var speed: String? = null

    @SerializedName("lat")
    var lat: String? = null

    @SerializedName("lng")
    var lng: String? = null

    @SerializedName("status")
    var status: String? = null

    @SerializedName("user_id")
    var userId: String? = null

    @SerializedName("created_at")
    var createdAt: String? = null

    @SerializedName("active_status")
    var activeStatus: String? = null
}