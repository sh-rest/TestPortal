import React, { useState } from "react";
import api from "../api/axios";
import { useNavigate } from "react-router-dom";
import "../styles/Login.css";

interface LoginFormData {
  email: string;
  password: string;
  userType: "STUDENT" | "TEACHER" | "ADMIN";
}

interface LoginResponse {
  userType: string;
  userId: number;
  redirectUrl: string;
}

const Login: React.FC = () => {
  const [formData, setFormData] = useState<LoginFormData>({
    email: "",
    password: "",
    userType: "STUDENT",
  });
  const [error, setError] = useState<string>("");
  const navigate = useNavigate();

  const handleChange = (
    e: React.ChangeEvent<HTMLInputElement | HTMLSelectElement>
  ) => {
    setFormData({
      ...formData,
      [e.target.name]: e.target.value,
    });
  };

  const handleSubmit = async (e: React.FormEvent<HTMLFormElement>) => {
    e.preventDefault();
    try {
      const response = await api.post<LoginResponse>(
        "/api/auth/login",
        formData
      );
      const { redirectUrl } = response.data;
      navigate(redirectUrl);
    } catch (err) {
      setError("Invalid credentials. Please try again.");
    }
  };

  return (
    <div className="login-container">
      <form onSubmit={handleSubmit} className="login-form">
        <h2>Login to Test Portal</h2>

        {error && <div className="error-message">{error}</div>}

        <div className="form-group">
          <label>User Type:</label>
          <select
            name="userType"
            value={formData.userType}
            onChange={handleChange}
          >
            <option value="STUDENT">Student</option>
            <option value="TEACHER">Teacher</option>
            <option value="ADMIN">Admin</option>
          </select>
        </div>

        <div className="form-group">
          <label>Email:</label>
          <input
            type="email"
            name="email"
            value={formData.email}
            onChange={handleChange}
            required
          />
        </div>

        <div className="form-group">
          <label>Password:</label>
          <input
            type="password"
            name="password"
            value={formData.password}
            onChange={handleChange}
            required
          />
        </div>

        <button type="submit">Login</button>
      </form>
    </div>
  );
};

export default Login;
