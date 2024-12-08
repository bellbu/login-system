package com.example.loginsystem.security.custom;

import com.example.loginsystem.domain.user.Admin;
import com.example.loginsystem.domain.user.AdminRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;

@Slf4j
@RequiredArgsConstructor // 의존성 자동 주입(필드 주입이 아닌 생성자 주입)
@Service
public class CustomUserDetailService implements UserDetailsService {

    private final AdminRepository adminRepository;

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        log.info("login - loadUserByUsername : " + username);
        return adminRepository.findByEmail(username)
                .map(this::createUserDetails)
                .orElseThrow(() -> new UsernameNotFoundException(username + " -> DB에서 관리자를 찾을 수 없습니다."));

    }

    // DB 에 User 값이 존재한다면 UserDetails 객체로 만들어서 리턴
    private UserDetails createUserDetails(Admin admin) {
        GrantedAuthority grantedAuthority = new SimpleGrantedAuthority(admin.getAuthority().toString());
        return new User(
                String.valueOf(admin.getEmail()),
                admin.getPassword(),
                Collections.singleton(grantedAuthority)
        );
    }

}
