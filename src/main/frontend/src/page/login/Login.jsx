import React from 'react'
import LoginForm from '../../component/form/login/LoginForm'
import Header from '../../component/Header'

const Login = () => {
  return (
    <>
        <Header />
        <div className="container">
            <LoginForm />
        </div>
    </>   
  )
}

export default Login