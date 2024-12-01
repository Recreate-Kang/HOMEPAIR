package com.home.ssafyhome.auth.utils;

import com.home.ssafyhome.auth.dto.JwtTokenDTO;
import com.home.ssafyhome.auth.model.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import javax.crypto.spec.SecretKeySpec;
import java.time.Instant;
import java.util.Date;

public class JwtUtil {

    private static final String SECRETE_KEY = "SSAFYHOMEkangjaehyunrecreatekangchogunheeabel";

    public static JwtTokenDTO createJwtTokenDTO(User user) {
        Claims claims = Jwts.claims()
                .add("sub", String.valueOf(user.getUserId()))
                .add("role", user.getRole())
                .issuedAt(Date.from(Instant.now()))
                .expiration(Date.from(Instant.now().plusSeconds(6000)))
                .build();
        String serialized = Jwts
                .builder()
                .header()
                .type("JWT")
                .and()
                .claims(claims)
                .signWith(SignatureAlgorithm.HS256, SECRETE_KEY)
                .compact();
        //Claims c = verifyJwtToken(serialized);
        //System.out.println(c.getAudience()+"--" +c.getId()+"--"+c.getSubject());


        return JwtTokenDTO.of(serialized);
    }

    public static Claims verifyJwtToken(String token) {
        Claims claims = Jwts.parser()
                .verifyWith(new SecretKeySpec(SECRETE_KEY.getBytes(), "HS256"))
                .build()
                .parseClaimsJws(token)
                .getBody();

        return claims;
    }
}
