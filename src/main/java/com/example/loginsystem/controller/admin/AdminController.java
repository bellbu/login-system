package com.example.loginsystem.controller.admin;

import com.example.loginsystem.domain.admin.Admin;
import com.example.loginsystem.dto.admin.request.AdminCreateRequest;
import com.example.loginsystem.dto.admin.request.AdminUpdateRequest;
import com.example.loginsystem.security.custom.CustomAdmin;
import com.example.loginsystem.service.AdminService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;


/*
* [GET]     /admin/info - 회원정보 조회   (ROLE_USER)
* [POST]    /admin      - 회원가입        ALL
* [PUT]     /admin      - 회원정보 수정   (ROLE_USER)
* [DELETE]  /admin      - 회원탈퇴       (ROLE_ADMIN)
* */
@Slf4j // log 객체를 자동 생성
@RequiredArgsConstructor // 필드 주입을 생성자 주입으로 자동 설정
@RestController
@RequestMapping("/admin")
public class AdminController { // JWT 토큰 생성 RestController

    private final AdminService adminService;

    /**
     * 관리자 조회
     */
    @Secured({"ROLE_ADMIN", "ROLE_USER"}) // 해당 메서드에 접근할 수 있는 권한 설정
    @GetMapping("/info")
    public ResponseEntity<?> getAdmins(@AuthenticationPrincipal CustomAdmin customAdmin) {
        if (customAdmin == null) {
            throw new IllegalStateException("CustomAdmin is null");
        }
        log.info("::::: customUser :::::");
        log.info("customAdmin : "+ customAdmin);

        Admin admin = customAdmin.getAdmin();
        log.info("admin : " + admin);

        // 인증된 사용자 정보
        if( admin != null )
            return new ResponseEntity<>(admin, HttpStatus.OK);

        // 인증 되지 않음
        return new ResponseEntity<>("UNAUTHORIZED", HttpStatus.UNAUTHORIZED);
    }

    /**
     * 관리자 가입
     */
    @PostMapping("")
    public ResponseEntity<?> saveAdmin(@RequestBody AdminCreateRequest request) throws Exception {
        System.out.println("테스트~~~~~~~");
        log.info("[POST] - /users");
        int result = adminService.saveAdmin(request);

        if( result > 0 ) {
            log.info("관리자 가입 성공! - SUCCESS");
            return new ResponseEntity<>("SUCCESS", HttpStatus.OK);
        }
        else {
            log.info("관리자 가입 실패! - FAIL");
            return new ResponseEntity<>("FAIL", HttpStatus.BAD_REQUEST);
        }
    }


    /**
     * 관리자 정보 수정
     */
    @Secured("ROLE_ADMIN") // ADMIN 권한 설정
    @PutMapping("")
    public ResponseEntity<?> updateAdmin(@RequestBody AdminUpdateRequest request) throws Exception {
        log.info("[PUT] - /users");
        int result = adminService.updateAdmin(request);

        if( result > 0 ) {
            log.info("관리자 수정 성공! - SUCCESS");
            return new ResponseEntity<>("SUCCESS", HttpStatus.OK);
        } else {
            log.info("관리자 수정 실패! - FAIL");
            return new ResponseEntity<>("FAIL", HttpStatus.BAD_REQUEST);
        }
    }


    /**
     * 관리자 탈퇴
     */
    @Secured("ROLE_ADMIN") // Admin 권한 설정
    @DeleteMapping("/{email}")
    public ResponseEntity<?> destroy(@PathVariable("email") String email) throws Exception {
        log.info("[DELETE] - /admin/{email}");

        int result = adminService.deleteAdmin(email);

        if( result > 0 ) {
            log.info("관리자 삭제 성공! - SUCCESS");
            return new ResponseEntity<>("SUCCESS", HttpStatus.OK);
        }
        else {
            log.info("관리자 삭제 실패! - FAIL");
            return new ResponseEntity<>("FAIL", HttpStatus.BAD_REQUEST);
        }

    }


}
