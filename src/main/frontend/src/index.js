import React from 'react';
import ReactDOM from 'react-dom/client';
import './index.css';
import App from './App';
import reportWebVitals from './reportWebVitals';

// index.js: 리액트 애플리케이션의 진입점 파일
const root = ReactDOM.createRoot(document.getElementById('root')); // 애플리케이션의 루트를 생성
root.render( // <App /> 컴포넌트를 React의 StrictMode로 감싸 렌더링
  // React.StrictMode: 개발 중에 잠재적인 문제를 감지하고 경고하기 위해 사용
  <React.StrictMode> 
    <App />
  </React.StrictMode>
);

// If you want to start measuring performance in your app, pass a function
// to log results (for example: reportWebVitals(console.log))
// or send to an analytics endpoint. Learn more: https://bit.ly/CRA-vitals
reportWebVitals(); // 성능 측정을 위한 코드로 사용. 현재 파일에서는 호출하지 않음
