package br.com.darlan.grpc.util

import br.com.darlan.grpc.ProductServiceRequest
import br.com.darlan.grpc.ProductServiceUpdateRequest
import br.com.darlan.grpc.exceptions.InvalidArgumentException

class ValidationUtil {
    companion object {
        fun validatePayload(payload: ProductServiceRequest?): ProductServiceRequest {
            payload?.let {
                if (it.name.isNullOrBlank())
                    throw InvalidArgumentException("nome")
                if (it.price.isNaN().or(it.price <= 0))
                    throw InvalidArgumentException("preço")
                return it
            }
            throw InvalidArgumentException("payload")
        }

        fun validateUpdatePayload(payload: ProductServiceUpdateRequest?): ProductServiceUpdateRequest {
            payload?.let {

                if (it.id <= 0L)
                    throw InvalidArgumentException("ID")

                if (it.name.isNullOrBlank())
                    throw InvalidArgumentException("nome")

                if (it.price.isNaN().or(it.price <= 0))
                    throw InvalidArgumentException("preço")

                return it
            }
            throw InvalidArgumentException("payload")
        }
    }
}