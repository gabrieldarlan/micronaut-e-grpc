package br.com.darlan.grpc.services.impl

import br.com.darlan.grpc.dto.ProductReq
import br.com.darlan.grpc.dto.ProductRes
import br.com.darlan.grpc.dto.ProductUpdateReq
import br.com.darlan.grpc.exceptions.AlreadyExistsException
import br.com.darlan.grpc.exceptions.ProductNotFoundException
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
        return verifyName(req.name).run {
            productRepository.save(req.toDomain()).toProductRes()
        }
    }

    override fun findById(id: Long): ProductRes {
        return productRepository.findById(id).orElseThrow { ProductNotFoundException(id) }.toProductRes()
    }

    override fun update(req: ProductUpdateReq): ProductRes {
        verifyName(req.name)
        return productRepository.findById(req.id).orElseThrow { ProductNotFoundException(req.id) }.let { product ->
            product.copy(name = req.name, price = req.price, quantityInStock = req.quantityInStock).let {
                productRepository.update(it).toProductRes()
            }
        }
    }

    private fun verifyName(name: String) {
        productRepository.findByNameIgnoreCase(name)?.let {
            throw AlreadyExistsException(name)
        }
    }
}