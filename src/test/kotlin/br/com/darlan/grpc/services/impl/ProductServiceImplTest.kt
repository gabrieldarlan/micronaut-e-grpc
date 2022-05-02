package br.com.darlan.grpc.services.impl

import br.com.darlan.grpc.domain.Product
import br.com.darlan.grpc.dto.ProductReq
import br.com.darlan.grpc.dto.ProductRes
import br.com.darlan.grpc.dto.ProductUpdateReq
import br.com.darlan.grpc.exceptions.AlreadyExistsException
import br.com.darlan.grpc.exceptions.ProductNotFoundException
import br.com.darlan.grpc.repository.ProductRepository
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertThrowsExactly
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow
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

    @Test
    fun `when update method is call with duplicated product-name, throws AlreadyExistsException`() {
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

        val productReq = ProductUpdateReq(
            id = 1L,
            name = "product name",
            price = 10.00,
            quantityInStock = 5
        )

        assertThrowsExactly(AlreadyExistsException::class.java) {
            productService.update(productReq)
        }
    }

    @Test
    fun `when update method is call with invalid id, throws ProductNotFoundException`() {
        val productReq = ProductUpdateReq(
            id = 1L,
            name = "product name",
            price = 10.00,
            quantityInStock = 5
        )

        assertThrowsExactly(ProductNotFoundException::class.java) {
            productService.update(productReq)
        }
    }

    @Test
    fun `when update method is call with valid data a ProductRes is returned`() {
        val productInput = Product(id = 1, name = "updated product", price = 11.0, quantityInStock = 10)
        val findByOutput = Product(id = 1, name = "product name", price = 10.0, quantityInStock = 5)

        `when`(productRepository.findById(productInput.id!!)).thenReturn(Optional.of(findByOutput))

        `when`(productRepository.update(productInput)).thenReturn(productInput)

        val productUpdateReq = ProductUpdateReq(id = 1, name = "updated product", price = 11.0, quantityInStock = 10)

        val productRes = productService.update(productUpdateReq)
        assertEquals(productUpdateReq.name, productRes.name)

    }

    @Test
    fun `when delete method is call with valid id a ProductRes is deleted`() {
        val id = 1L

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

        `when`(productRepository.findById(id)).thenReturn(Optional.of(productOutput))

        assertDoesNotThrow {
            productService.delete(id)
        }
    }

    @Test
    fun `when delete method is call with invalid id a throws ProductNotFoundException`() {
        val id = 1L

        `when`(productRepository.findById(id)).thenReturn(Optional.empty())

        assertThrowsExactly(ProductNotFoundException::class.java) {
            productService.delete(id)
        }
    }

    @Test
    fun `when findAll method is call a list of ProductRes is returned`() {
        val productList = listOf(
            Product(
                id = 1,
                name = "product name",
                price = 10.0,
                quantityInStock = 5
            )
        )
        `when`(productRepository.findAll()).thenReturn(productList)

        val productRes = productService.findAll()
        assertEquals(productList[0].name, productRes[0].name)

    }

    @Test
    fun `when findAll method is call without products a empty list of ProductRes is returned`() {
        val productList = emptyList<ProductRes>()
        `when`(productRepository.findAll()).thenReturn(emptyList())

        val productRes = productService.findAll()
        assertEquals(productList.size, productRes.size)

    }
}