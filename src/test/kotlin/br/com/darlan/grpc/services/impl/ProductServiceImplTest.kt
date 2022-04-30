package br.com.darlan.grpc.services.impl

import br.com.darlan.grpc.domain.Product
import br.com.darlan.grpc.dto.ProductReq
import br.com.darlan.grpc.repository.ProductRepository
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.mockito.Mockito.`when`
import org.mockito.Mockito.mock

internal class ProductServiceImplTest {
    private val productRepository = mock(ProductRepository::class.java)
    private val productService = ProductServiceImpl(productRepository)

    @Test
    fun `when create method is call with valid data a ProductRes is returned`() {
        val productInput = Product(
            id = null,
            name = "product name",
            price = 10.00,
            quantityInStock = 5
        )
        val productOutput = Product(
            id = 1,
            name = "product name",
            price = 10.00,
            quantityInStock = 5
        )

        `when`(productRepository.save(productInput)).thenReturn(productOutput)

        val productReq = ProductReq(
            name = "product name",
            price = 10.00,
            quantityInStock = 5
        )

        val productRes = productService.create(productReq)

        assertEquals(productReq.name, productRes.name)
    }

}