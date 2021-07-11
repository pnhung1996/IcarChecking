package com.pnhung.icarchecking.view.api.model.entities

import com.google.gson.annotations.SerializedName
import java.io.Serializable

class ChangePasswordEntity : Serializable{
    @SerializedName("old_password")
    var oldPassword : String? = null

    @SerializedName("new_password")
    var newPassword : String? = null
}