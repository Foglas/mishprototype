package com.fim.prototype.mish.RestInterface

import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.MvcResult
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*

@SpringBootTest
@AutoConfigureMockMvc
abstract class RestRequestProvider {

    @Autowired
    private lateinit var mockMvc: MockMvc
    private val objectMapper = jacksonObjectMapper()
        .registerModule(JavaTimeModule())

    fun sendRequestToApi(method: HttpMethod, baseUrl: String, objectToSend: Any?= null, urlParams: String = "", contentType: String = "application/json"): MvcResult {
        val urlParamsInner = if (urlParams.isNotBlank()) "?$urlParams" else ""

        return when(method){
            HttpMethod.GET -> getRequest(baseUrl, objectToSend, urlParamsInner, contentType)
            HttpMethod.POST -> postRequest(baseUrl, objectToSend, urlParamsInner, contentType)
            HttpMethod.PUT -> putRequest(baseUrl, objectToSend, urlParamsInner, contentType)
            HttpMethod.DELETE -> deleteRequest(baseUrl, objectToSend, urlParamsInner, contentType)
        }
    }

    private fun postRequest(baseUrl: String, objectToSend: Any?, urlParams: String, contentType: String): MvcResult{
        return mockMvc.perform(
            post("$baseUrl$urlParams")
                .content(objectMapper.writeValueAsString(objectToSend?:""))
                .contentType(contentType))
                .andReturn()
    }

    private fun deleteRequest(baseUrl: String, objectToSend: Any?, urlParams: String, contentType: String): MvcResult{
        return mockMvc.perform(
            delete("$baseUrl$urlParams")
                .content(objectMapper.writeValueAsString(objectToSend?:""))
                .contentType(contentType))
                .andReturn()
    }

    private fun getRequest(baseUrl: String, objectToSend: Any?, urlParams: String, contentType: String): MvcResult{
        return mockMvc.perform(
            get("$baseUrl$urlParams")
                .content(objectMapper.writeValueAsString(objectToSend?:""))
                .contentType(contentType))
                .andReturn()
    }

    private fun putRequest(baseUrl: String, objectToSend: Any?, urlParams: String, contentType: String): MvcResult{
        return mockMvc.perform(
            put("$baseUrl$urlParams")
                .content(objectMapper.writeValueAsString(objectToSend?:""))
                .contentType(contentType))
                .andReturn()
    }

    fun <K> MvcResult.deserializeClass(clazz: Class<K>): K {
        return objectMapper.readValue(this.response.contentAsString, clazz)
    }
}



enum class HttpMethod{
    GET, POST, PUT, DELETE
}
