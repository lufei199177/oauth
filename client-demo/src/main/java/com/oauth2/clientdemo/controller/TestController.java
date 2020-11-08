package com.oauth2.clientdemo.controller;

import com.alibaba.fastjson.JSONObject;
import com.oauth2.clientdemo.config.HttpSessionConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author lufei
 * @date 2020-11-05 23:03
 * @desc
 */
@RestController
public class TestController {

    @Value("${oauth2.resourceServer.uri}")
    private String resourceServerUri;

    @GetMapping("/test")
    public String hello(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String sessionId=request.getSession().getId();
        if(!HttpSessionConfig.TOKEN_MAP.containsKey(sessionId)){
            HttpSessionConfig.URL_MAP.put(sessionId,"/test");
            response.sendRedirect("/authorize");
            return null;
        }
        JSONObject jsonObject=HttpSessionConfig.TOKEN_MAP.get(sessionId);
        String token=jsonObject.getString("access_token");

        HttpHeaders httpHeaders=new HttpHeaders();
        httpHeaders.add("authorization","bearer "+token);
        HttpEntity httpEntity=new HttpEntity(httpHeaders);

        RestTemplate restTemplate=new RestTemplate();
        ResponseEntity<String> responseEntity = restTemplate.exchange(this.resourceServerUri+"/hello", HttpMethod.GET,
                httpEntity,String.class);
        if(responseEntity!=null){
            return responseEntity.getBody();
        }
        return "访问resource失败!";
    }
}
