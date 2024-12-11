import React, { useState, useEffect } from "react";
import api from "../api/axios";
import "../styles/StudentDashboard.css";
import { useAuth } from "../context/AuthContext";
import { useNavigate } from "react-router-dom";

interface Course {
  courseId: number;
  courseName: string;
  description: string;
  teacherName: string;
}

interface Submission {
  submissionId: number;
  examTitle: string;
  totalScore: number;
  submissionDate: string;
}

interface AvailableTest {
  examId: number;
  title: string;
  description: string;
  duration: number;
  startTime: string;
  endTime: string;
}

const StudentDashboard: React.FC = () => {
  const { user } = useAuth();
  const studentId = user?.studentId;
  const navigate = useNavigate();

  const [courses, setCourses] = useState<Course[]>([]);
  const [submissions, setSubmissions] = useState<Submission[]>([]);
  const [visibleSection, setVisibleSection] = useState<string | null>(null);
  const [message, setMessage] = useState<string>("");
  const [availableTests, setAvailableTests] = useState<AvailableTest[]>([]);
  const [selectedCourse, setSelectedCourse] = useState<number | null>(null);

  useEffect(() => {
    if (!studentId) {
      setMessage("Student ID not found");
    }
  }, [studentId]);

  useEffect(() => {
    if (visibleSection === "takeTest" || visibleSection === "courses") {
      fetchCourses();
    }
  }, [visibleSection]);

  const fetchCourses = async () => {
    try {
      const response = await api.get(`/api/students/${studentId}/courses`);
      setCourses(response.data);
    } catch (error: any) {
      setMessage(
        `Error fetching courses: ${
          error.response?.data?.message || error.message
        }`
      );
    }
  };

  const fetchSubmissions = async () => {
    try {
      const response = await api.get(`/api/students/${studentId}/submissions`);
      setSubmissions(response.data);
    } catch (error: any) {
      setMessage(
        `Error fetching submissions: ${
          error.response?.data?.message || error.message
        }`
      );
    }
  };

  const fetchAvailableTests = async (courseId: number) => {
    try {
      const response = await api.get(
        `/api/students/courses/${courseId}/available-tests`
      );
      setAvailableTests(response.data);
      setSelectedCourse(courseId);
    } catch (error: any) {
      console.error("Error fetching tests:", error);
      setMessage(
        `Error fetching tests: ${
          error.response?.data?.message || error.message
        }`
      );
    }
  };

  const handleTakeTest = (courseId: number) => {
    fetchAvailableTests(courseId);
  };

  const startTest = (testId: number) => {
    navigate(`/test/${testId}`);
  };

  return (
    <div className="student-dashboard">
      <h1>Student Dashboard</h1>
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
        <button
          onClick={() => {
            setVisibleSection("courses");
            fetchCourses();
          }}
        >
          View Courses
        </button>
        <button
          onClick={() => {
            setVisibleSection("submissions");
            fetchSubmissions();
          }}
        >
          View Submissions
        </button>
        <button onClick={() => setVisibleSection("takeTest")}>Take Test</button>
      </div>

      {visibleSection === "courses" && (
        <div className="dashboard-section">
          <h2>Your Courses</h2>
          {courses.length > 0 ? (
            <table className="data-table">
              <thead>
                <tr>
                  <th>Course ID</th>
                  <th>Course Name</th>
                  <th>Description</th>
                  <th>Teacher</th>
                </tr>
              </thead>
              <tbody>
                {courses.map((course) => (
                  <tr key={course.courseId}>
                    <td>{course.courseId}</td>
                    <td>{course.courseName}</td>
                    <td>{course.description}</td>
                    <td>{course.teacherName}</td>
                  </tr>
                ))}
              </tbody>
            </table>
          ) : (
            <p>No courses available</p>
          )}
        </div>
      )}

      {visibleSection === "submissions" && (
        <div className="dashboard-section">
          <h2>Previous Submissions</h2>
          {submissions.length > 0 ? (
            <table className="data-table">
              <thead>
                <tr>
                  <th>Submission ID</th>
                  <th>Exam Title</th>
                  <th>Total Score</th>
                  <th>Submission Date</th>
                </tr>
              </thead>
              <tbody>
                {submissions.map((submission) => (
                  <tr key={submission.submissionId}>
                    <td>{submission.submissionId}</td>
                    <td>{submission.examTitle}</td>
                    <td>{submission.totalScore}</td>
                    <td>
                      {new Date(submission.submissionDate).toLocaleString()}
                    </td>
                  </tr>
                ))}
              </tbody>
            </table>
          ) : (
            <p>No submissions found</p>
          )}
        </div>
      )}

      {visibleSection === "takeTest" && (
        <div className="dashboard-section">
          <h2>Take a Test</h2>
          {courses.length > 0 ? (
            <div>
              <p>Select a course to view available tests:</p>
              <ul className="course-list">
                {courses.map((course) => (
                  <li key={course.courseId}>
                    <div className="course-item">
                      <span>{course.courseName}</span>
                      <button onClick={() => handleTakeTest(course.courseId)}>
                        View Tests
                      </button>
                    </div>
                    {selectedCourse === course.courseId &&
                      availableTests.length > 0 && (
                        <ul className="test-list">
                          {availableTests.map((test) => (
                            <li key={test.examId} className="test-item">
                              <div className="test-info">
                                <h4>{test.title}</h4>
                                <p>{test.description}</p>
                                <p>Duration: {test.duration} minutes</p>
                                <p>
                                  Start:{" "}
                                  {new Date(test.startTime).toLocaleString()}
                                </p>
                                <p>
                                  End: {new Date(test.endTime).toLocaleString()}
                                </p>
                              </div>
                              <button onClick={() => startTest(test.examId)}>
                                Start Test
                              </button>
                            </li>
                          ))}
                        </ul>
                      )}
                    {selectedCourse === course.courseId &&
                      availableTests.length === 0 && (
                        <p className="no-tests">
                          No tests available for this course
                        </p>
                      )}
                  </li>
                ))}
              </ul>
            </div>
          ) : (
            <p>No courses available for testing</p>
          )}
        </div>
      )}
    </div>
  );
};

export default StudentDashboard;
