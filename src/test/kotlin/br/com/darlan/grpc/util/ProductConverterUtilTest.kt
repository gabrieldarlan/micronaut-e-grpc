package br.com.darlan.grpc.util

import br.com.darlan.grpc.domain.Product
import br.com.darlan.grpc.dto.ProductReq
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.function.Executable

class ProductConverterUtilTest {

    @Test
    fun `when toProductRes is call, should return a ProductRes with all data`() {
        val product = Product(id = 1, name = "product-name", price = 10.90, quantityInStock = 10)
        val toProductRes = product.toProductRes()
//        assertEquals(product.id, toProductRes.id)

        assertAll(
            Executable { assertEquals(product.id, toProductRes.id) },
            Executable { assertEquals(product.name, toProductRes.name) },
            Executable { assertEquals(product.price, toProductRes.price) },
            Executable { assertEquals(product.quantityInStock, toProductRes.quantityInStock) },
        )
    }

    @Test
    fun `when toProductRes is call, should return a Product with all data`() {
        val productReq = ProductReq(name = "product-name", price = 10.90, quantityInStock = 10)
        val product = productReq.toDomain()

        assertAll(
            Executable { assertEquals(null, product.id) },
            Executable { assertEquals(productReq.name, product.name) },
            Executable { assertEquals(productReq.price, product.price) },
            Executable { assertEquals(productReq.quantityInStock, product.quantityInStock) },
        )
    }

}
