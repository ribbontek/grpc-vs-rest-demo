package com.ribbontek.grpcvsrestdemo.util

import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import org.awaitility.Awaitility
import org.springframework.web.client.RestTemplate
import java.io.Serializable
import java.util.concurrent.TimeUnit

data class Health(val status: String) : Serializable

object ServiceUtil {
    private val objectMapper: ObjectMapper = ObjectMapper().registerKotlinModule()
        .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)

    private fun String.toHealth() = objectMapper.readValue(this, Health::class.java)

    fun awaitService(
        name: String,
        host: String,
        port: Int,
    ) {
        val restTemplate = RestTemplate()
        Awaitility.await().atMost(60, TimeUnit.SECONDS).until {
            println("Waiting for $name...")
            try {
                restTemplate.getForEntity(
                    "http://$host:$port/actuator/health",
                    String::class.java,
                ).body?.toHealth()?.status == "UP"
            } catch (ex: Exception) {
                ex.printStackTrace()
                false
            }
        }
        println("Found $name")
    }
}
