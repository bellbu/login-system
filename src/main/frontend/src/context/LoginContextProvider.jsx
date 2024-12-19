import React, { createContext, useEffect, useState } from 'react';

export const LoginContext = createContext(); // 새로운 Context 생성하여 로그인 상태(isLogin)와 로그아웃 함수(logout)를 전달 
                                             // LoginContext.Provider를 통해 값을 전달, 자식 컴포넌트에서 useContext(LoginContext)를 사용해 값을 가져옴
LoginContext.displayName = 'LoginContextName' // displayName: 컨텍스트를 디버깅할 때 표시되는 이름 지정(기본적으로 Context로 표시)

// 로그인 상태(isLogin)와 로그아웃 함수(logout)를 제공하는 컨텍스트 제공자
const LoginContextProvider = ({children}) => {
    // context value: 로그인 여부, 로그아웃 함수
    const [isLogin, setLogin] = useState(false); // 초기 로그아웃 상태(false)

    const logout = () => {
        setLogin(false)
    }

    // useEffect를 사용해 3초 뒤에 로그인 상태로 변경
    useEffect( () => {
     
    }, [])

    return (
        // LoginContext.Provider를 사용해 value 속성 값을 Context에 전달
        // LoginContextProvider의 자식 컴포넌트들이 Context 값을 사용할 수 있음
        <LoginContext.Provider value={ {isLogin, logout} }> 
            {children}  
        </LoginContext.Provider>
    )
}

export default LoginContextProvider