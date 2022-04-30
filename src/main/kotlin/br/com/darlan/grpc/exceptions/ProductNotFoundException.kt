package br.com.darlan.grpc.exceptions

import io.grpc.Status

class ProductNotFoundException(
    private val productId: Long
) : BaseBusinessException() {
    override fun errorMessage(): String {
        return "Produto com ID $productId não cadastrado."
    }

    override fun statusCode(): Status.Code {
        return Status.Code.NOT_FOUND
    }
}