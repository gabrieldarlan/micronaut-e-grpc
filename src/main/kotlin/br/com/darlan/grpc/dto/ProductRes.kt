package br.com.darlan.grpc.dto

data class ProductRes(
    val id: Long,
    val name: String,
    val price: Double,
    val quantityInStock: Int
)