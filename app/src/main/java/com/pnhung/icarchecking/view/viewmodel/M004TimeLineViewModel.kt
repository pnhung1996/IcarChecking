package com.pnhung.icarchecking.view.viewmodel

import com.pnhung.icarchecking.view.api.APIRequest
import com.pnhung.icarchecking.view.api.model.entities.CarInfoEntity
import com.pnhung.icarchecking.view.api.model.entities.ChangePasswordEntity

class M004TimeLineViewModel : BaseViewModel() {
    fun getListDayByCar(carId: Int) {
        val api: APIRequest = getWS().create(APIRequest::class.java)
        api.getListDayByCar(token, carId)?.enqueue(initResponse(API_KEY_GET_GROUP_DAY))
    }

    fun getTimeLineDetail(carInfo: CarInfoEntity, date: String) {
        val createAtGteq = "00:00 $date"
        val createAtLteq = "23:59 $date"
        val api: APIRequest = getWS().create(APIRequest::class.java)
        api.getTimeLineDetail(token, 1, carInfo.id!!, createAtGteq, createAtLteq)
            ?.enqueue(initResponse(API_KEY_GET_TIME_LINE_DETAIL))
    }

    companion object {
        const val API_KEY_GET_GROUP_DAY = "API_KEY_GET_GROUP_DAY"
        const val API_KEY_GET_TIME_LINE_DETAIL = "API_KEY_GET_TIME_LINE_DETAIL"
    }
}
