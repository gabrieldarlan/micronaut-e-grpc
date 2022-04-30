package br.com.darlan.grpc.services.impl

import br.com.darlan.grpc.dto.ProductReq
import br.com.darlan.grpc.dto.ProductRes
import br.com.darlan.grpc.repository.ProductRepository
import br.com.darlan.grpc.services.ProductService
import br.com.darlan.grpc.util.toDomain
import br.com.darlan.grpc.util.toProductRes
import jakarta.inject.Singleton

@Singleton
class ProductServiceImpl(
    private val productRepository: ProductRepository
) : ProductService {
    override fun create(req: ProductReq): ProductRes {
        val product = req.toDomain()
        val productSaved = productRepository.save(product)
        return productSaved.toProductRes()
    }
}