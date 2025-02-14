import React from 'react'

const AdminForm = ({ adminInfo, updateAdmin, deleteAdmin }) => {

    const onUpdate = (e) => {
        e.preventDefault();

        const form = e.target;
        const email = form.email.value;
        const name = form.name.value;
        const password = form.password.value;
        const emailVerified = form.emailVerified.value;

        console.log(email, name, password, emailVerified);

        updateAdmin({email, name, password, emailVerified});
    }

  return (
    <div className="form">
        <h2 className='login-title'>관리자 정보 수정</h2>

        <form className="login-form" onSubmit={(e) => onUpdate(e)}>
            <div>
                <label htmlFor="email">이메일</label>
                <input type="text"
                    id='email'
                    placeholder='이메일 입력'
                    name='email'
                    autoComplete='email'
                    readOnly
                    defaultValue={ adminInfo?.email }
                />
            </div>

            <div>
            <label htmlFor="name">이름</label>
                <input type="text"
                        id='name'
                        placeholder='이름 입력'
                        name='name'
                        autoComplete='name'
                        required
                        defaultValue={ adminInfo?.name }
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
            {/* 이메일 인증여부 추후에 추가 */}
            <input
                type="hidden"
                name="emailVerified"
                value="true"
            />
            <button type='submit' className='btn btn--form btn-login'>정보 수정</button>
            <button type='button' className='btn btn--form btn-login' onClick={() => deleteAdmin(adminInfo.email) }>관리자 탈퇴</button>
        </form>
    </div>
  )
}

export default AdminForm