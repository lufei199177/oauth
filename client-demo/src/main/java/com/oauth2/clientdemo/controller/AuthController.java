package com.oauth2.clientdemo.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author lufei
 * @date 2020/11/5
 * @desc
 */
@Controller
public class AuthController {

    @Value("${oauth2.client.clientId}")
    private String clientId;
    @Value("${oauth2.client.clientSecret}")
    private String clientSecret;
    @Value("${oauth2.client.redirectUris}")
    private String redirectUri;
    @Value("${oauth2.authServer.authorizationEndpoint}")
    private String authorizationEndpoint;
    @Value("${oauth2.authServer.tokenEndpoint}")
    private String tokenEndpoint;

    @GetMapping("/authorize/{code}")
    public void authorize(@PathVariable String code, HttpServletResponse response) throws IOException {
        StringBuilder sb=new StringBuilder(this.authorizationEndpoint);
        sb.append("?clientId=").append(this.clientId).append("&redirectUri=").append(this.redirectUri);
        response.sendRedirect(sb.toString());
    }

    @GetMapping("/callback")
    public void handleCallback(HttpServletRequest request,HttpServletResponse response){
        String code=request.getParameter("code");

    }

}
