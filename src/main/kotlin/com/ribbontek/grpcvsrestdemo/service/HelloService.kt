package com.ribbontek.grpcvsrestdemo.service

import com.ribbontek.grpcvsrestdemo.model.HelloRequestDto
import com.ribbontek.grpcvsrestdemo.model.HelloResponseDto
import jakarta.validation.Valid
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import org.springframework.validation.annotation.Validated

@Service
@Validated
class HelloService {
    private val logger = LoggerFactory.getLogger(this::class.java)

    fun helloWorld(
        @Valid helloRequestDto: HelloRequestDto,
    ): HelloResponseDto {
        logger.info("Hello ${helloRequestDto.name}")
        return HelloResponseDto(greeting = "Hello ${helloRequestDto.name}")
    }
}
