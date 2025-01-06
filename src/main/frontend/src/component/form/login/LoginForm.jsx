import React, { useContext } from 'react'
import './LoginForm.css'
import { LoginContext } from '../../../context/LoginContextProvider'

const LoginForm = () => {

    const {login} = useContext(LoginContext); // LoginContextProvider에서 생성된 LoginContext 사용

    const onLogin = (e) => { // 사용자가 form을 submit하면 onLogin 함수 호출
        e.preventDefault(); //  기본 이벤트 실행 방지 ※ <submit>태그의 경우 해당 페이지 새로고침 방지, <a>태그의 경우 링크 페이지 이동 방지

        const form = e.target; // 이벤트 객체 e를 통해 form의 이메일과 비밀번호 값을 가져옴
        const email = form.email.value;
        const password = form.password.value;

        // 데이터 세팅
        
        // 로그인 요청
        login(email, password);

    }

    return (
        <div className="form">
            <h2 className='login-title'>로그인</h2>

            <form className="login-form" onSubmit={(e) => onLogin(e)}>
                <div>
                    <label htmlFor="email">이메일</label>
                    <input type="text" 
                           id='email' // label 태그와 연결
                           placeholder='이메일 입력' 
                           name='email' // 폼 제출 시 서버로 전송되는 데이터 키
                           autoComplete='email' // 브라우저 자동완성 기능 활성화
                           required 
                           // TODO: 아이디 저장 기능 구현 후 추가
                           // FIXME: rememberEmail를 넣어줘    
                           // defaultValue={}
                    />
                </div>
                <div>
                    <label htmlFor="password">비밀번호</label>
                    <input type="password"
                           id='password'
                           placeholder='비밀번호 입력'
                           name='password'
                           autoComplete='password'
                           required       
                    />
                </div>
                <button type='submit' className='btn btn--form btn-login'>로그인</button>
            </form>

        </div>
    )
}

export default LoginForm