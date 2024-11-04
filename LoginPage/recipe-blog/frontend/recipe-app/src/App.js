// src/App.js
import React from 'react';
import { BrowserRouter as Router, Route, Routes } from 'react-router-dom';
import Main from './main'; // Import the Main component
import LoginSignup from './LoginSignup'; // Import the LoginSignup component

const App = () => {
  return (
    <Router>
      <Routes>
        <Route path="/" element={<Main />} />
        <Route path="/login-signup" element={<LoginSignup />} />
      </Routes>
    </Router>
  );
};

export default App;
