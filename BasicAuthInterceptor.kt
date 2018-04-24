package com.developer.rohal.mantra

import okhttp3.Credentials
import okhttp3.Interceptor
import okhttp3.Response
import java.io.IOException

class BasicAuthInterceptor(user: String, password: String) : Interceptor {

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