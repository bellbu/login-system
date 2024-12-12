package com.example.loginsystem.security.jwt.filter;

import com.example.loginsystem.security.custom.CustomAdmin;
import com.example.loginsystem.security.jwt.constants.JwtConstants;
import com.example.loginsystem.security.jwt.provider.JwtTokenProvider;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

/**
* JwtAuthenticationFilter: JWT 기반 인증 로직을 구현한 커스텀 필터
*
* UsernamePasswordAuthenticationFilter: 사용자 인증 요청(/login)을 처리
*
* client -> JwtAuthenticationFilter("/login") -> server
*                                       ↳ email, password로 인증 시도: attemptAuthentication 메소드 실행
*                                       ↳ 인증 성공 시 successfulAuthentication 메소드 호출 -> JWT 생성 -> response > header > authrization에 JWT 담음
*                                       ↳ 인증 실패 시 response > status에 401 담음 (UNAUTHORIZED)
*/
@Slf4j
public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
    
    private final AuthenticationManager authenticationManager; // 스프링 시큐리티 인증 관리자
    private final JwtTokenProvider jwtTokenProvider; // JWT 토큰 생성, 파싱, 유효성 검사 등을 담당하는 유틸 클래스.

    // 생성자 주입
    public JwtAuthenticationFilter(AuthenticationManager authenticationManager, JwtTokenProvider jwtTokenProvider) {
        this.authenticationManager = authenticationManager;
        this.jwtTokenProvider = jwtTokenProvider;
        
        setFilterProcessesUrl(JwtConstants.AUTH_LOGIN_URL); // "/login" 로그인 경로 요청 시 JwtAuthenticationFilter에서 처리하도록 지정
    }


    /**
     * attemptAuthentication: 인증 시도
     * */
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        String email = request.getParameter("email");
        String password = request.getParameter("password");

        log.info("username : " + email);
        log.info("password : " + password);

        // 이메일과 비밀번호로 사용자 인증 객체 생성
        Authentication authentication = new UsernamePasswordAuthenticationToken(email, password);

        // 사용자 인증 시도: 성공 시 - Authentication 객체 반환 / 실패 시 - 예외 던짐
        authentication = authenticationManager.authenticate(authentication);

        log.info("인증 여부 : " + authentication.isAuthenticated());
        
        // 인증 실패 (username, password 불일치)
        if(!authentication.isAuthenticated()) {
            log.info("인증 실패 : 아이디 또는 비밀번호가 일치하지 않습니다." );
            response.setStatus(401); // 인증 실패 시 HTTP 응답 상태를 401(UNAUTHORIZED)로 설정.
        }

        return authentication;
    }

    /**
     * 인증 성공
     * - JWT를 생성
     * - JWT를 응답 헤더에 설정
     */
    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authentication) throws IOException, ServletException {

        log.info("인증 성공");

        CustomAdmin admin = (CustomAdmin) authentication.getPrincipal();
        int adminNo = Math.toIntExact(admin.getAdmin().getId());
        String email = admin.getAdmin().getEmail();

        // 권한 정보
        List<String> authorities = admin.getAdmin().getAuthorities().stream()
                                .map(Enum::name)
                                .collect(Collectors.toList());

        // JwtTokenProvider를 사용해 사용자 정보를 포함한 JWT를 생성
        String jwt = jwtTokenProvider.createToken(adminNo, email, authorities);
        
        // 응답 설정
        response.addHeader(JwtConstants.TOKEN_HEADER, JwtConstants.TOKEN_PREFIX+ jwt); // 생성한 JWT를 HTTP 응답 헤더(Authorization)에 추가
        response.setStatus(200); // HTTP 응답 상태를 200 (OK)로 설정
    }

}
