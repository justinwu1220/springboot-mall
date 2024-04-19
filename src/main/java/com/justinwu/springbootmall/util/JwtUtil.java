package com.justinwu.springbootmall.util;

import com.justinwu.springbootmall.model.User;
import io.jsonwebtoken.*;

import java.util.Date;

public class JwtUtil {
    //private static long time = 1000 * 60 * 60 * 1; //1小時
    private static long time = 1000 * 60;
    private static String sign = "JWTTokenSignatureForSpringBootMallProjectCreatedByJustinWu";
    public static String createJwtToken(User user){

        JwtBuilder jwtBuilder = Jwts.builder();

        String jwtToken = jwtBuilder
                //Header
                .setHeaderParam("typ", "JWT")
                .setHeaderParam("alg", "HS256")
                //Payload
                .setId(user.getUserId().toString())
                .claim("email",user.getEmail())
                .setExpiration( new Date(System.currentTimeMillis() + time)) //token存活時間
                //Signature
                .signWith(SignatureAlgorithm.HS256, sign) //設定加密算法和簽名
                //連接成字串
                .compact();

        return jwtToken;
    }

    public static boolean checkJwtToken(String token){

        if(token == null){
            return false;
        }
        try {
            Jws<Claims> claimsJws = Jwts.parser().setSigningKey(sign).parseClaimsJws(token);
        } catch (Exception e){
            e.printStackTrace();
            return false;
        }
        return true;
    }
}
