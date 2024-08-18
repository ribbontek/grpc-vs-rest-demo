package com.ribbontek.grpcvsrestdemo.exception

import jakarta.validation.ConstraintViolationException
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler

data class ApiException(
    val code: Int = HttpStatus.INTERNAL_SERVER_ERROR.value(),
    override val message: String = "Something went wrong!",
    val errors: List<String> = emptyList(),
) : Throwable()

@ControllerAdvice
class GlobalExceptionHandler : ResponseEntityExceptionHandler() {
    private val log: Logger = LoggerFactory.getLogger(this::class.java)

    @ExceptionHandler
    fun handleException(ex: Exception): ResponseEntity<ApiException> {
        log.error("Unhandled exception", ex)
        return ApiException().toApiExceptionResponseEntity()
    }

    @ExceptionHandler
    fun handleConstraintViolationException(ex: ConstraintViolationException): ResponseEntity<ApiException> {
        log.error("Handling ConstraintViolationException", ex)
        return ResponseEntity(
            ApiException(
                message = "Validation Error",
                errors =
                    ex.constraintViolations
                        .sortedBy { it.propertyPath.drop(2).toString() }
                        .map { it.propertyPath.drop(2)[0].toString() + " " + it.message },
            ),
            HttpStatus.BAD_REQUEST,
        )
    }

    private fun ApiException.toApiExceptionResponseEntity(): ResponseEntity<ApiException> =
        ResponseEntity(this, HttpStatus.valueOf(this.code))
}
