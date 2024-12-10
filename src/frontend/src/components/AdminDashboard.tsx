import React, { useState } from "react";
import api from "../api/axios";
import "../styles/AdminDashboard.css";

interface Student {
  id?: number;
  name: string;
  email: string;
  password: string;
}

interface Teacher {
  id?: number;
  name: string;
  email: string;
  password: string;
}

interface Course {
  id?: number;
  courseName: string;
  description: string;
  teacherId: number;
}

const AdminDashboard: React.FC = () => {
  // State for forms
  const [student, setStudent] = useState<Student>({
    name: "",
    email: "",
    password: "",
  });
  const [teacher, setTeacher] = useState<Teacher>({
    name: "",
    email: "",
    password: "",
  });
  const [course, setCourse] = useState<Course>({
    courseName: "",
    description: "",
    teacherId: 0,
  });
  const [message, setMessage] = useState<string>("");

  // Add state for visibility
  const [visibleSection, setVisibleSection] = useState<string | null>(null);

  // Student Management
  const handleAddStudent = async (e: React.FormEvent) => {
    e.preventDefault();
    try {
      await api.post("/api/admin/students", student);
      setMessage("Student added successfully");
      setStudent({ name: "", email: "", password: "" });
      setVisibleSection(null); // Close the form
    } catch (error) {
      setMessage("Error adding student");
    }
  };

  const handleUpdateStudent = async (id: number) => {
    try {
      await api.put(`/api/admin/students/${id}`, student);
      setMessage("Student updated successfully");
    } catch (error) {
      setMessage("Error updating student");
    }
  };

  const handleDeleteStudent = async (id: number) => {
    try {
      await api.delete(`/api/admin/students/${id}`);
      setMessage("Student deleted successfully");
    } catch (error) {
      setMessage("Error deleting student");
    }
  };

  // Teacher Management
  const handleAddTeacher = async (e: React.FormEvent) => {
    e.preventDefault();
    try {
      await api.post("/api/admin/teachers", teacher);
      setMessage("Teacher added successfully");
      setTeacher({ name: "", email: "", password: "" });
      setVisibleSection(null); // Close the form
    } catch (error) {
      setMessage("Error adding teacher");
    }
  };

  const handleUpdateTeacher = async (id: number) => {
    try {
      await api.put(`/api/admin/teachers/${id}`, teacher);
      setMessage("Teacher updated successfully");
    } catch (error) {
      setMessage("Error updating teacher");
    }
  };

  const handleDeleteTeacher = async (id: number) => {
    try {
      await api.delete(`/api/admin/teachers/${id}`);
      setMessage("Teacher deleted successfully");
    } catch (error) {
      setMessage("Error deleting teacher");
    }
  };

  // Course Management
  const handleAddCourse = async (e: React.FormEvent) => {
    e.preventDefault();
    try {
      // Validate teacherId
      if (!course.teacherId || course.teacherId <= 0) {
        setMessage("Please enter a valid Teacher ID");
        return;
      }

      // Validate other fields
      if (!course.courseName.trim() || !course.description.trim()) {
        setMessage("Please fill in all fields");
        return;
      }

      // First verify if teacher exists
      try {
        await api.get(`/api/admin/teachers/${course.teacherId}`);
      } catch (error) {
        setMessage(
          "Teacher ID does not exist. Please enter a valid Teacher ID."
        );
        return;
      }

      const response = await api.post("/api/admin/courses", course);

      if (response.status === 200 || response.status === 201) {
        setMessage("Course added successfully");
        setCourse({ courseName: "", description: "", teacherId: 0 });
        setVisibleSection(null);
      } else {
        throw new Error("Failed to add course");
      }
    } catch (error: any) {
      console.error("Error adding course:", error);
      if (error.response?.status === 404) {
        setMessage("Teacher not found. Please enter a valid Teacher ID.");
      } else {
        setMessage(
          error.response?.data?.message ||
            "Error adding course. Please check if the Teacher ID exists."
        );
      }
    }
  };

  const handleAssignTeacher = async (courseId: number, teacherId: number) => {
    try {
      await api.put(`/api/admin/courses/${courseId}/teachers/${teacherId}`);
      setMessage("Teacher assigned to course successfully");
    } catch (error) {
      setMessage("Error assigning teacher to course");
    }
  };

  // Enrollment Management
  const handleEnrollStudent = async (studentId: number, courseId: number) => {
    try {
      await api.post(
        `/api/admin/enrollments/students/${studentId}/courses/${courseId}`
      );
      setMessage("Student enrolled successfully");
    } catch (error) {
      setMessage("Error enrolling student");
    }
  };

  const handleRemoveStudentFromCourse = async (
    studentId: number,
    courseId: number
  ) => {
    try {
      await api.delete(
        `/api/admin/enrollments/students/${studentId}/courses/${courseId}`
      );
      setMessage("Student removed from course successfully");
    } catch (error) {
      setMessage("Error removing student from course");
    }
  };

  return (
    <div className="admin-dashboard">
      <h1>Admin Dashboard</h1>
      {message && <div className="message">{message}</div>}

      <div className="dashboard-buttons">
        <button onClick={() => setVisibleSection("student")}>
          Manage Students
        </button>
        <button onClick={() => setVisibleSection("teacher")}>
          Manage Teachers
        </button>
        <button onClick={() => setVisibleSection("course")}>
          Manage Courses
        </button>
        <button onClick={() => setVisibleSection("enrollment")}>
          Manage Enrollments
        </button>
      </div>

      {visibleSection === "student" && (
        <div className="dashboard-section">
          <h2>Student Management</h2>
          <form onSubmit={handleAddStudent}>
            <input
              type="text"
              placeholder="Name"
              value={student.name}
              onChange={(e) => setStudent({ ...student, name: e.target.value })}
            />
            <input
              type="email"
              placeholder="Email"
              value={student.email}
              onChange={(e) =>
                setStudent({ ...student, email: e.target.value })
              }
            />
            <input
              type="password"
              placeholder="Password"
              value={student.password}
              onChange={(e) =>
                setStudent({ ...student, password: e.target.value })
              }
            />
            <div className="form-buttons">
              <button type="submit">Add Student</button>
              <button type="button" onClick={() => setVisibleSection(null)}>
                Cancel
              </button>
            </div>
          </form>
        </div>
      )}

      {visibleSection === "teacher" && (
        <div className="dashboard-section">
          <h2>Teacher Management</h2>
          <form onSubmit={handleAddTeacher}>
            <input
              type="text"
              placeholder="Name"
              value={teacher.name}
              onChange={(e) => setTeacher({ ...teacher, name: e.target.value })}
            />
            <input
              type="email"
              placeholder="Email"
              value={teacher.email}
              onChange={(e) =>
                setTeacher({ ...teacher, email: e.target.value })
              }
            />
            <input
              type="password"
              placeholder="Password"
              value={teacher.password}
              onChange={(e) =>
                setTeacher({ ...teacher, password: e.target.value })
              }
            />
            <div className="form-buttons">
              <button type="submit">Add Teacher</button>
              <button type="button" onClick={() => setVisibleSection(null)}>
                Cancel
              </button>
            </div>
          </form>
        </div>
      )}

      {visibleSection === "course" && (
        <div className="dashboard-section">
          <h2>Course Management</h2>
          <form onSubmit={handleAddCourse}>
            <input
              type="text"
              placeholder="Course Name"
              value={course.courseName}
              required
              onChange={(e) =>
                setCourse({ ...course, courseName: e.target.value })
              }
            />
            <input
              type="text"
              placeholder="Description"
              value={course.description}
              required
              onChange={(e) =>
                setCourse({ ...course, description: e.target.value })
              }
            />
            <input
              type="number"
              placeholder="Teacher ID"
              value={course.teacherId || ""}
              required
              min="1"
              onChange={(e) =>
                setCourse({
                  ...course,
                  teacherId: parseInt(e.target.value) || 0,
                })
              }
            />
            <div className="form-buttons">
              <button type="submit">Add Course</button>
              <button type="button" onClick={() => setVisibleSection(null)}>
                Cancel
              </button>
            </div>
          </form>
        </div>
      )}

      {visibleSection === "enrollment" && (
        <div className="dashboard-section">
          <h2>Enrollment Management</h2>
          <div className="enrollment-actions">
            <input type="number" placeholder="Student ID" id="studentId" />
            <input type="number" placeholder="Course ID" id="courseId" />
            <button
              onClick={() => {
                const studentId = parseInt(
                  (document.getElementById("studentId") as HTMLInputElement)
                    .value
                );
                const courseId = parseInt(
                  (document.getElementById("courseId") as HTMLInputElement)
                    .value
                );
                handleRemoveStudentFromCourse(studentId, courseId);
                setVisibleSection(null);
              }}
            >
              Remove Student from Course
            </button>
            <button type="button" onClick={() => setVisibleSection(null)}>
              Cancel
            </button>
          </div>
        </div>
      )}
    </div>
  );
};

export default AdminDashboard;
