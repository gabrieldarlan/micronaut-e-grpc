package br.com.darlan.grpc.resources

import br.com.darlan.grpc.ProductServiceRequest
import br.com.darlan.grpc.ProductServiceUpdateRequest
import br.com.darlan.grpc.ProductsServiceGrpc.ProductsServiceBlockingStub
import br.com.darlan.grpc.RequestById
import io.grpc.Status
import io.grpc.StatusRuntimeException
import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import org.flywaydb.core.Flyway
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

@MicronautTest
internal class ProductResourcesTestIT(
    private val flyway: Flyway,
    private val productsServiceBlockingStub: ProductsServiceBlockingStub
) {

    @BeforeEach
    fun setUp() {
        flyway.run {
            clean()
            migrate()
        }
    }

    @Test
    fun `when ProductsServiceGrpc create method is call with valid data a success is returned`() {
        val request = ProductServiceRequest.newBuilder()
            .setName("product name")
            .setPrice(20.99)
            .setQuantityInStock(10)
            .build()

        val response = productsServiceBlockingStub.create(request)

        assertEquals(3, response.id)
        assertEquals("product name", response.name)

    }

    @Test
    fun `when ProductsServiceGrpc create method is call with invalid data a AlreadyExistsException is returned`() {
        val request = ProductServiceRequest.newBuilder()
            .setName("Product A")
            .setPrice(20.99)
            .setQuantityInStock(10)
            .build()

        val description = "Produto ${request.name} já cadastrado no sistema."
        val response = assertThrows(StatusRuntimeException::class.java) {
            productsServiceBlockingStub.create(request)
        }

        assertEquals(Status.ALREADY_EXISTS.code, response.status.code)
        assertEquals(description, response.status.description)
    }

    @Test
    fun `when ProductsServiceGrpc findById method is call with valid id a success is returned`() {
        val request = RequestById.newBuilder()
            .setId(1)
            .build()

        val response = productsServiceBlockingStub.findById(request)

        assertEquals(1, response.id)
        assertEquals("Product A", response.name)

    }

    @Test
    fun `when ProductsServiceGrpc findById method is call with invalid id a ProductNotFound is returned`() {
        val request = RequestById.newBuilder()
            .setId(10)
            .build()
        val description = "Produto com ID ${request.id} não encontrado."
        val response = assertThrows(StatusRuntimeException::class.java) {
            productsServiceBlockingStub.findById(request)
        }

        assertEquals(Status.NOT_FOUND.code, response.status.code)
        assertEquals(description, response.status.description)
    }

    @Test
    fun `when ProductsServiceGrpc update method is call with valid data a success is returned`() {
        val request = ProductServiceUpdateRequest.newBuilder()
            .setId(2L)
            .setName("product name")
            .setPrice(20.99)
            .setQuantityInStock(10)
            .build()

        val response = productsServiceBlockingStub.update(request)

        assertEquals(2, response.id)
        assertEquals("product name", response.name)

    }

    @Test
    fun `when ProductsServiceGrpc update method is call with invalid data a AlreadyExistsException is returned`() {
        val request = ProductServiceUpdateRequest.newBuilder()
            .setId(2L)
            .setName("Product A")
            .setPrice(20.99)
            .setQuantityInStock(10)
            .build()

        val description = "Produto ${request.name} já cadastrado no sistema."
        val response = assertThrows(StatusRuntimeException::class.java) {
            productsServiceBlockingStub.update(request)
        }

        assertEquals(Status.ALREADY_EXISTS.code, response.status.code)
        assertEquals(description, response.status.description)
    }

    @Test
    fun `when ProductsServiceGrpc update method is call with invalid id a ProductNotFound is returned`() {
        val request = ProductServiceUpdateRequest.newBuilder()
            .setId(10L)
            .setName("Product C")
            .setPrice(20.99)
            .setQuantityInStock(10)
            .build()

        val description = "Produto com ID ${request.id} não encontrado."
        val response = assertThrows(StatusRuntimeException::class.java) {
            productsServiceBlockingStub.update(request)
        }

        assertEquals(Status.NOT_FOUND.code, response.status.code)
        assertEquals(description, response.status.description)
    }

    @Test
    fun `when ProductsServiceGrpc delete method is call with valid data a success is returned`() {
        val request = RequestById.newBuilder()
            .setId(2L)
            .build()

        assertDoesNotThrow {
            productsServiceBlockingStub.delete(request)
        }

    }

    @Test
    fun `when ProductsServiceGrpc delete method is call with invalid id a ProductNotFound is returned`() {
        val request = RequestById.newBuilder()
            .setId(12L)
            .build()

        val description = "Produto com ID ${request.id} não encontrado."
        val response = assertThrows(StatusRuntimeException::class.java) {
            productsServiceBlockingStub.delete(request)
        }

        assertEquals(Status.NOT_FOUND.code, response.status.code)
        assertEquals(description, response.status.description)
    }
}