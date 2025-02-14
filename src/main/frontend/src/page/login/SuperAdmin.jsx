import React, { useEffect, useContext } from 'react'
import Header from '../../component/Header'
import { LoginContext } from '../../context/LoginContextProvider'
import { useNavigate } from 'react-router-dom';
import * as Swal from '../../api/alert';

const SuperAdmin = () => {

  const { isLogin, adminInfo, authorities } = useContext(LoginContext);
  const navigate = useNavigate();

  useEffect ( () => {

    if (isLogin === null) {
      return; // 아직 로그인 상태 확인 전이면 아무 동작 안 함
    }

    if( !isLogin ) {
      Swal.alert("로그인이 필요합니다.", "로그인 화면으로 이동합니다.", "warning", () => { navigate("/login") });
      /*
      alert(`로그인이 필요합니다.`);
      navigate("/login");
       */
      return;
    }

    if( !authorities.isAdmin ) {
      Swal.alert("권한이 없습니다.", "이전 화면으로 이동합니다.", "warning", () => { navigate(-1) });
      /*
      alert(`권한이 없습니다.`);
      navigate(-1);
      */
      return;
    }

  }, [isLogin]);


  return (
    <>
      {
        isLogin && authorities.isAdmin &&
        <>
          <Header />
          <div className="container">
              <h1>About</h1>
              <hr />
              <h2>최고 관리자 페이지</h2>
              <center>
                <img src="/img/loading.webp" alt="loading" />
              </center>
          </div>
        </>
      }
    </>   
  )
}

export default SuperAdmin