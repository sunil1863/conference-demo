package com.pluralsight.conferencedemo.filters;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.extern.log4j.Log4j2;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.web.servlet.support.RequestContext;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.bind.DatatypeConverter;
import java.io.IOException;
import java.net.Authenticator;

@Log4j2
@Order(1)
public class LoggingFilter implements Filter{
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        Filter.super.init(filterConfig);
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest)servletRequest;
        HttpServletResponse response = (HttpServletResponse)servletResponse;

        log.info("Received request {} : {}", request.getMethod(), request.getRequestURI());
        if(request.getHeader("Content-Type") == "application/json" &&
           HttpMethod.POST.matches(request.getMethod()) || HttpMethod.PUT.matches(request.getMethod()) || HttpMethod.DELETE.matches(request.getMethod())
          ){
            String str = new String(request.getInputStream().readAllBytes());
            log.info(str);

            //test git
        }

        filterChain.doFilter(request, response);
        log.info("Response sent with type {}", response.getContentType());
    }

    @Override
    public void destroy() {
        Filter.super.destroy();
    }
}
