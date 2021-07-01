package com.pnhung.icarchecking.view.viewmodel

import com.pnhung.icarchecking.view.api.APIRequest

class M005ForgotPassViewModel : BaseViewModel(){
    fun checkPhone(phone: String){
        val api : APIRequest = getWS().create(APIRequest::class.java)
        api.checkPhone(phone)?.enqueue(initResponse(API_CHECK_PHONE_KEY))
    }

    companion object{
        const val API_CHECK_PHONE_KEY = "API_CHECK_PHONE_KEY"
    }
}
