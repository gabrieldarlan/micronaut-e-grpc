package br.com.darlan.grpc.exceptions

import io.grpc.Status

class InvalidArgumentException(
    private val argumentName: String
) : BaseBusinessException() {
    override fun errorMessage(): String {
        return "Argumento $argumentName inválido."
    }

    override fun statusCode(): Status.Code {
        return Status.Code.INVALID_ARGUMENT
    }
}