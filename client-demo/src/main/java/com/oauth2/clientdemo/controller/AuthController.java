package com.oauth2.clientdemo.controller;

import com.alibaba.fastjson.JSONObject;
import com.oauth2.clientdemo.config.HttpSessionConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author lufei
 * @date 2020/11/5
 * @desc
 */
@RestController
public class AuthController {

    @Value("${oauth2.client.clientId}")
    private String clientId;
    @Value("${oauth2.client.clientSecret}")
    private String clientSecret;
    @Value("${oauth2.client.redirectUri}")
    private String redirectUri;
    @Value("${oauth2.client.grantType}")
    private String grantType;
    @Value("${oauth2.authServer.authorizationEndpoint}")
    private String authorizationEndpoint;
    @Value("${oauth2.authServer.tokenEndpoint}")
    private String tokenEndpoint;

    @GetMapping("/authorize")
    public void authorize(HttpServletResponse response) throws IOException {
        String sb = this.authorizationEndpoint + "?response_type=code" + "&client_id=" + this.clientId +
                "&redirect_uri=" + this.redirectUri;
        response.sendRedirect(sb);
    }

    @GetMapping("/login/oauth2/code/github")
    public void handleCallback(HttpServletRequest request,HttpServletResponse response) throws IOException {
        String code=request.getParameter("code");
        MultiValueMap<String, String> params= new LinkedMultiValueMap<>();
        params.add("grant_type",this.grantType);
        params.add("code",code);
        params.add("redirect_uri",this.redirectUri);

        HttpHeaders httpHeaders=new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        httpHeaders.setBasicAuth(this.clientId,this.clientSecret);
        String cookie=request.getHeader("Cookie");
        httpHeaders.add("Cookie",cookie);

        HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(params, httpHeaders);
        //执行HTTP请求
        RestTemplate restTemplate=new RestTemplate();
        ResponseEntity<JSONObject> result = restTemplate.exchange(this.tokenEndpoint, HttpMethod.POST, requestEntity, JSONObject.class);
        if(result!=null){
            HttpSessionConfig.TOKEN_MAP.put(request.getSession().getId(),result.getBody());
            response.sendRedirect(HttpSessionConfig.URL_MAP.get(request.getSession().getId()));
        }else{
            response.sendRedirect("/authFail");
        }
    }

    @GetMapping("/authFail")
    public String authFail(){
        return "授权失败!";
    }

}
