import React from 'react'
import Header from '../../component/Header'
import LoginContextConsumer from '../../context/LoginContextConsumer'
import JoinForm from '../../component/form/join/JoinForm'

const Join = () => {
  return (
    <>
        <Header />
        <div className="container">
          <JoinForm />
        </div>
    </>   
  )
}

export default Join