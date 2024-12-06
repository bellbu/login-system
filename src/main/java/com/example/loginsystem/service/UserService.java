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

        // 아이디, 패스워드 인증토큰 생성
        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(email, password);

        // 토큰에 요청 정보 등록
        token.setDetails(new WebAuthenticationDetails(request));

        // 토큰을 이용하여 인증 요청 - 로그인
        Authentication authentication = authenticationManager.authenticate(token);
        log.info("인증 여부 : " + authentication.isAuthenticated());

        // 인증된 관리자 정보
        User authUser = (User) authentication.getPrincipal();
        log.info("인증된 관리자 아이디 : " );

        // 시큐리티 컨텍스트dp 인증된 사용자 등록
        SecurityContextHolder.getContext().setAuthentication(authentication);

    }
    
}
