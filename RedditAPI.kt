package com.developer.rohal.mantra

import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.*
import retrofit2.http.POST

interface RedditAPI  {
    @Multipart
    @POST("/v1/account/register")
    fun uploadFileWithPartMap(
            @PartMap partMap: HashMap<String, RequestBody>
    ): Call<ResponseBody>
    @POST("/v1/account/login")
    fun updatePassword(
            @Body body: PojoLoginData
    ): Call<ResponseBody>
    @GET("/v1/quote/getAllQuote")
    fun GetQuote(
            @Header("x-auth") token: String): Call<ResponseBody>
    @GET("/v1/quote/markFavorite")
    fun GetMark(
            @Header("x-auth") token: String,
            @Query("quoteID") id: String
    ): Call<ResponseBody>
    @GET("/v1/quote/getAllFavorites")
    fun GetAllLikeQuote(
            @Header("x-auth") token: String): Call<ResponseBody>
    @POST("/v1/quote/storeQuote")
    fun StoreBackGround(
            @Header("x-auth") token: String,
            @PartMap partMap: HashMap<String, RequestBody>): Call<ResponseBody>
    @GET("/v1/quote/addReflection")
    fun addReflection(
            @Header("x-auth") token: String,
            @Query("quoteID") id: String,
            @Query("reflection") reflection: String
    ): Call<ResponseBody>

    @Multipart
    @POST("/v1/quote/makeImage")
    fun makeImage(
            @Header("x-auth") token: String,
            @PartMap partMap: HashMap<String, RequestBody>
    ): Call<ResponseBody>
    @POST("v1/account/forgotPwd")
    fun changePassword(
            @Body body: PasswordChange
    ): Call<ResponseBody>

    @GET("v1/quote/getReflection")
    fun getReflection(
            @Header("x-auth") token: String,
            @Query("quoteID") id: String
    ): Call<ResponseBody>

    @GET("v1/quote/getAllReflection")
    fun getAllReflection(
            @Header("x-auth") token: String
    ): Call<ResponseBody>
    @GET("v1/account/setAlarm")
    fun setAlarm(
            @Header("x-auth") token: String,
            @Query("alarm") id: String
    ): Call<ResponseBody>
    @GET("v1/account/addFeedback")
    fun addFeedback(
            @Header("x-auth") token: String,
            @Query("feedback") id: String
    ): Call<ResponseBody>

    @Multipart
    @PUT("v1/account/singleUser")
    fun changePassword(
            @Header("x-auth") token: String,
            @PartMap partMap: HashMap<String, RequestBody>
    ): Call<ResponseBody>


    @GET("v1/account/getScore")
    fun getPoints(
            @Header("x-auth") token: String
    ): Call<ResponseBody>

    @GET("/v1/quote/getAllQuote")
    fun GetNextQuote(
            @Header("x-auth") token: String,
            @Query("quoteID") id: String
    ): Call<ResponseBody>
}