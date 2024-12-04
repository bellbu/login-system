package com.example.loginsystem.controller;

import com.example.loginsystem.constants.SecurityConstants;
import com.example.loginsystem.domain.AuthenticationRequest;
import com.example.loginsystem.prop.JwtProp;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.net.openssl.ciphers.Authentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.ObjectStreamClass;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Slf4j // log 객체를 자동 생성
@RestController
public class LoginController { // JWT 토큰 생성 RestController

    @Autowired // 의존성 주입
    private JwtProp jwtProp; // JWT 관련 설정 클래스

    // 토큰 생성
    @PostMapping("login")
    public ResponseEntity<?> login(@RequestBody AuthenticationRequest request) { 

        String username = request.getUsername();
        String password = request.getPassword();

        log.info("username : " + username);
        log.info("password : " + password);

        // USER, ADMIN 사용자 권한 부여
        List<String> roles = new ArrayList<>();
        roles.add("ROLE_USER");
        roles.add("ROLE_ADMIN");

        // JWT 비밀키 준비
        byte[] signinKey = jwtProp.getSecretKey().getBytes();

        // JWT 생성
        String jwt = Jwts.builder() // JWT를 생성하는 빌더 객체 생성
                        .signWith( Keys.hmacShaKeyFor(signinKey) , Jwts.SIG.HS512 ) // 시그니처에 사용할 비밀키, 알고리즘 설정
                        .header() // JWT 헤더 설정
                .add("typ", SecurityConstants.TOKEN_TYPE).and() // type: JWT
                .expiration(new Date( System.currentTimeMillis() + 1000*60*60*24*5 )) // 토큰 만료 시간: 5일 설정
                .claim("uid", username) // 사용자 이름을 클레임(페이로드의 한 조각)에 추가
                .claim("rol", roles) // 권한 정보를 클레임(페이로드의 한 조각)에 추가
                .compact(); // 최종적으로 토큰 생성

        log.info("jwt : " + jwt);

        return new ResponseEntity<String>(jwt, HttpStatus.OK); // JWT 문자열을 HTTP 200(요청 성공) 상태 코드와 함께 클라이언트에게 반환
    }

    // 토큰 검증
    @GetMapping("/user/info")
    public ResponseEntity<?> useInfo(@RequestHeader(name="Authorization") String header) { // HTTP 요청 헤더 중 Authorization 값을 받음
        log.info("========header========");
        log.info("Authorization : " + header); // header = Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzUxMiJ9...

        String jwt = header.replace(SecurityConstants.TOKEN_PREFIX, ""); // jwt = eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...

        // JWT 비밀키 준비
        byte[] signingKey = jwtProp.getSecretKey().getBytes();

        // JWT 검증 및 파싱
        Jws<Claims> parsedToken = Jwts.parser() // JwtParser 객체 생성
                                    .setSigningKey( Keys.hmacShaKeyFor(signingKey) ) // 비밀키 설정
                                    .build() // 설정을 적용하여 JwtParser 완성
                                    .parseClaimsJws(jwt); // jwt를 파싱하여 유효성 검증하고 페이로드를 읽음

        log.info("parsedToken : " + parsedToken);

        // uid : user
        String username = parsedToken.getPayload().get("uid").toString();
        log.info("username : " + username);

        // rol : [ROLE_USER, ROLE_ADMIN]
        Claims claims = parsedToken.getPayload();
        Object roles = claims.get("rol");
        log.info("role : " + roles);

        return new ResponseEntity<String>(parsedToken.toString(), HttpStatus.OK);
    }
}
