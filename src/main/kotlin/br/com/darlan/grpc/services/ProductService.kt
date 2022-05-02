package br.com.darlan.grpc.services

import br.com.darlan.grpc.dto.ProductReq
import br.com.darlan.grpc.dto.ProductRes
import br.com.darlan.grpc.dto.ProductUpdateReq

interface ProductService {
    fun create(req: ProductReq): ProductRes
    fun findById(id: Long): ProductRes
    fun update(req: ProductUpdateReq): ProductRes
    fun delete(id: Long)
    fun findAll(): List<ProductRes>

}