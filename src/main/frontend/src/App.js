import React, { useState } from 'react';
import logo from './logo.svg';
import './App.css';

function App() {
  let test = 'React Start';
  let [name, setName] =  useState('React Start');
  return (
    <div className="App">
      <header className="App-header">
        <img src={logo} className="App-logo" alt="logo" />
        <p>
          {test}
        </p>
        <a
          className="App-link"
          href="https://reactjs.org"
          target="_blank"
          rel="noopener noreferrer"
        >
          Learn React
        </a>
      </header>
    </div>
  );
}

export default App;
