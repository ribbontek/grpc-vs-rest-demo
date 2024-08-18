package com.ribbontek.grpcvsrestdemo

import com.ribbontek.grpcvsrestdemo.util.ServiceUtil.awaitService
import io.gatling.app.Gatling
import io.gatling.javaapi.core.CoreDsl.StringBody
import io.gatling.javaapi.core.CoreDsl.atOnceUsers
import io.gatling.javaapi.core.CoreDsl.listFeeder
import io.gatling.javaapi.core.CoreDsl.scenario
import io.gatling.javaapi.http.HttpDsl.http
import io.gatling.javaapi.http.HttpDsl.status

class RestSimulation : AbstractGrpcSim() {
    private val successfulRequestCall =
        http("Hello Request Success")
            .post("$basePath/hello")
            .body(StringBody("""{ "name": "#{name}" }"""))
            .asJson()
            .check(status().`is`(200))

    private val badRequestCall =
        http("Hello Request Failure")
            .post("$basePath/hello")
            .body(StringBody("""{ "name": " " }"""))
            .asJson()
            .check(status().`is`(400))

    private val feeder =
        listFeeder(
            (1..1000).map { mapOf("name" to "name-$it") },
        )

    private val scenario =
        scenario("Hello World Scenario")
            .feed(feeder)
            .exec(successfulRequestCall)
            .exec(badRequestCall)

    init {
        awaitService("grpcvsrest-service", simulationHost, simulationRestPort)
        println("Running Hello World Scenario for REST server @ $simulationHost:$simulationRestPort")
        setUp(scenario.injectOpen(atOnceUsers(1000)))
    }

    companion object {
        /**
         * Manually run the Simulation from here.
         * Ensure to have the docker-compose running - ./gradlew composeUp
         *
         * Run via the command line
         * ./gradlew gatlingRun --simulation com.ribbontek.grpcvsrestdemo.RestSimulation
         */
        @JvmStatic
        fun main(args: Array<String>) {
            Gatling.main(arrayOf("-s", RestSimulation::class.java.name))
        }
    }
}
