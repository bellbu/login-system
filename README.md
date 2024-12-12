# JWT X SECURITY

## **<mark>1. 요청 처리 흐름</mark>**

### 1-1. **로그인 요청 (`/login`) : 인증 → 토큰 생성 → 토큰 클라이언트에게 반환**

- **요청 정보**:  
  Body에 `email`과 `password`를 담아 전송

- **처리 과정**:
  1. **`JwtAuthenticationFilter` 실행**  
      - `attemptAuthentication` 메서드 실행  
        → `email`과 `password`로 `UsernamePasswordAuthenticationToken` 객체 생성  
        → `AuthenticationManager`를 통해 인증(`authenticate`) 시도  
  2. **사용자 인증 (`CustomAdminDetailService`)**  
      - `AuthenticationManager`가 `CustomAdminDetailService`의 `loadUserByUsername` 메서드를 호출하여 데이터베이스에서 사용자 조회  
      - 사용자를 찾으면 인증된 사용자 객체(`CustomAdmin`) 반환  
  3. **인증 성공 (`successfulAuthentication`)**  
      - `JwtTokenProvider`를 사용해 JWT 토큰 생성  
      - 생성된 JWT를 응답 헤더(`Authorization`)에 포함하여 클라이언트에 반환  

<br/>

### 1-2. **정보 조회 요청 (`/admin/info`) : 토큰 유효성 검사 → 인증된 토큰만 접근**

- **JWT 역할**:  
  사용자의 인증 상태를 나타내며 이후 요청에서 인증 정보를 전달

- **요청 방식**:  
  `Authorization` 헤더에 발급받은 JWT를 포함하여 요청

- **처리 과정**:
  1. **`JwtRequestFilter` 실행**  
      - 모든 요청에서 `JwtRequestFilter`가 `Authorization` 헤더 검사  
      - `JwtTokenProvider`의 `getAuthentication` 메서드를 호출해 JWT 파싱  
      - `JwtTokenProvider`의 `validateToken` 메서드로 JWT 유효성(만료일) 검사  
      - 유효한 JWT인 경우 사용자 정보를 `SecurityContextHolder`에 저장  
  2. **Controller 호출**  
      - `/admin/info` 요청이 컨트롤러로 전달  
      - `@Secured("ROLE_ADMIN")` 어노테이션으로 ADMIN 권한 확인  
      - 인증된 사용자(`CustomAdmin`)라면 사용자 정보(`Admin` 객체) 반환  
      - 인증 실패 또는 권한 부족 시 **401 (Unauthorized)** 응답  

---

<br/>

## **<mark>2. CustomAdmin 클래스</mark>**

### 2-1. **`CustomAdmin`이 필요한 이유**

- Spring Security가 사용자 인증 및 권한 관리를 수행하는 데 필요한 **사용자 세부 정보**를 담고 있음  
- **역할**: Spring Security의 요구사항(UserDetails)과 Admin 엔티티를 연결하는 중간 객체

<br/>

### 2-2. **`CustomAdmin`의 주요 기능**

- **`UserDetails` 인터페이스 구현**:
  1. **사용자 정보 제공**:
      - Spring Security는 인증된 사용자 정보를 `UserDetails` 객체로 관리하며 `SecurityContext`에 저장
  2. **권한 정보 제공:**
      - 사용자 권한(Role)을 Spring Security에 제공하여 접근 제어 수행
