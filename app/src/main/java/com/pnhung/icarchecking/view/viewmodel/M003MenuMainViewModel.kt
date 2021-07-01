package com.pnhung.icarchecking.view.viewmodel

import com.pnhung.icarchecking.CommonUtils
import com.pnhung.icarchecking.view.api.APIRequest
import com.pnhung.icarchecking.view.api.model.entities.AccountEntity

class M003MenuMainViewModel : BaseViewModel(){

    fun getListCar(){
        val token = CommonUtils.getInstance().getPref(TOKEN)
        val api : APIRequest = getWS().create(APIRequest::class.java)
        api.getListCar(token,1 , 2)?.enqueue(initResponse(API_GET_LIST_CAR))
    }

    companion object{
        const val API_GET_LIST_CAR = "API_GET_LIST_CAR"
    }
}
