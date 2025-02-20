package com.example.loginsystem.service;

import com.example.loginsystem.domain.admin.Admin;
import com.example.loginsystem.domain.admin.AdminRepository;
import com.example.loginsystem.dto.admin.request.AdminCreateRequest;
import com.example.loginsystem.dto.admin.request.AdminUpdateRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@RequiredArgsConstructor // 의존성 자동 주입(필드 주입이 아닌 생성자 주입)
@Service
public class AdminService {

    private final PasswordEncoder passwordEncoder;
    private final AdminRepository adminRepository;

    // 관리자 등록
    @Transactional
    public int saveAdmin(AdminCreateRequest request) {
        // 유효성 검사
        if (adminRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("이미 가입되어 있는 이메일입니다.");
        }
        if (Boolean.FALSE.equals(request.getEmailVerified())) {
            throw new RuntimeException("이메일 검증이 필요합니다.");
        }

        // 관리자 엔티티 생성
        Admin admin = request.toEntity(passwordEncoder);

        // 회원 등록
        Admin savedAdmin = adminRepository.save(admin);

        // 저장 성공 여부 반환
        return savedAdmin.getId() != null ? 1 : 0;
    }

    // 관리자 정보 수정(이름, 메일검증여부)
    @Transactional
    public int updateAdmin(AdminUpdateRequest request) {
        Admin admin = adminRepository.findByEmail(request.getEmail()) // findById: id를 기준으로 1개의 데이터를 가져옴. Optional<User> 형태로 반환
                .orElseThrow(IllegalArgumentException::new); // Optional의 orElseThrow: 데이터가 없는 경우(해당 id가 없는 경우) 예외를 던짐

        // 비밀번호 암호화 적용
        String encodePassword = passwordEncoder.encode(request.getPassword());

        admin.updateAdmin(request.getName(), encodePassword, request.getEmailVerified()); // 객체 이름 변경

        return 1;
    }

    @Transactional
    public int deleteAdmin(String email) {
        Admin admin = adminRepository.findByEmail(email)
                .orElseThrow(IllegalArgumentException::new);
        adminRepository.delete(admin);

        return 1;
    }

}
