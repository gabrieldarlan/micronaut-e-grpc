package br.com.darlan.grpc.exceptions

import io.grpc.Status

class AlreadyExistsException(
    private val productName: String
) : BaseBusinessException() {
    override fun errorMessage(): String {
        return "Produto $productName já cadastrado no sistema."
    }

    override fun statusCode(): Status.Code {
        return Status.Code.ALREADY_EXISTS
    }
}