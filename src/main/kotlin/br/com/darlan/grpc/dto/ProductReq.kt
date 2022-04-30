package br.com.darlan.grpc.dto

data class ProductReq(
    val name: String,
    val price: Double,
    val quantityInStock: Int
)