package com.ribbontek.grpcvsrestdemo

import com.ribbontek.grpcvsrestdemo.util.GrpcSimUtils
import com.ribbontek.grpcvsrestdemo.util.ServiceUtil.awaitService
import io.gatling.app.Gatling
import io.gatling.grpc.enterprise.`Usage$`
import io.gatling.javaapi.core.CoreDsl.atOnceUsers
import io.gatling.javaapi.core.CoreDsl.feed
import io.gatling.javaapi.core.CoreDsl.listFeeder
import io.gatling.javaapi.core.CoreDsl.scenario
import io.gatling.javaapi.grpc.GrpcDsl.grpc
import io.gatling.javaapi.grpc.GrpcDsl.response
import io.gatling.javaapi.grpc.GrpcDsl.statusCode
import io.grpc.Status
import java.util.concurrent.atomic.AtomicInteger

class GrpcSimulation : AbstractGrpcSim() {
    private val grpcConf = grpc.forAddress(simulationHost, simulationGrpcPort).usePlaintext()

    private val helloWorldSuccess =
        grpc("Hello World Success")
            .unary(HelloServiceGrpcKt.helloWorldMethod)
            .send { session ->
                helloRequest {
                    name = session.getString("name") ?: "null"
                }
            }
            .check(
                statusCode().shouldBe(Status.Code.OK),
                response(HelloResponse::getGreeting).isEL("Hello #{name}"),
            )

    private val badRequestFailure =
        grpc("Hello World Failure")
            .unary(HelloServiceGrpcKt.helloWorldMethod)
            .send(HelloRequest.newBuilder().build())
            .check(statusCode().shouldBe(Status.Code.FAILED_PRECONDITION))

    private val feeder =
        listFeeder(
            (1..1000).map { mapOf("name" to "name-$it") },
        )

    private val scenario =
        scenario("Hello World Scenario")
            .repeat(1) // option to increase repeats
            .on(
                feed(feeder)
                    .exec(helloWorldSuccess)
                    .exec(badRequestFailure),
            )

    init {
        val totalUsers = 1000
        // hack the plugin to bypass enterprise signup requirements
        GrpcSimUtils.setStaticFinalField(`Usage$`::class.java, "users", AtomicInteger(-totalUsers))
        awaitService("grpcvsrest-service", simulationHost, simulationRestPort)
        println("Running Hello World Scenario for gRPC server @ $simulationHost:$simulationGrpcPort")
        setUp(scenario.injectOpen(atOnceUsers(totalUsers))).protocols(grpcConf)
    }

    companion object {
        /**
         * Manually run the Simulation from here.
         * Ensure to have the docker-compose running - ./gradlew composeUp
         *
         * Run via the command line
         * ./gradlew gatlingRun --simulation com.ribbontek.grpcvsrestdemo.GrpcSimulation
         */
        @JvmStatic
        fun main(args: Array<String>) {
            Gatling.main(arrayOf("-s", GrpcSimulation::class.java.name))
        }
    }
}
