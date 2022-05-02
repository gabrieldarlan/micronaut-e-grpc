package br.com.darlan.grpc.resources

import br.com.darlan.grpc.*
import br.com.darlan.grpc.dto.ProductReq
import br.com.darlan.grpc.dto.ProductUpdateReq
import br.com.darlan.grpc.exceptions.BaseBusinessException
import br.com.darlan.grpc.services.ProductService
import br.com.darlan.grpc.util.ValidationUtil
import io.grpc.Status
import io.grpc.stub.StreamObserver
import io.micronaut.grpc.annotation.GrpcService

@GrpcService
class ProductResources(
    private val productService: ProductService
) : ProductsServiceGrpc.ProductsServiceImplBase() {

    override fun create(request: ProductServiceRequest?, responseObserver: StreamObserver<ProductServiceResponse>?) {
        try {
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
        } catch (ex: BaseBusinessException) {
            responseObserver?.onError(
                ex.statusCode().toStatus().withDescription(ex.errorMessage()).asRuntimeException()
            )
        } catch (ex: Throwable) {
            responseObserver?.onError(
                Status.UNKNOWN.code.toStatus().withDescription("Internal Server Error").asException()
            )
        }
    }

    override fun findById(request: RequestById?, responseObserver: StreamObserver<ProductServiceResponse>?) {
        try {
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
        } catch (ex: BaseBusinessException) {
            responseObserver?.onError(
                ex.statusCode().toStatus().withDescription(ex.errorMessage()).asRuntimeException()
            )
        } catch (ex: Throwable) {
            responseObserver?.onError(
                Status.UNKNOWN.code.toStatus().withDescription("Internal Server Error").asException()
            )
        }
    }

    override fun update(
        request: ProductServiceUpdateRequest?,
        responseObserver: StreamObserver<ProductServiceResponse>?
    ) {
        try {
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
        } catch (e: BaseBusinessException) {
            responseObserver?.onError(e.statusCode().toStatus().withDescription(e.errorMessage()).asRuntimeException())
        } catch (ex: Throwable) {
            responseObserver?.onError(
                Status.UNKNOWN.code.toStatus().withDescription("Internal Server Error").asException()
            )
        }
    }

    override fun delete(request: RequestById?, responseObserver: StreamObserver<Empty>?) {
        try {
            productService.delete(request!!.id).let {
                responseObserver?.run {
                    onNext(Empty.newBuilder().build())
                    onCompleted()
                }
            }
        } catch (e: BaseBusinessException) {
            responseObserver?.onError(e.statusCode().toStatus().withDescription(e.errorMessage()).asRuntimeException())
        } catch (ex: Throwable) {
            responseObserver?.onError(
                Status.UNKNOWN.code.toStatus().withDescription("Internal Server Error").asException()
            )
        }
    }

    override fun findAll(request: Empty?, responseObserver: StreamObserver<ProductsList>?) {
        try {
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
        } catch (e: Throwable) {
            responseObserver?.onError(
                Status.UNKNOWN.code.toStatus().withDescription("Internal Server Error").asException()
            )
        }
    }
}