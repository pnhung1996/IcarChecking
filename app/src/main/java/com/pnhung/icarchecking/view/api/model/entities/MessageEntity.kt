package com.pnhung.icarchecking.view.api.model.entities

import com.google.gson.annotations.SerializedName
import java.io.Serializable
class MessageEntity : Serializable {
    @SerializedName("message")
    val carInfo: CarInfoEntity? = null
}