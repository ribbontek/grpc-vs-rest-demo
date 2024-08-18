package com.ribbontek.grpcvsrestdemo.rest

import com.ribbontek.grpcvsrestdemo.model.HelloRequestDto
import com.ribbontek.grpcvsrestdemo.model.HelloResponseDto
import com.ribbontek.grpcvsrestdemo.service.HelloService
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/hello")
class HelloController(
    private val helloService: HelloService,
) {
    @PostMapping
    fun helloWorld(
        @RequestBody helloRequestDto: HelloRequestDto,
    ): HelloResponseDto {
        return helloService.helloWorld(helloRequestDto)
    }
}
