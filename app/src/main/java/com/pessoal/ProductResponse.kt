package com.pessoal

data class Product(
    val ean: String,
    val title: String,
    val lowest_recorded_price: Double?,
    val images: List<String>
)

data class ProductCreate(
    val ean: String,
    val title: String,
    val lowest_recorded_price: Double?,
    val images: List<String>
)

data class ProductResponse(
    val items: List<Product>
)
