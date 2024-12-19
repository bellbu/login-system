import React from 'react'
import Header from '../../component/Header'
import LoginContextConsumer from '../../context/LoginContextConsumer'

const About = () => {
  return (
    <>
        <Header />
        <div className="container">
            <h1>About</h1>
            <hr />
            <h2>소개 페이지</h2>
            <LoginContextConsumer />
        </div>
    </>   
  )
}

export default About