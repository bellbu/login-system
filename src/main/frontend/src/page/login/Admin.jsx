import React, { useContext, useEffect, useState } from 'react';
import Header from '../../component/Header';
import * as auth from '../../api/auth'
import AdminForm from '../../component/form/admin/AdminForm';
import { LoginContext } from '../../context/LoginContextProvider';
import { useNavigate } from 'react-router-dom';
import * as Swal from '../../api/alert';

const Admin = () => {

  const { isLogin, loginCheck, authorities, logout } = useContext(LoginContext);
  const [ adminInfo, setAdminInfo ] = useState();
  const navigate = useNavigate();

    console.log("isLogin",isLogin);
    console.log("authorities",authorities);

  // 관리자 정보 조회 - /admin/info
  const getAdminInfo = async () => {
    
    // 비로그인 또는 USER 권한이 없으면 => 로그인 페이지로 이동
    if(!isLogin || !(authorities.isAdmin || authorities.isUser)) {
      navigate("/login");
      return;
    }

    const response = await auth.info();
    const data = response.data;
    console.log('getAdminInfo',data);
    setAdminInfo(data);
  }

  // 관리자 정보 수정
  const updateAdmin = async (form) => {
    console.log(form);

    let response;
    let data;
    try {
      response =  await auth.update(form);
    } catch (error) {
      console.error(`${error}`);
      console.error(`관리자 정보 수정 중 에러가 발생하였습니다.`);
      return;
    }

    data = response.data;
    const status = response.status;
    console.log(`data : ${data}`);
    console.log(`status : ${status}`);

    if(status === 200) {
      console.log(`관리자 정보 수정 성공!`);
      Swal.alert("관리자 정보 수정 성공", "로그아웃 후, 다시 로그인해주세요.", "success", () => { logout(true) });
      /*
      alert(`관리자 정보 수정 성공!`);
      logout();
       */
    } else {
      console.log(`관리자 정보 수정 실패!`);
       Swal.alert("관리자 정보 수정 실패", "회원수정에 실패하였습니다.", "error");
      // alert(`관리자 정보 수정 실패!`);
    }

  }

  // 관리자  탈퇴
  const deleteAdmin = async (email) => {
    console.log(email);

    let response;
    let data;
    try {
      response = await auth.remove(email);
    } catch (error) {
      console.error(`${error}`);
      console.error(`관리자 삭제 중 에러가 발생하였습니다.`);
      return;
    }

    data = response.data;
    const status = response.status;
    console.log(`data : ${data}`);
    console.log(`status : ${status}`);

    if(status === 200) {
      console.log(`관리자 삭제 성공!`);
      Swal.alert("관리자 삭제 성공", "그 동안 감사했습니다.", "success", () => { logout(true) });
      /*
      alert(`관리자 삭제 성공!`);
      logout();
       */
    } else {
      console.log(`관리자 삭제 실패!`);
      Swal.alert("관리자 삭제 실패", "관리자 삭제에 실패하였습니다.", "error");
      //alert(`관리자 삭제 실패!`);
    }


  }

  useEffect( () => {
    if (isLogin === null) {
        return; // 아직 로그인 상태 확인 전이면 아무 동작 안 함
    }
    if (!isLogin) {
      navigate("/login");
      return;
    }
    getAdminInfo();
  }, [isLogin]);

  return (
    <>
        <Header />
        <div className="container">
            <h1>Admin</h1>
            <AdminForm adminInfo={adminInfo} updateAdmin={updateAdmin} deleteAdmin={deleteAdmin} />
        </div>
    </>   
  )
}

export default Admin