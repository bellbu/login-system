import React from 'react'
import Header from '../../component/Header'
import LoginContextConsumer from '../../context/LoginContextConsumer'

// 애플리케이션의 메인 페이지
// LoginContextConsumer: 로그인 상태 표시
const Home = () => {
  return (
    <>
        <Header />
        <div className="container">
            <h1>Home</h1>
            <hr />
            <h2>메인 페이지</h2>
            <LoginContextConsumer /> 
        </div>
    </>    
  )
}

export default Home