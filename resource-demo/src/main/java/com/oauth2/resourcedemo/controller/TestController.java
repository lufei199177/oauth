package com.oauth2.resourcedemo.controller;

import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author lufei
 * @date 2020/11/6
 * @desc
 */
@RestController
public class TestController {

    @GetMapping("/hello")
    public String hello(HttpServletRequest request){
        String authorization=request.getHeader("authorization");
        String accessToken=request.getParameter("access_token");
        String tokenStr=(authorization==null||(authorization=authorization.trim()).equals(""))?((accessToken!=null&&!(accessToken=accessToken.trim()).equals(""))?accessToken:null):authorization;
        if(tokenStr==null){
            return "token不能为空";
        }
        String trim=tokenStr.trim().toLowerCase();
        if(trim.indexOf("bearer")!=0){
            return "token无效";
        }
        String token=trim.substring(6);

        return "访问resource成功!";
    }
}
