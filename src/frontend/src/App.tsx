import React from "react";
import {
  BrowserRouter as Router,
  Routes,
  Route,
  Navigate,
} from "react-router-dom";
import Login from "./components/Login";
import AdminDashboard from "./components/AdminDashboard";
import TeacherDashboard from "./components/TeacherDashboard";
import StudentDashboard from "./components/StudentDashboard";
import { AuthProvider } from "./context/AuthContext";
import { useAuth } from "./context/AuthContext";

// Create a protected route component
const ProtectedRoute: React.FC<{
  element: React.ReactElement;
  allowedRole: string;
}> = ({ element, allowedRole }) => {
  const { user } = useAuth();

  if (!user) {
    return <Navigate to="/login" />;
  }

  if (user.role !== allowedRole) {
    return <Navigate to="/login" />;
  }

  return element;
};

const App: React.FC = () => {
  return (
    <AuthProvider>
      <Router>
        <Routes>
          <Route path="/" element={<Navigate to="/login" />} />
          <Route path="/login" element={<Login />} />
          <Route
            path="/admin/dashboard"
            element={
              <ProtectedRoute
                element={<AdminDashboard />}
                allowedRole="admin"
              />
            }
          />
          <Route
            path="/teacher/dashboard"
            element={
              <ProtectedRoute
                element={<TeacherDashboard />}
                allowedRole="teacher"
              />
            }
          />
          <Route
            path="/student/dashboard"
            element={
              <ProtectedRoute
                element={<StudentDashboard />}
                allowedRole="student"
              />
            }
          />
        </Routes>
      </Router>
    </AuthProvider>
  );
};

export default App;
