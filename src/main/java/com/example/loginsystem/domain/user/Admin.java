package com.example.loginsystem.domain.user;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Entity
public class Admin {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // pk 자동 생성
    private Long id = null;

    @Column(nullable = false, unique = true)
    private String email; // 이메일,

    @Column(nullable = false)
    private String name; // 이름

    @Column(nullable = false)
    private String password; // 비밀번호

    @Enumerated(EnumType.STRING)
    private Authority authority; // 권한(관리자, 회원)

    @Column(nullable = false)
    private Boolean emailVerified = false; // 이메일 검증 여부

    @Column(updatable = false)
    private LocalDateTime regDate; // 등록일자

    @Column
    private LocalDateTime visitDate; // 방문일자

    @PrePersist // 엔티티 생성될 때 실행
    public void prePersist() {
        this.regDate = LocalDateTime.now();
    }

    @PrePersist // 엔티티 생성될 때 실행
    public void visitDate() {
        this.visitDate = LocalDateTime.now();
    }

    @Builder
    public Admin(String email, String name, String password, Authority authority, Boolean emailVerified) {
        this.email = email;
        this.name = name;
        this.password = password;
        this.authority = authority;
        this.emailVerified = emailVerified != null ? emailVerified : false;
    }
}
