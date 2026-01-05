package org.duhan.commerce.gateway

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.ConfigurationPropertiesScan
import org.springframework.boot.runApplication
import org.springframework.cloud.client.discovery.EnableDiscoveryClient

@EnableDiscoveryClient
@ConfigurationPropertiesScan
@SpringBootApplication
class CommerceGatewayApplication

fun main(args: Array<String>) {
    runApplication<CommerceGatewayApplication>(*args)
}
