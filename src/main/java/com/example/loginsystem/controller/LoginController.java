package com.example.loginsystem.controller;

import com.example.loginsystem.constants.SecurityConstants;
import com.example.loginsystem.domain.AuthenticationRequest;
import com.example.loginsystem.prop.JwtProp;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Slf4j // log 객체를 자동 생성
@RestController
public class LoginController { // JWT 토큰 생성 RestController

    @Autowired // 의존성 주입
    private JwtProp jwtProp; // JWT 관련 설정 클래스

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

        // JWT 생성 준비
        byte[] signinKey = jwtProp.getSecretKey().getBytes();

        // JWT 생성
        String jwt = Jwts.builder() // JWT를 생성하는 빌더 객체 생성
                        .signWith( Keys.hmacShaKeyFor(signinKey) , Jwts.SIG.HS512 ) // 시크릿 키(서명키), HMAC-SHA512 알고리즘 사용하여 토큰에 서명
                        .header().add("type", SecurityConstants.TOKEN_TYPE).and() // JWT 헤더에 타입 정보를 추가
                .expiration(new Date( System.currentTimeMillis() + 1000*60*60*24*5 )) // 토큰의 만료 시간 5일 설정
                .claim("uid", username) // 사용자 이름을 클레임에 추가
                .claim("rol", roles) // 권한 정보를 클레임에 추가
                .compact(); // 최종적으로 JWT를 문자열 생성

        log.info("jwt : " + jwt);

        return new ResponseEntity<String>(jwt, HttpStatus.OK); // JWT 문자열을 HTTP 200(요청 성공) 상태 코드와 함께 클라이언트에게 반환
    }
}
