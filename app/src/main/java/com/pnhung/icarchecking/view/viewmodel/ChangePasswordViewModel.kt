package com.pnhung.icarchecking.view.viewmodel

import com.pnhung.icarchecking.view.api.APIRequest
import com.pnhung.icarchecking.view.api.model.entities.ChangePasswordEntity
import com.pnhung.icarchecking.view.api.model.entities.UpdateUsernameEntity

class ChangePasswordViewModel : BaseViewModel() {
    fun changePassword(oldPassword: String, newPassword: String) {
        val api: APIRequest = getWS().create(APIRequest::class.java)
        val changePasswordEntity = ChangePasswordEntity()
        changePasswordEntity.oldPassword = oldPassword
        changePasswordEntity.newPassword = newPassword
        api.changePassword(token, changePasswordEntity)?.enqueue(initResponse(API_KEY_CHANGE_PASSWORD))
    }

    companion object{
        const val API_KEY_CHANGE_PASSWORD = "API_KEY_CHANGE_PASSWORD"
    }
}
