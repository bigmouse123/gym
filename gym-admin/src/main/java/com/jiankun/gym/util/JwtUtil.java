package com.jiankun.gym.util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @author OfferKing
 * @version 1.0
 * @date 2025/4/17 23:00
 */
public class JwtUtil {

    private static final String KEY = "jiankun";

    //接收业务数据,生成token并返回
    public static String createToken(Map<String, Object> map) {
        return JWT.create()
                .withClaim("claims", map) //有效载荷
                .withExpiresAt(new Date(System.currentTimeMillis() + 24 * 1000 * 60 * 60)) //失效时间
                .sign(Algorithm.HMAC256(KEY)); //秘钥
    }

    //接收token,验证token,并返回业务数据
    public static Map<String, Object> parseToken(String token) {
        return JWT.require(Algorithm.HMAC256(KEY))
                .build()
                .verify(token)
                .getClaim("claims")
                .asMap();
    }

    public static void main(String[] args) {
        Map<String, Object> map = new HashMap<>();
        map.put("name", "zhangsan");
        String token = createToken(map);
        System.out.println(token);

        String token2 = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJjbGFpbXMiOnsibmFtZSI6InpoYW5nc2FuIn0sImV4cCI6MTc0NDkwNTY3Nn0.Q2Q0KDf99Wcwr_xxzaOkAvdn9C2R-3EDkyvCcMD4lHs";
        Map<String, Object> map2 = parseToken(token2);
        System.out.println(map2);

    }
}