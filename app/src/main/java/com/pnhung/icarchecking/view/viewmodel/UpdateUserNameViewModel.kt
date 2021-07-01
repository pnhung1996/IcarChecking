package com.pnhung.icarchecking.view.viewmodel

import com.pnhung.icarchecking.view.api.APIRequest
import com.pnhung.icarchecking.view.api.model.entities.UpdateUserEntity
import com.pnhung.icarchecking.view.api.model.entities.UserCreateNewPassEntity

class UpdateUserNameViewModel : BaseViewModel() {
    fun updateUserName(newUserName : String) {
        val api: APIRequest = getWS().create(APIRequest::class.java)
        val updateUserEntity = UpdateUserEntity()
        updateUserEntity.username = newUserName
        api.updateUserName(token, updateUserEntity)?.enqueue(initResponse(API_KEY_UPDATE_USERNAME))
    }

    companion object{
        const val API_KEY_UPDATE_USERNAME = "API_KEY_UPDATE_USERNAME"
    }
}
