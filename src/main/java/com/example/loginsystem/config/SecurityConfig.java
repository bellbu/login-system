package com.example.loginsystem.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

// SpringSecurity 5.4 이하
// @EnableWebSecurity
// public class SecurityConfig extends WebSecurityConfigurerAdapter { }

@Configuration
@EnableWebSecurity  // Spring Security를 활성화하고 기본 보안 필터 체인을 설정
public class SecurityConfig {

    // SpringSecurity 5.5 이상
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception { // Spring Security의 필터 체인 구성 정의
        // 폼 기반 로그인 비활성화(토큰 기반 인증(JWT)과 같은 방식 사용)
        http.formLogin( (login) -> login.disable() );

        // HTTP 기본 인증 비활성화
        http.httpBasic( (basic) -> basic.disable() );
        
        //CSRF 공격 방어 기능 비활성화(RESTful API와 Stateless 인증에 적합)
        http.csrf ( (csrf) -> csrf.disable() );
        
        // 세션 관리 정책 설정: JWT로 인증하므로 세션 비활성화(SessionCreationPolicy.STATELESS)
        http.sessionManagement( management -> management
                                            .sessionCreationPolicy( SessionCreationPolicy.STATELESS ));

        return http.build(); // 정의한 보안 설정으로 SecurityFilterChain을 빌드하고 반환
    }

}
