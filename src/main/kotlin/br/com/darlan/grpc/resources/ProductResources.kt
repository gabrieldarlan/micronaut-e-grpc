package br.com.darlan.grpc.resources

import br.com.darlan.grpc.ProductServiceRequest
import br.com.darlan.grpc.ProductServiceResponse
import br.com.darlan.grpc.ProductsServiceGrpc
import br.com.darlan.grpc.dto.ProductReq
import br.com.darlan.grpc.services.ProductService
import br.com.darlan.grpc.util.ValidationUtil
import io.grpc.stub.StreamObserver
import io.micronaut.grpc.annotation.GrpcService

@GrpcService
class ProductResources(
    private val productService: ProductService
) : ProductsServiceGrpc.ProductsServiceImplBase() {
    override fun create(request: ProductServiceRequest?, responseObserver: StreamObserver<ProductServiceResponse>?) {
        val payload = ValidationUtil.validatePayload(request)
        val productReq = ProductReq(
            name = payload.name,
            price = payload.price,
            quantityInStock = payload.quantityInStock
        )
        val productRes = productService.create(productReq)

        val productServiceResponse = ProductServiceResponse.newBuilder()
            .setId(productRes.id)
            .setName(productRes.name)
            .setPrice(productRes.price)
            .setQuantityInStock(productRes.quantityInStock)
            .build()

        responseObserver?.apply {
            onNext(productServiceResponse)
            onCompleted()
        }
    }
}