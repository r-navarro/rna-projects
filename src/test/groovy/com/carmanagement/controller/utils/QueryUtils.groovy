package com.carmanagement.controller.utils

import groovy.json.JsonSlurper
import org.springframework.http.MediaType
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors
import org.springframework.test.web.servlet.ResultActions
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.request.RequestPostProcessor

trait QueryUtils {

    MockHttpServletRequestBuilder getPostQuery(String url, String content, RequestPostProcessor user) {
        return MockMvcRequestBuilders.post(url)
                .content(content)
                .contentType(MediaType.APPLICATION_JSON)
                .with(user)
                .with(SecurityMockMvcRequestPostProcessors.csrf())
    }

    MockHttpServletRequestBuilder getGetQuery(String url, RequestPostProcessor user) {
        return MockMvcRequestBuilders.get(url)
                .with(user)
                .with(SecurityMockMvcRequestPostProcessors.csrf())
    }

    Object getResponseAsJsonObject(ResultActions result) {
        return new JsonSlurper().parseText(result.andReturn().response.contentAsString)
    }
}
