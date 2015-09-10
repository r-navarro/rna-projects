package com.carmanagement.controller

import com.carmanagement.config.PersistenceTestConfig
import org.springframework.data.web.PageableHandlerMethodArgumentResolver
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter
import org.springframework.security.authentication.TestingAuthenticationToken
import org.springframework.security.core.authority.AuthorityUtils
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.userdetails.User
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import org.springframework.web.method.HandlerMethod
import org.springframework.web.method.annotation.ExceptionHandlerMethodResolver
import org.springframework.web.servlet.mvc.method.annotation.ExceptionHandlerExceptionResolver
import org.springframework.web.servlet.mvc.method.annotation.ServletInvocableHandlerMethod
import spock.lang.Specification

import java.lang.reflect.Method

@ContextConfiguration(classes = PersistenceTestConfig.class)
@ActiveProfiles("test")
abstract class AbstractControllerTest extends Specification {

    MockMvc mockMvc

    def setupMockMvc(def controller) {
        mockMvc = MockMvcBuilders.standaloneSetup(controller).setHandlerExceptionResolvers(createExceptionResolver())
                .setCustomArgumentResolvers(new PageableHandlerMethodArgumentResolver()).build()
        User user = new User("user", "", AuthorityUtils.createAuthorityList("ROLE_USER"))
        TestingAuthenticationToken testingAuthenticationToken = new TestingAuthenticationToken(user, null)
        SecurityContextHolder.getContext().setAuthentication(testingAuthenticationToken)

    }

    def setupMockMvcAdminUser(def controller) {
        mockMvc = MockMvcBuilders.standaloneSetup(controller).setHandlerExceptionResolvers(createExceptionResolver())
                .setCustomArgumentResolvers(new PageableHandlerMethodArgumentResolver()).build()
        User user = new User("user", "", AuthorityUtils.createAuthorityList("ROLE_ADMIN"))
        TestingAuthenticationToken testingAuthenticationToken = new TestingAuthenticationToken(user, null)
        SecurityContextHolder.getContext().setAuthentication(testingAuthenticationToken)

    }

    private static ExceptionHandlerExceptionResolver createExceptionResolver() {
        ExceptionHandlerExceptionResolver exceptionResolver = new ExceptionHandlerExceptionResolver() {
            protected ServletInvocableHandlerMethod getExceptionHandlerMethod(HandlerMethod handlerMethod, Exception exception) {
                Method method = new ExceptionHandlerMethodResolver(GlobalExceptionHandler).resolveMethod(exception)
                return new ServletInvocableHandlerMethod(new GlobalExceptionHandler(), method)
            }
        }
        exceptionResolver.afterPropertiesSet()
        exceptionResolver.getMessageConverters().add(new MappingJackson2HttpMessageConverter())
        return exceptionResolver
    }
}
