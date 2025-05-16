package com.example.lab10.web;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import java.io.IOException;

@WebFilter("/*")
public class MethodFilter implements Filter {

    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest req = (HttpServletRequest) request;
        String method = req.getParameter("_method");

        if (method != null && (method.equalsIgnoreCase("PUT") || method.equalsIgnoreCase("DELETE") || method.equalsIgnoreCase("GET"))) {
            HttpServletRequest wrapper = new HttpMethodRequestWrapper(req, method);
            chain.doFilter(wrapper, response);
        } else {
            chain.doFilter(request, response);
        }
    }
}