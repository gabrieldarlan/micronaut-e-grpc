package br.com.darlan.grpc.util

import br.com.darlan.grpc.domain.Product
import br.com.darlan.grpc.dto.ProductReq
import br.com.darlan.grpc.dto.ProductRes

fun Product.toProductRes(): ProductRes {
    return ProductRes(
        id = id!!,
        name = name,
        price = price,
        quantityInStock = quantityInStock
    )
}

fun ProductReq.toDomain(): Product {
    return Product(
        id = null,
        name = name,
        price = price,
        quantityInStock = quantityInStock
    )
}
