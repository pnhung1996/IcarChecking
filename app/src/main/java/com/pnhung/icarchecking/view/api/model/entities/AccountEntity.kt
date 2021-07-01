package com.pnhung.icarchecking.view.api.model.entities

import com.google.gson.annotations.SerializedName
import com.pnhung.icarchecking.App
import com.pnhung.icarchecking.CommonUtils
import lombok.ToString
import java.io.Serializable

@ToString
class AccountEntity : Serializable {
    @SerializedName("phone")
    var mPhone: String? = null

    @SerializedName("password")
    var mPass: String? = null

    @SerializedName("device_token")
    var mDeviceToken: String? = null

    constructor(mPhone : String, mPass : String) : super(){
        this.mPhone = mPhone
        this.mPass = mPass
        mDeviceToken = CommonUtils.getInstance().getPref(App.DEVICE_TOKEN)
    }
}

