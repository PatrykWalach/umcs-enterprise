package com.umcs.enterprise.auth;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.netflix.graphql.types.errors.TypedGraphQLError;
import graphql.ExecutionResultImpl;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;

import java.io.IOException;

@Configuration
public class OAuth2AccessDeniedHandler implements AccessDeniedHandler {

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {
        ServletOutputStream out = response.getOutputStream();

        new ObjectMapper()
                .writeValue(out, ExecutionResultImpl
                        .newExecutionResult()
                        .addError(TypedGraphQLError
                                .newPermissionDeniedBuilder()
                                .message(accessDeniedException
                                        .getLocalizedMessage())
                                .build())
                .build());

        out.flush();
    }
}
