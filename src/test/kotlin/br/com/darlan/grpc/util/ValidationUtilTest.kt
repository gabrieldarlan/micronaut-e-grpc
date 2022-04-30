package br.com.darlan.grpc.util

import br.com.darlan.grpc.ProductServiceRequest
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class ValidationUtilTest {

    @Test
    fun `when validatePayload method is call with valid data, should not throw exception`() {
        val request = ProductServiceRequest.newBuilder()
            .setName("product name")
            .setPrice(20.99)
            .setQuantityInStock(10)
            .build()

        Assertions.assertDoesNotThrow {
            ValidationUtil.validatePayload(request)
        }
    }

    @Test
    fun `when validatePayload method is call with invalid product name, should throw exception`() {
        val request = ProductServiceRequest.newBuilder()
            .setName("")
            .setPrice(20.99)
            .setQuantityInStock(10)
            .build()

        Assertions.assertThrowsExactly(IllegalArgumentException::class.java) {
            ValidationUtil.validatePayload(request)
        }
    }

    @Test
    fun `when validatePayload method is call with invalid product price, should throw exception`() {
        val request = ProductServiceRequest.newBuilder()
            .setName("product name")
            .setPrice(-20.99)
            .setQuantityInStock(10)
            .build()

        Assertions.assertThrowsExactly(IllegalArgumentException::class.java) {
            ValidationUtil.validatePayload(request)
        }
    }

    @Test
    fun `when validatePayload method is call with null payload, should throw exception`() {
        val request = null

        Assertions.assertThrowsExactly(IllegalArgumentException::class.java) {
            ValidationUtil.validatePayload(request)
        }
    }

}