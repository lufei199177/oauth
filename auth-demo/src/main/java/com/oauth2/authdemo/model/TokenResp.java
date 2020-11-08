package com.oauth2.authdemo.model;

import lombok.Data;

/**
 * @author lufei
 * @date 2020-11-08 14:47
 * @desc
 */
@Data
public class TokenResp {

    private String code;
    private String msg;
    private String access_token;

    public TokenResp(String code, String msg, String access_token) {
        this.code = code;
        this.msg = msg;
        this.access_token = access_token;
    }
}
