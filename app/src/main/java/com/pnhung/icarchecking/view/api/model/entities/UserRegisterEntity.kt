package com.pnhung.icarchecking.view.api.model.entities

import com.google.gson.annotations.SerializedName
import com.pnhung.icarchecking.App
import com.pnhung.icarchecking.CommonUtils
import java.io.Serializable

class UserRegisterEntity() : Serializable {

    @SerializedName("phone")
    var phone: String? = null

    @SerializedName("password")
    var password: String? = null

    @SerializedName("verification_id")
    var verificationId: String? = null

    @SerializedName("code")
    var code: String? = null

    @SerializedName("device_token")
    var deviceToken: String? = null

    constructor(phone: String, password : String, verificationId : String, code : String) : this() {
        this.phone = phone
        this.password = password
        this.verificationId = verificationId
        this.code = code
        this.deviceToken = CommonUtils.getInstance().getPref(App.DEVICE_TOKEN)
    }
}