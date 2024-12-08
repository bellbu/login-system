package com.example.loginsystem.config;

import com.example.loginsystem.security.custom.CustomUserDetailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

// SpringSecurity 5.4 이하
// @EnableWebSecurity
// public class SecurityConfig extends WebSecurityConfigurerAdapter { }
@Slf4j
@Configuration
@RequiredArgsConstructor
@EnableWebSecurity  // Spring Security를 활성화하고 기본 보안 필터 체인을 설정
public class SecurityConfig {

    private final CustomUserDetailService customUserDetailService;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception { // Spring Security의 필터 체인 구성 정의
        // 폼 기반 로그인 비활성화(토큰 기반 인증(JWT)과 같은 방식 사용)
        http.formLogin( (login) -> login.disable() );

        // HTTP 기본 인증 비활성화
        http.httpBasic( (basic) -> basic.disable() );
        
        //CSRF 공격 방어 기능 비활성화(RESTful API와 Stateless 인증에 적합)
        http.csrf ( (csrf) -> csrf.disable() );

        // 필터 설정
        http.addFilterAt(null, null)
            .addFilterBefore(null, null);

        // 인가 설정
        http.authorizeHttpRequests(authorizeRequests -> authorizeRequests
                                                        .requestMatchers(PathRequest.toStaticResources().atCommonLocations()).permitAll()
                                                        .requestMatchers("/").permitAll()
                                                        .requestMatchers("/login").permitAll()
                                                        .requestMatchers("/user/**").hasAnyRole("USER", "ADMIN")
                                                        .requestMatchers("/admin/**").hasAnyRole("ADMIN")
                                                        .anyRequest().authenticated()
                                    );

        // 인증 방식 설정
        http.userDetailsService(customUserDetailService);

        return http.build(); // 정의한 보안 설정으로 SecurityFilterChain을 빌드하고 반환
    }

    // 암호화 알고리즘 방식: Bcrypt
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
