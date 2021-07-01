package com.pnhung.icarchecking.view.viewmodel

import com.pnhung.icarchecking.CommonUtils
import com.pnhung.icarchecking.view.api.APIRequest
import com.pnhung.icarchecking.view.api.model.entities.UserCreateNewPassEntity
import com.pnhung.icarchecking.view.api.model.entities.UserRegisterEntity

class M006CreateNewPassViewModel : BaseViewModel() {
    companion object {
        val API_CREATE_NEW_PASS_KEY: String = "API_CREATE_NEW_PASS_KEY"
    }

    fun createNewPass(phone: String, newPass: String, verificationId: String, code: String) {
        val api: APIRequest = getWS().create(APIRequest::class.java)
        val userEntity = UserCreateNewPassEntity(phone, newPass, verificationId, code)
        api.createNewPass(userEntity)?.enqueue(initResponse(API_CREATE_NEW_PASS_KEY))
    }

}
