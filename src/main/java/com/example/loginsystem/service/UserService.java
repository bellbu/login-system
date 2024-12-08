package com.example.loginsystem.service;

import com.example.loginsystem.domain.user.Admin;
import com.example.loginsystem.domain.user.AdminRepository;
import com.example.loginsystem.dto.user.request.AdminCreateRequest;
import com.example.loginsystem.dto.user.request.AdminLoginRequest;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@RequiredArgsConstructor // 의존성 자동 주입(필드 주입이 아닌 생성자 주입)
@Service
public class UserService {

    private final PasswordEncoder passwordEncoder;
    private final AdminRepository adminRepository;
    private final AuthenticationManager authenticationManager; // 인증 관리하는 객체

    // 관리자 등록
    @Transactional
    public void saveAdmin(AdminCreateRequest request) {
        // 유효성 검사
        if (adminRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("이미 가입되어 있는 유저입니다.");
        }
        if (Boolean.FALSE.equals(request.getEmailVerified())) {
            throw new RuntimeException("이메일 검증이 필요합니다.");
        }

        // 관리자 엔티티 생성
        Admin admin = request.toEntity(passwordEncoder);

        // 회원 등록
        adminRepository.save(admin);

    }

    // 관리자 로그인
    @Transactional
    public void login(AdminLoginRequest admin, HttpServletRequest request) throws Exception {
        String email = admin.getEmail();
        String password = admin.getPassword();
        log.info("email : " + email);
        log.info("password : " + password);

        // 아이디, 패스워드 인증 토큰 생성
        // UsernamePasswordAuthenticationToken: 스프링 시큐리티 인증 객체
        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(email, password);

        // 토큰에 클라이언트 요청 정보 추가
        // WebAuthenticationDetails: HTTP 요청 정보를 기반으로 세부 정보를 설정하는 객체
        token.setDetails(new WebAuthenticationDetails(request));

        // 토큰을 이용하여 인증 처리(성공 시 Authentication 객체 반환 / 실패시 예외 발생)
        Authentication authentication = authenticationManager.authenticate(token); // 인증 매니저가 UsernamePasswordAuthenticationToken을 사용해 사용자 인증을 처리
        log.info("인증 여부 : " + authentication.isAuthenticated());

        // 인증된 사용자 정보 반환
        // User: 스프링 시큐리티 User 객체(아이디, 비밀번호, 권한 등 정보 포함)
        User authUser = (User) authentication.getPrincipal();
        log.info("인증된 관리자 아이디 : " + authUser);

        // 시큐리티 컨텍스트에 인증 정보 저장
        SecurityContextHolder.getContext().setAuthentication(authentication);

    }
    
}
