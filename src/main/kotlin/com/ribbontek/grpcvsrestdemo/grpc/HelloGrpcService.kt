package com.ribbontek.grpcvsrestdemo.grpc

import com.ribbontek.grpcvsrestdemo.HelloRequest
import com.ribbontek.grpcvsrestdemo.HelloResponse
import com.ribbontek.grpcvsrestdemo.HelloServiceGrpcKt.HelloServiceCoroutineImplBase
import com.ribbontek.grpcvsrestdemo.helloResponse
import com.ribbontek.grpcvsrestdemo.model.HelloRequestDto
import com.ribbontek.grpcvsrestdemo.model.HelloResponseDto
import com.ribbontek.grpcvsrestdemo.service.HelloService
import net.devh.boot.grpc.server.service.GrpcService

@GrpcService
class HelloGrpcService(
    private val helloService: HelloService,
) : HelloServiceCoroutineImplBase() {
    override suspend fun helloWorld(request: HelloRequest): HelloResponse {
        return helloService.helloWorld(request.toHelloRequestModel()).toHelloResponse()
    }

    private fun HelloRequest.toHelloRequestModel(): HelloRequestDto {
        return HelloRequestDto(name = this.name)
    }

    private fun HelloResponseDto.toHelloResponse(): HelloResponse =
        let { source ->
            helloResponse {
                this.greeting = source.greeting
            }
        }
}
