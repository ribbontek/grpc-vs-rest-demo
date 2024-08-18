package com.ribbontek.grpcvsrestdemo.grpc

import com.ribbontek.grpcvsrestdemo.HelloServiceGrpcKt.HelloServiceCoroutineStub
import com.ribbontek.grpcvsrestdemo.grpc.abstract.AbstractIntegTest
import com.ribbontek.grpcvsrestdemo.helloRequest
import io.grpc.Status.Code.FAILED_PRECONDITION
import io.grpc.StatusException
import kotlinx.coroutines.runBlocking
import net.devh.boot.grpc.client.inject.GrpcClient
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.MatcherAssert.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class HelloGrpcServiceTests : AbstractIntegTest() {
    @GrpcClient("clientstub")
    private lateinit var stub: HelloServiceCoroutineStub

    @Test
    fun `hello world with valid request - success`() {
        val result = runBlocking {
            stub.helloWorld(request = helloRequest { name = "Joel" })
        }

        assertThat(result.greeting, equalTo("Hello Joel"))
    }

    @Test
    fun `hello world with invalid request - failure`() {
        val result = assertThrows<StatusException> {
            runBlocking {
                stub.helloWorld(request = helloRequest { name = " " })
            }
        }

        assertThat(
            result.status.description,
            equalTo("name must not be blank"),
        )
        assertThat(result.status.code, equalTo(FAILED_PRECONDITION))
    }
}
