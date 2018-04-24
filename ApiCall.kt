package com.developer.rohal.mantra

import okhttp3.Credentials
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.IOException
import java.util.concurrent.TimeUnit

class ApiCall {

    private object Holder {
        val INSTANCE = ApiCall()
    }

    companion object {
        val instance:ApiCall by lazy { Holder.INSTANCE }
    }

    var  okHttpClient:OkHttpClient?=null
    var  user:String="mantrame@emilence.com"
    var  pass:String="Emilence@1"
    var retrofit:Retrofit?=null
    fun httpClient()
    {
        okHttpClient = OkHttpClient.Builder()
                .addInterceptor(BasicAuthInterceptorClass("${user}", "${pass}"))
                .connectTimeout(60, TimeUnit.SECONDS)
                .writeTimeout(60, TimeUnit.SECONDS)
                .readTimeout(60, TimeUnit.SECONDS)
                .build()
    }
    fun retrofitClient(): Retrofit? {
         httpClient()
         retrofit = Retrofit.Builder()
                .client(okHttpClient)
                .baseUrl("http://139.59.18.239:6010/")
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        return retrofit
    }
    class BasicAuthInterceptorClass(user: String, password: String) : Interceptor {

        private val credentials: String

        init {
            this.credentials = Credentials.basic(user, password)
        }

        @Throws(IOException::class)
        override fun intercept(chain: Interceptor.Chain): Response {


            val request = chain.request()

            // Log.e("ss_token","abc");
            val authenticatedRequest = request.newBuilder()
                    .header("Authorization", credentials)
                    .build()
            return chain.proceed(authenticatedRequest)


            /*
            if(sessionManager!=null && sessionManager.getUserToken()){//essentially checking if the prefs has a non null token
                request = request.newBuilder()
                        .addHeader("ss_token", sessionManager.getUserToken()))
                .build();
            }*/


        }

    }

}