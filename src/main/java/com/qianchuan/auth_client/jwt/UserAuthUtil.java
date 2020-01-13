package com.qianchuan.auth_client.jwt;

import com.qianchuan.auth_client.config.UserAuthConfig;
import com.qianchuan.commons.admincommons.exception.auth.UserTokenException;
import com.qianchuan.commons.authcommons.util.jwt.IJWTInfo;
import com.qianchuan.commons.authcommons.util.jwt.JWTHelper;
import com.qianchuan.commons.commons.exception.CustomInternalException;
import com.qianchuan.commons.commons.exception.ErrorCode;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.SignatureException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

/**
 * @author yanhaopeng
 * @version 1.0
 * @date 2019/9/18 13:36
 */
@Configuration
public class UserAuthUtil {
    @Autowired
    private UserAuthConfig userAuthConfig;

    public IJWTInfo getInfoFromToken(String token) throws Exception {
        try {
            return JWTHelper.getInfoFromToken(token, userAuthConfig.getPubKeyByte());
        } catch (ExpiredJwtException ex) {
            throw new CustomInternalException(ErrorCode.UserTokenExpired, "User token expired!");
        } catch (SignatureException ex) {
            throw new CustomInternalException(ErrorCode.UserTokenSignatureError, "User token signature error!");
        } catch (IllegalArgumentException ex) {
            throw new CustomInternalException(ErrorCode.UserTokenEmpty, "User token is null or empty!");
        }
    }
}
