import React, { createContext, useEffect, useState } from 'react';
import api from '../api/api';
import Cookies from 'js-cookie';
import * as auth from '../api/auth';
import { useNavigate } from 'react-router-dom'

export const LoginContext = createContext(); // 새로운 Context 생성하여 로그인 상태(isLogin)와 로그아웃 함수(logout)를 전달 
                                             // LoginContext.Provider를 통해 값을 전달, 자식 컴포넌트에서 useContext(LoginContext)를 사용해 값을 가져옴
LoginContext.displayName = 'LoginContextName' // displayName: 컨텍스트를 디버깅할 때 표시되는 이름 지정(기본적으로 Context로 표시)

/**       
 * 로그인 상태(isLogin)와 로그아웃 함수(logout)를 제공하는 컨텍스트 제공자 
 * - 로그인 체크
 * - 로그인
 * - 로그 아웃
 * 
 * 로그인 세팅
 * 로그아웃 세팅
 */
const LoginContextProvider = ({children}) => {
    
    /**
     * 상태
     * - 로그인 여부 
     * - 유저 정보
     * - 권한 정보
     * - 아이디 저장 여부(쿠키)
     */

    /*----------------------------[State]--------------------------- */
    // 로그인 여부
    const [isLogin, setLogin] = useState(false); // 초기 로그아웃 상태(false)

    // 관리자 정보
    const [adminInfo, setAdminInfo] = useState({});

    // 권한 정보
    const [authorities, setAuthorities] = useState({isUser : false, isAdmin : false});

    // 이메일 저장
    const [rememberEmail, setRememberEmail] = useState();
    /*-------------------------------------------------------------- */

    // 페이지 이동
    const navigate = useNavigate()

    /**
     * 로그인 체크
     * - 쿠키에 jwt가 있는지 확인
     * - jwt로 사용자 정보를 요청
     * - 
     */
    const loginCheck = async () => {

        // 쿠키에서 jwt 토큰 가져오기
        const accessToken = Cookies.get("accessToken");
        console.log(`accessToken : ${accessToken}`);

        // header에 jwt 담기

        // accessToken (jwt)이 없음
        if(!accessToken) {
            console.log(`쿠키에 accessToken(jwt)이 없음`)
            // 로그아웃 세팅
            logoutSetting();
            return;
        }

        // accessToken (jwt)이 있는 경우 header에 jwt 담기
         api.defaults.headers.common.Authorization = `Bearer ${accessToken}`

        // 사용자 정보 요청청
        let response
        let data
        try {
            response = await auth.info();
        } catch (error) {
            console.log(`error : ${error}`);
            console.log(`status : ${response.status}`);
            return;
        }

        data = response.data;
        console.log(`data : ${data}`);

        // 인증 실패
        if(data == 'UNAUTHRIZED' || response.status == 401) {
            console.error(`accessToken (jwt)이 만료되었거나 인증에 실패하였습니다.`);
            return;
        }

        // 인증 성공
        console.log(`accessToken (jwt) 토큰으로 사용자 인증정보 요청 성공!`)

        // 로그인 세팅
        loginSetting(data, accessToken);

    }


    // 로그인
    const login = async(email, password) => {
       
        console.log(`email : ${email}`);
        console.log(`password : ${password}`);

        try {
            const response = await auth.login(email, password);
            const data = response.data;
            const status = response.status;
            const headers = response.headers;
            const authorization = headers.authorization;
            const accessToken = authorization.replace("Bearer ", ""); // jwt

            console.log(`data : ${data}`);
            console.log(`status : ${status}`);
            console.log(`headers : ${headers}`);
            console.log(`jwt : ${accessToken}`);

            // 로그인 성공
            if(status === 200) {
                // 쿠키에 accessToken(jwt) 저장 
                Cookies.set("accessToken", accessToken);

                // 로그인 체크 (/users/{email} => userData)
                loginCheck();

                alert('로그인 성공');

                navigate("/");
            }
        } catch (error) {
            // 로그인 실패
            // - 아이디 또는 비밀번호가 일치하지 않습니다.
            alert('로그인 실패!');
        }
    
    }

    // 로그아웃
    const logout = () => {
        const check = window.confirm(`로그아웃하시겠습니까?`)
        if(check){
            // 로그아웃 세팅
            logoutSetting();

            // 메인 페이지로 이동
            navigate("/");
        }
    }

    // 로그인 세팅
    // adminData(관리자 정보), accessToken(jwt)
    const loginSetting = (adminData, accessToken) => {
        
        const {id, email, authorities} = adminData;
        // 필요없음
        // const roleList = authorities.map((auth) => auth.authorities);
        
        console.log(`no : ${id}`);
        console.log(`email : ${email}`);
        console.log(`authorities : ${authorities}`);

        // 필요없음
        // console.log(`roleList : ${roleList}`);
        
        // axios 객체의 header - Authorization : `Bearer ${accessToken}`
        api.defaults.headers.common.Authorization = `Bearer ${accessToken}`;
        
        // 로그인 여부 true 세팅
        setLogin(true);    

        // 유저 정보 세팅
        const updatedAdminInfo = {id, email, authorities};
        setAdminInfo(updatedAdminInfo);

        // 권한 정보 세팅
        const updatedAuthorities = {isUser : false, isAdmin : false};
        
        authorities.forEach((role) => {
            if(role == 'ROLE_USER') updatedAuthorities.isUser = true;
            if(role == 'ROLE_ADMIN') updatedAuthorities.isAdmin = true;
        });
        setAuthorities(updatedAuthorities);
    }


    // 로그아웃 세팅
    const logoutSetting = () => {
        
        // axios 헤더 초기화
        api.defaults.headers.common.Authorization = undefined;

        // 쿠키 초기화
        Cookies.remove("accessToken");
        
        // 로그인 여부 : false
        setLogin(false);

        // 유저 정보 초기화
        setAdminInfo(null);

        // 권한 정부 초기화
        setAuthorities(null);

    }


    // useEffect를 사용해 3초 뒤에 로그인 상태로 변경
    useEffect( () => {
     
    }, [])

    return (
        // LoginContext.Provider를 사용해 value 속성 값을 Context에 전달
        // LoginContextProvider의 자식 컴포넌트들이 Context 값을 사용할 수 있음
        <LoginContext.Provider value={ {isLogin, adminInfo, authorities, login, logout} }> 
            {children}  
        </LoginContext.Provider>
    )
}

export default LoginContextProvider