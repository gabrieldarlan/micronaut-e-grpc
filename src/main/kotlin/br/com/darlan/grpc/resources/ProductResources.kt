package br.com.darlan.grpc.resources

import br.com.darlan.grpc.*
import br.com.darlan.grpc.dto.ProductReq
import br.com.darlan.grpc.dto.ProductUpdateReq
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

    override fun findById(request: RequestById?, responseObserver: StreamObserver<ProductServiceResponse>?) {
        val productRes = productService.findById(request!!.id)
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

    override fun update(
        request: ProductServiceUpdateRequest?,
        responseObserver: StreamObserver<ProductServiceResponse>?
    ) {
        val payload = ValidationUtil.validateUpdatePayload(request)
        val productReq = ProductUpdateReq(
            id = payload.id,
            name = payload.name,
            price = payload.price,
            quantityInStock = payload.quantityInStock
        )
        val productRes = productService.update(productReq)

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

    override fun delete(request: RequestById?, responseObserver: StreamObserver<Empty>?) {
        productService.delete(request!!.id).let {
            responseObserver?.run {
                onNext(Empty.newBuilder().build())
                onCompleted()
            }
        }
    }

    override fun findAll(request: Empty?, responseObserver: StreamObserver<ProductsList>?) {
        productService.findAll().let { productRes ->
            productRes.map {
                ProductServiceResponse.newBuilder()
                    .setId(it.id)
                    .setName(it.name)
                    .setPrice(it.price)
                    .setQuantityInStock(it.quantityInStock)
                    .build()
            }.let { productListRest ->
                ProductsList.newBuilder().addAllProducts(productListRest).build()
            }.let {
                responseObserver?.run {
                    onNext(it)
                    onCompleted()
                }
            }
        }
    }
}