package com.ribbontek.grpcvsrestdemo

import io.gatling.javaapi.core.Simulation

abstract class AbstractGrpcSim : Simulation() {
    protected val simulationHost: String = System.getenv("SIMULATION_HOST") ?: "localhost"
    protected val simulationGrpcPort: Int = System.getenv("GRPC_SIMULATION_PORT")?.toInt() ?: 9898
    protected val simulationRestPort: Int = System.getenv("REST_SIMULATION_PORT")?.toInt() ?: 8080
    protected val basePath: String = "http://$simulationHost:$simulationRestPort"
}
