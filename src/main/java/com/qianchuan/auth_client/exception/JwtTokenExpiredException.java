package com.qianchuan.auth_client.exception;

/**
 * @author yanhaopeng
 * @version 1.0
 * @date 2019/9/18 13:45
 */
public class JwtTokenExpiredException extends Exception {
    public JwtTokenExpiredException(String s) {
        super(s);
    }
}
