package com.pessoal

import retrofit2.Call
import retrofit2.http.*

interface ProductApi {
    @POST("/products/create")
    fun createProduct(@Body product: Product): Call<Product>

    @GET("/products/findAll")
    fun getAllProducts(): Call<List<Product>>
}
