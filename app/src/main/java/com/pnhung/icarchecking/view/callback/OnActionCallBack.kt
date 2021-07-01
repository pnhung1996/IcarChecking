package com.pnhung.icarchecking.view.callback

interface OnActionCallBack {
    fun callBack(key:String, data : Any?){}
    fun logout(){}
    fun showWarnNoInternet(){}
}
