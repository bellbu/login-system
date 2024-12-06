package com.example.loginsystem.dto.user.request;

import com.example.loginsystem.domain.user.Admin;
import com.example.loginsystem.domain.user.Authority;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import org.springframework.security.crypto.password.PasswordEncoder;

@Getter
public class AdminCreateRequest {

    @NotBlank(message = "이메일을 입력해주세요.")
    @Email(message = "유효한 이메일 형식이어야 합니다.")
    private String email; // 이메일,

    @NotBlank(message = "이름을 입력해주세요")
    private String name; // 이름

    @NotBlank(message = "비밀번호를 입력해주세요")
    private String password; // 비밀번호

    private Boolean emailVerified; // 이메일 검증 여부

    public Admin toEntity(PasswordEncoder passwordEncoder){
        return Admin.builder()
                .email(email)
                .name(name)
                .password(passwordEncoder.encode(password))
                .authority(Authority.ROLE_ADMIN)
                .emailVerified(emailVerified)
                .build();
    }

}
