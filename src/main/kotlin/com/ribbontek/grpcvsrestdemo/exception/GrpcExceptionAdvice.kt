package com.ribbontek.grpcvsrestdemo.exception

import io.grpc.Status
import jakarta.validation.ConstraintViolationException
import net.devh.boot.grpc.server.advice.GrpcAdvice
import net.devh.boot.grpc.server.advice.GrpcExceptionHandler
import org.slf4j.LoggerFactory

@GrpcAdvice
class GrpcExceptionAdvice {
    private val logger = LoggerFactory.getLogger(this::class.java)

    @GrpcExceptionHandler
    fun handleInvalidArgument(ex: IllegalArgumentException): Status {
        logger.error("Handling IllegalArgumentException", ex)
        return Status.INVALID_ARGUMENT.withCause(ex)
    }

    @GrpcExceptionHandler
    fun handleApiException(ex: ApiException): Status {
        logger.error("Handling ApiException", ex)
        return when (ex.code) {
            404 -> Status.NOT_FOUND.withDescription(ex.message).withCause(ex) // not found
            400 -> Status.FAILED_PRECONDITION.withDescription(ex.message).withCause(ex) // bad request
            409 -> Status.ALREADY_EXISTS.withDescription(ex.message).withCause(ex) // conflict
            else -> Status.INTERNAL.withCause(ex)
        }
    }

    @GrpcExceptionHandler
    fun handleConstraintViolationException(ex: ConstraintViolationException): Status {
        logger.error("Handling ConstraintViolationException", ex)
        val errors = ex.constraintViolations.sortedBy { it.propertyPath.drop(2).toString() }
            .joinToString("; ") {
                it.propertyPath.drop(2)[0].toString() + " " + it.message
            }
        return Status.FAILED_PRECONDITION.withDescription(errors).withCause(ex)
    }

    @GrpcExceptionHandler
    fun handleException(ex: Throwable): Status {
        logger.error("Handling Throwable", ex)
        return Status.INVALID_ARGUMENT.withCause(ex)
    }
}
