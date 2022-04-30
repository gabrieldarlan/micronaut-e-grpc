package br.com.darlan.grpc.repository

import br.com.darlan.grpc.domain.Product
import io.micronaut.data.annotation.Repository
import io.micronaut.data.jpa.repository.JpaRepository

@Repository
interface ProductRepository : JpaRepository<Product, Long>