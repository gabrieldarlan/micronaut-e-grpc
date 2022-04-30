package br.com.darlan.grpc.services.impl

import br.com.darlan.grpc.domain.Product
import br.com.darlan.grpc.dto.ProductReq
import br.com.darlan.grpc.exceptions.AlreadyExistsException
import br.com.darlan.grpc.exceptions.ProductNotFoundException
import br.com.darlan.grpc.repository.ProductRepository
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertThrowsExactly
import org.junit.jupiter.api.Test
import org.mockito.Mockito.`when`
import org.mockito.Mockito.mock
import java.util.*

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

    @Test
    fun `when create method is call with duplicated product-name, throws AlreadyExistsException`() {
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

        `when`(productRepository.findByNameIgnoreCase(productInput.name)).thenReturn(productOutput)

        val productReq = ProductReq(
            name = "product name",
            price = 10.00,
            quantityInStock = 5
        )

        assertThrowsExactly(AlreadyExistsException::class.java) {
            productService.create(productReq)
        }
    }

    @Test
    fun `when findById method is call with valid id a ProductRes is returned`() {
        val productInput = 1L
        val productOutput = Product(
            id = 1,
            name = "product name",
            price = 10.00,
            quantityInStock = 5
        )

        `when`(productRepository.findById(productInput)).thenReturn(Optional.of(productOutput))

        val productRes = productService.findById(productInput)

        assertEquals(productInput, productRes.id)
        assertEquals(productOutput.name, productRes.name)
    }

    @Test
    fun `when findById method is call with invalid id, throws ProductNotFoundException`() {
        val id = 1L

        assertThrowsExactly(ProductNotFoundException::class.java) {
            productService.findById(id)
        }
    }
}