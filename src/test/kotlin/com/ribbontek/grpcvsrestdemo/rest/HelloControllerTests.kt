package com.ribbontek.grpcvsrestdemo.rest

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import com.ribbontek.grpcvsrestdemo.grpc.abstract.AbstractIntegTest
import com.ribbontek.grpcvsrestdemo.model.HelloRequestDto
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.CoreMatchers.hasItems
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockHttpServletRequestDsl
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.ResultActionsDsl
import org.springframework.test.web.servlet.post
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import org.springframework.web.context.WebApplicationContext

class HelloControllerTests : AbstractIntegTest() {
    @Autowired
    private lateinit var context: WebApplicationContext

    private lateinit var mockMvc: MockMvc

    @BeforeEach
    fun setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(context).build()
    }

    @Test
    fun `hello world with valid request - success`() {
        mockMvc.post("/hello") {
            withJsonContent(HelloRequestDto(name = "Joel"))
        }
            .andPrint()
            .andStatusIsOk()
            .andExpect {
                jsonPath("$.greeting", equalTo("Hello Joel"))
            }
    }

    @Test
    fun `hello world with invalid request - failure`() {
        mockMvc.post("/hello") {
            withJsonContent(HelloRequestDto(name = ""))
        }
            .andPrint()
            .andStatusIsBadRequest()
            .andExpect {
                jsonPath("$.message", equalTo("Validation Error"))
                jsonPath("$.errors", hasItems("name must not be blank", "name must not be empty"))
            }
    }

    private val objectMapper: ObjectMapper = ObjectMapper().registerKotlinModule()

    private fun Any.toJson(): String = objectMapper.writeValueAsString(this)

    private fun MockHttpServletRequestDsl.withJsonContent(content: Any): MockHttpServletRequestDsl {
        return this.apply {
            this.content = content.toJson()
            this.contentType = MediaType.APPLICATION_JSON
            this.characterEncoding = "utf-8"
        }
    }

    private fun ResultActionsDsl.andPrint(): ResultActionsDsl = this.andDo { print() }

    private fun ResultActionsDsl.andStatusIsOk(): ResultActionsDsl = this.andExpect { status { isOk() } }

    private fun ResultActionsDsl.andStatusIsBadRequest(): ResultActionsDsl = this.andExpect { status { isBadRequest() } }
}
