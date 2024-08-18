package com.ribbontek.grpcvsrestdemo.model

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotEmpty

data class HelloRequestDto(
    @get:[
    NotBlank
    NotEmpty
    ] val name: String,
)

data class HelloResponseDto(
    val greeting: String,
)
