import React, { useState, useEffect } from 'react';
import './App.css';
import { BrowserRouter, Route, Routes } from 'react-router-dom';
import Join from './page/login/Join';
import Login from './page/login/Login';
import Admin from './page/login/Admin';
import About from './page/login/About';
import Home from './page/login/Home';
import LoginContextProvider from './context/LoginContextProvider';

// App.js: 애플리케이션의 메인 컴포넌트
const App = () => {
    return (
        // BrowserRouter: 라우팅을 설정
        // LoginContextProvider: 애플리케이션 전반에 로그인 상태를 공유
        // Routes: 여러 Route를 포함하며, path 속성에 따라 특정 컴포넌트를 렌더링
        // Route: URL 경로(path)와 해당 경로에서 렌더링할 컴포넌트(element)를 매핑
        <BrowserRouter> 
            <LoginContextProvider>
                <Routes>
                    <Route path="/" element={ <Home />}></Route>
                    <Route path="/login" element={ <Login />}></Route>
                    <Route path="/join" element={ <Join />}></Route>
                    <Route path="/admin" element={ <Admin />}></Route>
                    <Route path="/about" element={ <About />}></Route>
                </Routes>
            </LoginContextProvider>
        </BrowserRouter>
    )
}

export default App;