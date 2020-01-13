package com.qianchuan.auth_client.exception;

/**
 * @author yanhaopeng
 * @version 1.0
 * @date 2019/9/18 13:43
 */
public class JwtSignatureException extends Exception {
    public JwtSignatureException(String s) {
        super(s);
    }
}
