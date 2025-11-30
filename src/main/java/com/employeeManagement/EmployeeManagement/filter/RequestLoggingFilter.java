package com.employeeManagement.EmployeeManagement.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
/*UUID = Universally Unique Identifier
It is a 128-bit value used to uniquely identify information across systems without significant risk of collision.
Usually represented as a string in the format: 550e8400-e29b-41d4-a716-446655440000
Often used for request IDs, transaction IDs, or unique identifiers in databases and distributed systems.*/

import java.util.UUID;
@Component
public class RequestLoggingFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

//        just to generate random id of 16 letters
        String requestId = UUID.randomUUID().toString();

        // Put into MDC :-
/*
* MDC = Mapped Diagnostic Context
It is a key-value store (thread-local) used by logging frameworks like
Logback to store information for the current request/thread.
* Example MDC values: requestId , httpMethod , requestUrl , userId , sessionId
* Every log line produced during that request automatically includes these values.*/

        MDC.put("requestId", requestId);
        MDC.put("httpMethod", request.getMethod());
        MDC.put("requestUrl", request.getRequestURI());
//      MDC.put("userId", springSecurityContext_inst.getUserId());

        try {
            filterChain.doFilter(request, response);
        } finally {
            MDC.clear(); // VERY IMPORTANT
        }
    }
}