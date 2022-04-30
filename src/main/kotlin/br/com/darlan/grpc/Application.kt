package br.com.darlan.grpc

import io.micronaut.runtime.Micronaut.*

fun main(args: Array<String>) {
    build()
        .args(*args)
        .packages("br.com.darlan.grpc")
        .start()
}

