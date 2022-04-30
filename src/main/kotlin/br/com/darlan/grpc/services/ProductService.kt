package br.com.darlan.grpc.services

import br.com.darlan.grpc.dto.ProductReq
import br.com.darlan.grpc.dto.ProductRes

interface ProductService {
    fun create(req: ProductReq):ProductRes
}