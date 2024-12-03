package com.example.loginsystem.constants;


/**
 * HTTP
 *     headers : {
 *			Authorization : Bearer ${jwt}
 * 	   }
 */
public class SecurityConstants { // Security 및 JWT 관련된 상수를 별도로 관리하는 클래스

    // JWT 토큰을 전달할 때 사용하는 HTTP 요청 헤더 이름
    public static final String TOKEN_HEADER = "Authorization";

    // 토큰 앞에 붙이는 접두사
    public static final String TOKEN_PREFIX = "Bearer "; // ex) Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...

    // 토큰 유형: JWT
    public static final String TOKEN_TYPE = "JWT";
}
