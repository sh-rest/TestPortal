import React, { useState } from "react";
import api from "../api/axios";
import { useNavigate } from "react-router-dom";
import { useAuth } from "../context/AuthContext";
import "../styles/Login.css";

interface LoginFormData {
  email: string;
  password: string;
  userType: "STUDENT" | "TEACHER" | "ADMIN";
}

interface LoginResponse {
  userType: string;
  userId: number;
  name: string;
  email: string;
  redirectUrl: string;
}

const Login: React.FC = () => {
  const { login } = useAuth();
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
      const { userType, userId, name, email, redirectUrl } = response.data;

      const userData = {
        role: userType.toLowerCase() as "teacher" | "student" | "admin",
        name,
        email,
        ...(userType === "TEACHER" && { teacherId: userId }),
        ...(userType === "STUDENT" && { studentId: userId }),
        ...(userType === "ADMIN" && { adminId: userId }),
      };

      login(userData);

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
