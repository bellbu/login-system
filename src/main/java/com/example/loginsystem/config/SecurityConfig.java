package com.example.loginsystem.config;

import com.example.loginsystem.security.custom.CustomAdminDetailService;
import com.example.loginsystem.security.jwt.filter.JwtAuthenticationFilter;
import com.example.loginsystem.security.jwt.filter.JwtRequestFilter;
import com.example.loginsystem.security.jwt.provider.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

// SpringSecurity 5.4 이하
// public class SecurityConfig extends WebSecurityConfigurerAdapter { }
@Slf4j
@Configuration
@RequiredArgsConstructor
@EnableWebSecurity  // @EnableWebSecurity: 스프링 시큐리티를 활성화하고 기본 보안 설정을 적용
@EnableMethodSecurity(prePostEnabled = true, securedEnabled = true) // @preAuthorize, @postAuthorize, @Secured 활성화
public class SecurityConfig {

    private final CustomAdminDetailService customAdminDetailService;
    private final JwtTokenProvider jwtTokenProvider;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        log.info("시큐리티 설정...");

        // 폼 기반 로그인 비활성화: RESTful API에서는 JWT 등 Stateless 인증 방식을 사용하며 폼 로그인은 불필요
        http.formLogin((login) -> login.disable());

        // HTTP 기본 인증 비활성화: 보안상의 이유로 대부분 비활성화(Base64로 인코딩)
        http.httpBasic((basic) -> basic.disable());

        // CSRF 공격 방어 기능 비활성화: RESTful API는 주로 Stateless 방식으로 동작하므로 CSRF 방지 불필요
        http.csrf ((csrf) -> csrf.disable());

        // 필터 설정
        http.addFilterAt(new JwtAuthenticationFilter(authenticationManager(http.getSharedObject(AuthenticationConfiguration.class)), jwtTokenProvider), UsernamePasswordAuthenticationFilter.class) // 특정 위치에 커스텀 필터를 추가할 때 사용
                .addFilterBefore(new JwtRequestFilter(jwtTokenProvider), UsernamePasswordAuthenticationFilter.class); // 지정된 필터 앞에 새로운 필터를 추가

        // 인가 설정
        http.authorizeHttpRequests(authorizeRequests -> authorizeRequests
                .requestMatchers(PathRequest.toStaticResources().atCommonLocations()).permitAll()
                .requestMatchers("/").permitAll() // 루트 경로("/") 모든 사용자에게 허용
                .requestMatchers("/login").permitAll()
                .requestMatchers("/user/**").permitAll() //.hasAnyRole("USER", "ADMIN")
                .requestMatchers("/admin/**").permitAll() //.hasAnyRole("ADMIN")
                .anyRequest().authenticated()
        );



        // Custom UserDetailsService 설정
        http.userDetailsService(customAdminDetailService);

        return http.build();
    }

    // 암호화 알고리즘 방식: Bcrypt
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // AuthenticationManager 빈 등록
    private AuthenticationManager authenticationManager;

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

}
