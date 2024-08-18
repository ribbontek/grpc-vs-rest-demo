import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("org.springframework.boot") version "3.2.3"
    id("io.spring.dependency-management") version "1.1.4"
    kotlin("jvm") version "2.0.10"
    kotlin("plugin.spring") version "2.0.10"
    id("org.jlleitschuh.gradle.ktlint") version "12.1.1"
    id("io.gatling.gradle") version "3.11.5.2"
    id("com.avast.gradle.docker-compose") version "0.14.3"
}

group = "com.ribbontek"
version = "0.0.1-SNAPSHOT"
java.sourceCompatibility = JavaVersion.VERSION_21

val grpcServerVersion = "3.0.0.RELEASE"

repositories {
    mavenCentral()
}

dependencies {
    implementation(project(":grpc-stubs"))
    implementation("org.springframework.boot:spring-boot-starter-web") {
        exclude("org.springframework.boot", "spring-boot-starter-tomcat")
    }
    implementation("org.springframework.boot:spring-boot-starter-undertow")
    implementation("org.springframework.boot:spring-boot-starter-actuator")
    implementation("org.springframework.boot:spring-boot-starter-validation")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
    implementation("net.devh:grpc-server-spring-boot-starter:$grpcServerVersion")

    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("net.devh:grpc-client-spring-boot-starter:$grpcServerVersion")

    gatling(project(":grpc-stubs"))
    gatling("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
    gatlingApi("com.google.protobuf:protobuf-kotlin:3.25.4")
    gatlingImplementation("io.gatling:gatling-grpc-java:3.11.5")

    gatlingImplementation("org.awaitility:awaitility:4.2.1")
    gatlingImplementation("org.springframework:spring-web:5.3.22")
    gatlingImplementation("com.fasterxml.jackson.module:jackson-module-kotlin")
}

tasks.withType<KotlinCompile> {
    kotlinOptions {
        freeCompilerArgs = listOf("-Xjsr305=strict")
        jvmTarget = "21"
    }
}

tasks.withType<Test> {
    useJUnitPlatform()
}

ktlint {
    filter {
        exclude { projectDir.toURI().relativize(it.file.toURI()).path.contains("/generated/") }
    }
}

dockerCompose.isRequiredBy(tasks.gatlingRun)
dockerCompose {
    useComposeFiles.add("$projectDir/dev_docker/docker-compose-loadtest.yml")
}

tasks.jar {
    enabled = false
}

tasks.bootJar {
    archiveFileName.set("${project.name}.jar")
}

gatling {
    // Enterprise Cloud (https://cloud.gatling.io/) configuration reference: https://gatling.io/docs/gatling/reference/current/extensions/gradle_plugin/#working-with-gatling-enterprise-cloud
    // Enterprise Self-Hosted configuration reference: https://gatling.io/docs/gatling/reference/current/extensions/gradle_plugin/#working-with-gatling-enterprise-self-hosted
}
