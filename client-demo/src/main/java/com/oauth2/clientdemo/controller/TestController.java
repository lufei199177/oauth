package com.oauth2.clientdemo.controller;

import com.oauth2.clientdemo.config.HttpSessionConfig;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

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

    @GetMapping("/hello")
    public String hello(HttpServletRequest request, HttpServletResponse response) throws IOException {
        if(!HttpSessionConfig.TOKEN_MAP.containsKey(request.getSession().getId())){
            HttpSessionConfig.URL_MAP.put(request.getSession().getId(),"/hello");
            response.sendRedirect("/authorize");
        }
        return "hello,world!";
    }
}
