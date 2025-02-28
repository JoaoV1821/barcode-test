package com.pessoal

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {
    private const val FASTAPI_BASE_URL = "http://10.150.44.11:3000"
    private const val UPC_BASE_URL = "https://api.upcitemdb.com/prod/trial/"

    private val logging = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    private val client = OkHttpClient.Builder()
        .addInterceptor(logging)
        .build()

    val instanceFastAPI: ProductApi by lazy {
        Retrofit.Builder()
            .baseUrl(FASTAPI_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()
            .create(ProductApi::class.java)
    }

    val instanceUPC: BarcodeApi by lazy {
        Retrofit.Builder()
            .baseUrl(UPC_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()
            .create(BarcodeApi::class.java)
    }
}
