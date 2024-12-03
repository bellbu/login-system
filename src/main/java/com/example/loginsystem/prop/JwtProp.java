package com.example.loginsystem.prop;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data // getter, setter, equals, hashCode, toString 메서드를 자동 생성
@Component
@ConfigurationProperties(prefix = "jwt") // application.yml 파일의 설정 값을 객체에 매핑
public class JwtProp { // JWT 관련 설정

    private String secretKey;  // jwt.secret-key 키와 매핑
}
