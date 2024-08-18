package com.ribbontek.grpcvsrestdemo.grpc.abstract

import org.junit.jupiter.api.TestInstance
import org.springframework.boot.test.context.SpringBootTest
import java.lang.annotation.Inherited

@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
@Inherited
@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
annotation class GrpcSpringBootTest
