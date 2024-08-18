# gRPC vs REST Demo

A simple project demonstrating the performance of gRPC vs REST

Based off the gradle version for gatling grpc official implementation 
https://github.com/gatling/gatling-grpc-demo

### Build the project

- `./gradlew clean build -i`

### Run the Gatling Tests

Run all the gatling tests:
- `./gradlew gatlingRun --all`

Gatling results are available under the results folder in the build directory

### Example Gatling Results

![gRPC Simulation Gatling Result](/example/GrpcSimulation.png "gRPC Simulation Gatling Result")
![REST Simulation Gatling Result](/example/RestSimulation.png "REST Simulation Gatling Result")

