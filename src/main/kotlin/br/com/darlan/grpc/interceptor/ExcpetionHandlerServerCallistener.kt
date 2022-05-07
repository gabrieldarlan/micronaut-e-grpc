package br.com.darlan.grpc.interceptor

import br.com.darlan.grpc.exceptions.BaseBusinessException
import io.grpc.ForwardingServerCallListener
import io.grpc.Metadata
import io.grpc.ServerCall

class ExceptionHandlerServerCallListener<ReqT, RespT>(
    private val serveCall: ServerCall<ReqT, RespT>?,
    private val metadata: Metadata?,
    delegate: ServerCall.Listener<ReqT>
) : ForwardingServerCallListener.SimpleForwardingServerCallListener<ReqT>(delegate) {
    override fun onHalfClose() {
        try {
            super.onHalfClose()
        } catch (e: BaseBusinessException) {
            serveCall?.close(e.statusCode().toStatus().withDescription(e.errorMessage()), metadata)
        }
    }

    override fun onReady() {
        try {
            super.onReady()
        } catch (e: BaseBusinessException) {
            serveCall?.close(e.statusCode().toStatus().withDescription(e.errorMessage()), metadata)
        }
    }
}