import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import './LoginSignup.css';

const LoginSignup = () => {
  const [isSignup, setIsSignup] = useState(true);
  const [username, setUsername] = useState('');
  const [email, setEmail] = useState('');
  const [password, setPassword] = useState('');
  const [message, setMessage] = useState('');
  const navigate = useNavigate();

  const handleSubmit = async (e) => {
    e.preventDefault();
    const url = isSignup ? 'http://localhost:5000/signup' : 'http://localhost:5000/login';
    const body = isSignup ? JSON.stringify({ username, email, password }) : JSON.stringify({ username, password });

    try {
      const response = await fetch(url, {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body,
      });
      const data = await response.json();
      setMessage(data.message);

      if (!isSignup && response.ok) {
        navigate('/image-upload');
      }
    } catch (error) {
      console.error("Error:", error);
      setMessage("Something went wrong. Please try again.");
    }
  };

  return (
    <div className="login-signup-container">
      <div className="form-container">
        <h2>{isSignup ? "Create Account" : "Welcome Back!"}</h2>
        <form onSubmit={handleSubmit}>
          <input 
            type="text" 
            placeholder="Username" 
            value={username} 
            onChange={(e) => setUsername(e.target.value)} 
            required 
          />
          {isSignup && (
            <input 
              type="email" 
              placeholder="Email" 
              value={email} 
              onChange={(e) => setEmail(e.target.value)} 
              required 
            />
          )}
          <input 
            type="password" 
            placeholder="Password" 
            value={password} 
            onChange={(e) => setPassword(e.target.value)} 
            required 
          />
          <button type="submit" className="submit-btn">
            {isSignup ? "Sign Up" : "Log In"}
          </button>
        </form>
        <p className="message">{message}</p>
        <button onClick={() => setIsSignup(!isSignup)} className="switch-btn">
          {isSignup ? "Already have an account? Log In" : "New here? Create an account"}
        </button>
      </div>
    </div>
  );
};

export default LoginSignup;
