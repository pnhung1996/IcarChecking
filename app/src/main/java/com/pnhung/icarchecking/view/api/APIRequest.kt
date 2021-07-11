package com.pnhung.icarchecking.view.api

import com.pnhung.icarchecking.view.api.model.CarInforModelRes
import com.pnhung.icarchecking.view.api.model.ListDayModelRes
import com.pnhung.icarchecking.view.api.model.TimeLineDetailModelRes
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
        @Query("page") page: Int
    ): Call<CarInforModelRes?>?

    @PUT("users/update_profile")
    @Headers("Content-Type:application/json")
    fun updateUserName(
        @Header("Authorization") token: String?,
        @Body body: UpdateUsernameEntity
    ): Call<UserInfoModelRes?>?

    @PUT("users/change_password")
    @Headers("Content-Type:application/json")
    fun changePassword(
        @Header("Authorization") token: String?,
        @Body body: ChangePasswordEntity
    ): Call<UserInfoModelRes?>?

    @GET("locations/group_day")
    @Headers("Content-Type:application/json")
    fun getListDayByCar(
        @Header("Authorization") token: String?,
        @Query("car_id") carId: Int
    ): Call<ListDayModelRes?>?

    @GET("locations")
    @Headers("Content-Type:application/json")
    fun getTimeLineDetail(
        @Header("Authorization") token: String?,
        @Query("page") page: Int,
        @Query("filter[car_id_eq]") carIdEq: Int,
        @Query("filter[created_at_gteq]") createAtGteq: String,
        @Query("filter[created_at_lteq]") createAtLteq: String
    ): Call<TimeLineDetailModelRes?>?

}