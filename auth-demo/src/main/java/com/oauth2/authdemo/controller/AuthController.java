package com.oauth2.authdemo.controller;

import com.oauth2.authdemo.config.HttpSessionConfig;
import com.oauth2.authdemo.model.TokenResp;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.UUID;

/**
 * @author lufei
 * @date 2020-11-08 13:47
 * @desc
 */
@RestController
@RequestMapping("/login/oauth")
public class AuthController {

    @Value("${oauth2.client.clientId}")
    private String clientId;
    @Value("${oauth2.client.clientSecret}")
    private String clientSecret;
    @Value("${oauth2.client.redirectUri}")
    private String redirectUri;
    @Value("${oauth2.client.grantType}")
    private String grantType;

    @GetMapping("/authorize")
    public String authorize(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        String sessionId=request.getRequestedSessionId();

        String responseType=request.getParameter("response_type");
        String clientId=request.getParameter("client_id");
        String redirectUri=request.getParameter("redirect_uri");
        if(!this.clientId.equals(clientId)){
            return "clientId错误";
        }
        if(!this.redirectUri.equals(redirectUri)){
            return "redirectUri错误";
        }
        //生成code
        String code= UUID.randomUUID().toString().replaceAll("-","");
        HttpSessionConfig.AUTH_CODE_MAP.put(sessionId,code);
        //重定向
        String url=redirectUri+"?code="+code;
        response.sendRedirect(url);
        return null;
    }

    @PostMapping("/access_token")
    @ResponseBody
    public TokenResp accessToken(HttpServletRequest request){
        String sessionId=request.getRequestedSessionId();
        String grantType=request.getParameter("grant_type");
        String code=request.getParameter("code");
        String redirectUri=request.getParameter("redirect_uri");
        if(StringUtils.isEmpty(code)){
            return new TokenResp("-1","code不能为空",null);
        }
        String sessionCode=HttpSessionConfig.AUTH_CODE_MAP.get(sessionId);
        if(!code.equals(sessionCode)){
            return new TokenResp("-1","code错误",null);
        }
        String header=request.getHeader("authorization");

        //生成token
        String token=UUID.randomUUID().toString().replaceAll("-","");
        return new TokenResp("200","授权成功",token);
    }
}
