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

interface Enrollment {
  student_id: number;
  student_name: string;
  course_id: number;
  course_name: string;
}

interface StudentList {
  studentId: number;
  name: string;
  email: string;
}

interface TeacherList {
  teacherId: number;
  name: string;
  email: string;
}

interface CourseList {
  courseId: number;
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

  // Add states for edit mode
  const [editMode, setEditMode] = useState<{ type: string; id: number | null }>(
    {
      type: "",
      id: null,
    }
  );

  // Add this state
  const [enrollments, setEnrollments] = useState<Enrollment[]>([]);

  // Add these states
  const [students, setStudents] = useState<StudentList[]>([]);
  const [teachers, setTeachers] = useState<TeacherList[]>([]);
  const [courses, setCourseList] = useState<CourseList[]>([]);

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
      if (!id || id <= 0) {
        setMessage("Please enter a valid Student ID");
        return;
      }

      const response = await api.delete(`/api/admin/students/${id}`);
      if (response.status === 200) {
        setMessage("Student deleted successfully");
        // Clear the input field
        const input = document.getElementById(
          "manageStudentId"
        ) as HTMLInputElement;
        if (input) input.value = "";
      } else {
        throw new Error("Failed to delete student");
      }
    } catch (error: any) {
      console.error("Error deleting student:", error);
      setMessage(error.response?.data?.message || "Error deleting student");
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
      if (!id || id <= 0) {
        setMessage("Please enter a valid Teacher ID");
        return;
      }

      const response = await api.delete(`/api/admin/teachers/${id}`);
      if (response.status === 200) {
        setMessage("Teacher deleted successfully");
        // Clear the input field
        const input = document.getElementById(
          "manageTeacherId"
        ) as HTMLInputElement;
        if (input) input.value = "";
      } else {
        throw new Error("Failed to delete teacher");
      }
    } catch (error: any) {
      console.error("Error deleting teacher:", error);
      setMessage(error.response?.data?.message || "Error deleting teacher");
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

  // Add these functions after existing handle functions
  const handleEditStudent = (studentId: number) => {
    setEditMode({ type: "student", id: studentId });
    // You can fetch student details here if needed
  };

  const handleEditTeacher = (teacherId: number) => {
    setEditMode({ type: "teacher", id: teacherId });
    // You can fetch teacher details here if needed
  };

  // Add this function to fetch enrollments
  const fetchEnrollments = async () => {
    try {
      const response = await api.get("/api/admin/enrollments");
      setEnrollments(response.data);
    } catch (error) {
      setMessage("Error fetching enrollments");
    }
  };

  // Add these fetch functions
  const fetchStudents = async () => {
    try {
      const response = await api.get("/api/admin/students/all");
      console.log("Students data:", response.data);
      setStudents(response.data);
    } catch (error) {
      console.error("Error fetching students:", error);
      setMessage("Error fetching students");
    }
  };

  const fetchTeachers = async () => {
    try {
      const response = await api.get("/api/admin/teachers/all");
      console.log("Teachers data:", response.data);
      setTeachers(response.data);
    } catch (error) {
      console.error("Error fetching teachers:", error);
      setMessage("Error fetching teachers");
    }
  };

  const fetchCourses = async () => {
    try {
      const response = await api.get("/api/admin/courses/all");
      console.log("Courses data:", response.data);
      setCourseList(response.data);
    } catch (error) {
      console.error("Error fetching courses:", error);
      setMessage("Error fetching courses");
    }
  };

  return (
    <div className="admin-dashboard">
      <h1>Admin Dashboard</h1>
      {message && (
        <div className="message">
          <span>{message}</span>
          <button
            className="message-close"
            onClick={() => setMessage("")}
            aria-label="Close message"
          >
            ×
          </button>
        </div>
      )}

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
          <form
            onSubmit={(e) => {
              e.preventDefault();
              if (editMode.type === "student" && editMode.id) {
                handleUpdateStudent(editMode.id);
                setEditMode({ type: "", id: null });
              } else {
                handleAddStudent(e);
              }
            }}
          >
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
              <button type="submit">
                {editMode.type === "student" ? "Update Student" : "Add Student"}
              </button>
              {editMode.type === "student" && (
                <button
                  type="button"
                  onClick={() => {
                    setEditMode({ type: "", id: null });
                    setStudent({ name: "", email: "", password: "" });
                  }}
                >
                  Cancel Edit
                </button>
              )}
              <button type="button" onClick={() => setVisibleSection(null)}>
                Close
              </button>
            </div>
          </form>

          <div className="management-actions">
            <h3>Manage Existing Students</h3>
            <div className="action-inputs">
              <input
                type="number"
                placeholder="Student ID"
                id="manageStudentId"
              />
              <button
                onClick={() => {
                  const id = parseInt(
                    (
                      document.getElementById(
                        "manageStudentId"
                      ) as HTMLInputElement
                    ).value
                  );
                  handleEditStudent(id);
                }}
              >
                Edit Student
              </button>
              <button
                onClick={() => {
                  const id = parseInt(
                    (
                      document.getElementById(
                        "manageStudentId"
                      ) as HTMLInputElement
                    ).value
                  );
                  handleDeleteStudent(id);
                }}
              >
                Delete Student
              </button>
            </div>
          </div>

          <div className="list-section">
            <h3>Student List</h3>
            <button onClick={fetchStudents}>Show Students</button>
            {students.length > 0 && (
              <table className="data-table">
                <thead>
                  <tr>
                    <th>ID</th>
                    <th>Name</th>
                    <th>Email</th>
                  </tr>
                </thead>
                <tbody>
                  {students.map((student) => (
                    <tr key={student.studentId}>
                      <td>{student.studentId}</td>
                      <td>{student.name}</td>
                      <td>{student.email}</td>
                    </tr>
                  ))}
                </tbody>
              </table>
            )}
          </div>
        </div>
      )}

      {visibleSection === "teacher" && (
        <div className="dashboard-section">
          <h2>Teacher Management</h2>
          <form
            onSubmit={(e) => {
              e.preventDefault();
              if (editMode.type === "teacher" && editMode.id) {
                handleUpdateTeacher(editMode.id);
                setEditMode({ type: "", id: null });
              } else {
                handleAddTeacher(e);
              }
            }}
          >
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
              <button type="submit">
                {editMode.type === "teacher" ? "Update Teacher" : "Add Teacher"}
              </button>
              {editMode.type === "teacher" && (
                <button
                  type="button"
                  onClick={() => {
                    setEditMode({ type: "", id: null });
                    setTeacher({ name: "", email: "", password: "" });
                  }}
                >
                  Cancel Edit
                </button>
              )}
              <button type="button" onClick={() => setVisibleSection(null)}>
                Close
              </button>
            </div>
          </form>

          <div className="management-actions">
            <h3>Manage Existing Teachers</h3>
            <div className="action-inputs">
              <input
                type="number"
                placeholder="Teacher ID"
                id="manageTeacherId"
              />
              <button
                onClick={() => {
                  const id = parseInt(
                    (
                      document.getElementById(
                        "manageTeacherId"
                      ) as HTMLInputElement
                    ).value
                  );
                  handleEditTeacher(id);
                }}
              >
                Edit Teacher
              </button>
              <button
                onClick={() => {
                  const id = parseInt(
                    (
                      document.getElementById(
                        "manageTeacherId"
                      ) as HTMLInputElement
                    ).value
                  );
                  handleDeleteTeacher(id);
                }}
              >
                Delete Teacher
              </button>
            </div>
          </div>

          <div className="list-section">
            <h3>Teacher List</h3>
            <button onClick={fetchTeachers}>Show Teachers</button>
            {teachers.length > 0 && (
              <table className="data-table">
                <thead>
                  <tr>
                    <th>ID</th>
                    <th>Name</th>
                    <th>Email</th>
                  </tr>
                </thead>
                <tbody>
                  {teachers.map((teacher) => (
                    <tr key={teacher.teacherId}>
                      <td>{teacher.teacherId}</td>
                      <td>{teacher.name}</td>
                      <td>{teacher.email}</td>
                    </tr>
                  ))}
                </tbody>
              </table>
            )}
          </div>
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

          <div className="management-actions">
            <h3>Assign Teacher to Course</h3>
            <div className="action-inputs">
              <input
                type="number"
                placeholder="Course ID"
                id="assignCourseId"
                min="1"
              />
              <input
                type="number"
                placeholder="Teacher ID"
                id="assignTeacherId"
                min="1"
              />
              <button
                onClick={() => {
                  const courseId = parseInt(
                    (
                      document.getElementById(
                        "assignCourseId"
                      ) as HTMLInputElement
                    ).value
                  );
                  const teacherId = parseInt(
                    (
                      document.getElementById(
                        "assignTeacherId"
                      ) as HTMLInputElement
                    ).value
                  );

                  if (!courseId || !teacherId) {
                    setMessage("Please enter both Course ID and Teacher ID");
                    return;
                  }

                  handleAssignTeacher(courseId, teacherId);

                  // Clear input fields after assignment
                  const courseInput = document.getElementById(
                    "assignCourseId"
                  ) as HTMLInputElement;
                  const teacherInput = document.getElementById(
                    "assignTeacherId"
                  ) as HTMLInputElement;
                  if (courseInput) courseInput.value = "";
                  if (teacherInput) teacherInput.value = "";
                }}
              >
                Assign Teacher
              </button>
            </div>
          </div>

          <div className="list-section">
            <h3>Course List</h3>
            <button onClick={fetchCourses}>Show Courses</button>
            {courses.length > 0 && (
              <table className="data-table">
                <thead>
                  <tr>
                    <th>ID</th>
                    <th>Course Name</th>
                    <th>Description</th>
                    <th>Teacher ID</th>
                  </tr>
                </thead>
                <tbody>
                  {courses.map((course) => (
                    <tr key={course.courseId}>
                      <td>{course.courseId}</td>
                      <td>{course.courseName}</td>
                      <td>{course.description}</td>
                      <td>{course.teacherId}</td>
                    </tr>
                  ))}
                </tbody>
              </table>
            )}
          </div>
        </div>
      )}

      {visibleSection === "enrollment" && (
        <div className="dashboard-section">
          <h2>Enrollment Management</h2>
          <div className="enrollment-actions">
            <input
              type="number"
              placeholder="Student ID"
              id="studentId"
              min="1"
            />
            <input
              type="number"
              placeholder="Course ID"
              id="courseId"
              min="1"
            />
            <div className="enrollment-buttons">
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

                  if (!studentId || !courseId) {
                    setMessage("Please enter both Student ID and Course ID");
                    return;
                  }

                  handleEnrollStudent(studentId, courseId);

                  // Clear input fields after enrollment
                  const studentInput = document.getElementById(
                    "studentId"
                  ) as HTMLInputElement;
                  const courseInput = document.getElementById(
                    "courseId"
                  ) as HTMLInputElement;
                  if (studentInput) studentInput.value = "";
                  if (courseInput) courseInput.value = "";
                }}
              >
                Enroll Student
              </button>
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

                  if (!studentId || !courseId) {
                    setMessage("Please enter both Student ID and Course ID");
                    return;
                  }

                  handleRemoveStudentFromCourse(studentId, courseId);

                  // Clear input fields after removal
                  const studentInput = document.getElementById(
                    "studentId"
                  ) as HTMLInputElement;
                  const courseInput = document.getElementById(
                    "courseId"
                  ) as HTMLInputElement;
                  if (studentInput) studentInput.value = "";
                  if (courseInput) courseInput.value = "";
                }}
              >
                Remove Student
              </button>
              <button type="button" onClick={() => setVisibleSection(null)}>
                Close
              </button>
            </div>

            <div className="enrollment-list">
              <h3>Current Enrollments</h3>
              <button onClick={fetchEnrollments}>Refresh Enrollments</button>
              <table className="enrollment-table">
                <thead>
                  <tr>
                    <th>Student ID</th>
                    <th>Student Name</th>
                    <th>Course ID</th>
                    <th>Course Name</th>
                  </tr>
                </thead>
                <tbody>
                  {enrollments.map((enrollment, index) => (
                    <tr key={index}>
                      <td>{enrollment.student_id}</td>
                      <td>{enrollment.student_name}</td>
                      <td>{enrollment.course_id}</td>
                      <td>{enrollment.course_name}</td>
                    </tr>
                  ))}
                </tbody>
              </table>
            </div>
          </div>
        </div>
      )}
    </div>
  );
};

export default AdminDashboard;
