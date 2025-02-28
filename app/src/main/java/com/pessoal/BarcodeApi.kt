package com.pessoal

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface BarcodeApi {
    @GET("lookup")
    fun getProduct(@Query("upc") barcode: String): Call<ProductResponse>

    @POST("/products/create")
    fun postProduct(@Body request: Product): Call<Product>

}