package com.example.lab10.web;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletRequestWrapper;

public class HttpMethodRequestWrapper extends HttpServletRequestWrapper {
    private final String method;

    public HttpMethodRequestWrapper(HttpServletRequest request, String method) {
        super(request);
        this.method = method.toUpperCase();
    }

    @Override
    public String getMethod() {
        return this.method;
    }
}