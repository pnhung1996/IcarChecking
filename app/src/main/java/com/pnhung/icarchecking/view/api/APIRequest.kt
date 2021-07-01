package com.pnhung.icarchecking.view.api

import com.pnhung.icarchecking.view.api.model.CarInforModelRes
import com.pnhung.icarchecking.view.api.model.UserInfoModelRes
import com.pnhung.icarchecking.view.api.model.entities.*
import retrofit2.Call
import retrofit2.http.*

interface APIRequest {
    @POST("auth/login")
    @Headers("Content-Type:application/json")
    fun login(@Body body: AccountEntity?): Call<UserInfoModelRes?>?

    @GET("auth/check")
    @Headers("Content-Type:application/json")
    fun checkPhone(@Query("phone") phone: String): Call<UserInfoModelRes?>?

    @POST("auth/register")
    @Headers("Content-Type:application/json")
    fun register(@Body body: UserRegisterEntity?): Call<UserInfoModelRes?>?

    @POST("auth/forgot_password")
    @Headers("Content-Type:application/json")
    fun createNewPass(@Body userCreateNewPassEntity: UserCreateNewPassEntity): Call<UserInfoModelRes?>?

    @GET("cars")
    @Headers("Content-Type:application/json")
    fun getListCar(
        @Header("Authorization") token: String?,
        @Query("page") page: Int,
        @Query("per_page") perPage: Int
    ): Call<CarInforModelRes?>?

    @PUT("users/update_profile")
    @Headers("Content-Type:application/json")
    fun updateUserName(
        @Header("Authorization") token: String?,
        @Body body : UpdateUserEntity
    ): Call<UserInfoModelRes?>?
}