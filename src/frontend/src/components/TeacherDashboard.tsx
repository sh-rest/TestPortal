import React, { useState } from "react";
import api from "../api/axios";
import "../styles/TeacherDashboard.css";
import { useAuth } from "../context/AuthContext";
import { useNavigate } from "react-router-dom";

interface Exam {
  examId?: number;
  title: string;
  description: string;
  date: string;
  courseId: number;
  duration: number;
  startTime: string;
  endTime: string;
}

interface Question {
  questionId?: number;
  examId: number;
  content: string;
  type: "MCQ" | "ShortAnswer";
  options: string;
  correctAnswer: string;
}

interface Student {
  studentId: number;
  name: string;
  email: string;
  courseId: number;
}

interface Course {
  courseId: number;
  courseName: string;
  description: string;
}

interface ExamStatistics {
  averageScore: number;
  highestScore: number;
  lowestScore: number;
  totalSubmissions: number;
}

interface QuestionStatistics {
  totalAttempts: number;
  correctAnswers: number;
  successRate: number;
}

interface ExamCreation {
  isCreating: boolean;
  currentExamId?: number;
  examTitle?: string;
}

const TeacherDashboard: React.FC = () => {
  const navigate = useNavigate();
  const { user } = useAuth();

  // Add this check at the beginning of the component
  React.useEffect(() => {
    if (!user || user.role !== "teacher") {
      navigate("/login");
    }
  }, [user, navigate]);

  const teacherId = user?.teacherId;

  // States for forms
  const [exam, setExam] = useState<Exam>({
    title: "",
    description: "",
    date: "",
    courseId: 0,
    duration: 0,
    startTime: "",
    endTime: "",
  });

  const [question, setQuestion] = useState<Question>({
    examId: 0,
    content: "",
    type: "MCQ",
    options: "",
    correctAnswer: "",
  });

  // States for lists
  const [exams, setExams] = useState<Exam[]>([]);
  const [questions, setQuestions] = useState<Question[]>([]);
  const [students, setStudents] = useState<Student[]>([]);
  const [courses, setCourses] = useState<Course[]>([]);
  const [examStats, setExamStats] = useState<ExamStatistics | null>(null);
  const [questionStats, setQuestionStats] = useState<QuestionStatistics | null>(
    null
  );

  const [message, setMessage] = useState<string>("");
  const [visibleSection, setVisibleSection] = useState<string | null>(null);

  // Add this state for exam creation flow
  const [examCreation, setExamCreation] = useState<ExamCreation>({
    isCreating: false,
  });

  // Exam Management
  const handleCreateExam = async (e: React.FormEvent) => {
    e.preventDefault();
    try {
      if (!teacherId) {
        setMessage("Not authenticated");
        return;
      }

      const examData = {
        ...exam,
        courseId: Number(exam.courseId),
        date: new Date(exam.date).toISOString(),
        startTime: new Date(exam.startTime).toISOString(),
        endTime: new Date(exam.endTime).toISOString(),
        duration: Number(exam.duration),
      };

      console.log("Sending exam data:", examData);
      const response = await api.post(
        `/api/teachers/${teacherId}/exams`,
        examData
      );
      console.log("Response:", response.data);

      setMessage("Exam created successfully");
      setExamCreation({
        isCreating: true,
        currentExamId: response.data,
        examTitle: exam.title,
      });
      fetchTeacherExams();
    } catch (error: any) {
      console.error("Error creating exam:", error);
      setMessage(error.response?.data?.message || "Error creating exam");
    }
  };

  const handleUpdateExam = async (examToUpdate: Exam) => {
    try {
      // Create a copy of the exam data for update
      const updateData = {
        title: examToUpdate.title,
        description: examToUpdate.description,
        date: examToUpdate.date,
        courseId: examToUpdate.courseId,
      };

      // Ensure date is in the correct format
      if (typeof updateData.date === "string") {
        updateData.date = new Date(updateData.date).toISOString();
      }

      await api.put(`/api/teachers/exams/${examToUpdate.examId}`, updateData);
      setMessage("Exam updated successfully");
      fetchTeacherExams(); // Refresh the list
    } catch (error: any) {
      console.error("Error updating exam:", error);
      setMessage(error.response?.data?.message || "Error updating exam");
    }
  };

  // Question Management
  const handleAddQuestion = async (e: React.FormEvent) => {
    e.preventDefault();
    try {
      await api.post(
        `/api/teachers/exams/${question.examId}/questions`,
        question
      );
      setMessage("Question added successfully");
      setQuestion({
        examId: 0,
        content: "",
        type: "MCQ",
        options: "",
        correctAnswer: "",
      });
      fetchExamQuestions(question.examId);
    } catch (error) {
      setMessage("Error adding question");
    }
  };

  const handleDeleteQuestion = async (questionId: number) => {
    try {
      await api.delete(`/api/teachers/questions/${questionId}`);
      setMessage("Question deleted successfully");
      fetchExamQuestions(question.examId);
    } catch (error) {
      setMessage("Error deleting question");
    }
  };

  // Fetch functions
  const fetchTeacherExams = async () => {
    try {
      const response = await api.get(`/api/teachers/${teacherId}/exams`);
      setExams(response.data);
    } catch (error) {
      setMessage("Error fetching exams");
    }
  };

  const fetchExamQuestions = async (examId: number) => {
    try {
      const response = await api.get(`/api/teachers/exams/${examId}/questions`);
      setQuestions(response.data);
    } catch (error) {
      setMessage("Error fetching questions");
    }
  };

  const fetchTeacherCourses = async () => {
    try {
      console.log("Fetching courses for teacher:", teacherId); // Debug log
      if (!teacherId) {
        setMessage("Teacher ID not found");
        return;
      }

      const response = await api.get(`/api/teachers/${teacherId}/courses`);
      console.log("Fetched courses:", response.data); // Debug log
      setCourses(response.data);
    } catch (error: any) {
      console.error("Error fetching courses:", error);
      setMessage(
        `Error fetching courses: ${
          error.response?.data?.message || error.message
        }`
      );
    }
  };

  const fetchEnrolledStudents = async (courseId: number) => {
    try {
      console.log(`Fetching students for course ${courseId}`); // Debug log
      if (!teacherId) {
        setMessage("Teacher ID not found");
        return;
      }

      const response = await api.get(
        `/api/teachers/${teacherId}/courses/${courseId}/students`
      );

      // Map the courseId to each student
      const studentsWithCourse = response.data.map((student: Student) => ({
        ...student,
        courseId: courseId, // Ensure each student has the courseId
      }));

      console.log("Fetched students:", studentsWithCourse); // Debug log
      setStudents(studentsWithCourse);
    } catch (error: any) {
      console.error("Error fetching enrolled students:", error);
      setMessage(
        `Error fetching enrolled students: ${
          error.response?.data?.message || error.message
        }`
      );
    }
  };

  const fetchExamStatistics = async (examId: number) => {
    try {
      const response = await api.get(
        `/api/teachers/exams/${examId}/statistics`
      );
      setExamStats(response.data);
    } catch (error) {
      setMessage("Error fetching exam statistics");
    }
  };

  const fetchQuestionStatistics = async (questionId: number) => {
    try {
      const response = await api.get(
        `/api/teachers/questions/${questionId}/statistics`
      );
      setQuestionStats(response.data);
    } catch (error) {
      setMessage("Error fetching question statistics");
    }
  };

  // Add this handler for completing exam creation
  const handleCompleteExam = () => {
    setExamCreation({ isCreating: false });
    setQuestion({
      examId: 0,
      content: "",
      type: "MCQ",
      options: "",
      correctAnswer: "",
    });
    fetchTeacherExams();
    setVisibleSection("exams");
  };

  return (
    <div className="teacher-dashboard">
      <h1>Teacher Dashboard</h1>
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
        <button onClick={() => setVisibleSection("exams")}>Manage Exams</button>
        <button onClick={() => setVisibleSection("questions")}>
          Manage Questions
        </button>
        <button onClick={() => setVisibleSection("courses")}>
          View Courses
        </button>
        <button onClick={() => setVisibleSection("students")}>
          View Students
        </button>
        <button onClick={() => setVisibleSection("statistics")}>
          View Statistics
        </button>
      </div>

      {visibleSection === "exams" && (
        <div className="dashboard-section">
          <h2>Exam Management</h2>
          {!examCreation.isCreating ? (
            <form onSubmit={handleCreateExam}>
              <input
                type="text"
                placeholder="Exam Title"
                value={exam.title}
                onChange={(e) => setExam({ ...exam, title: e.target.value })}
                required
              />
              <input
                type="text"
                placeholder="Description"
                value={exam.description}
                onChange={(e) =>
                  setExam({ ...exam, description: e.target.value })
                }
                required
              />
              <input
                type="datetime-local"
                placeholder="Date"
                value={exam.date}
                onChange={(e) => setExam({ ...exam, date: e.target.value })}
                required
              />
              <input
                type="number"
                placeholder="Course ID"
                value={exam.courseId || ""}
                onChange={(e) =>
                  setExam({ ...exam, courseId: parseInt(e.target.value) || 0 })
                }
                required
              />
              <input
                type="number"
                placeholder="Duration (minutes)"
                value={exam.duration || ""}
                onChange={(e) =>
                  setExam({ ...exam, duration: parseInt(e.target.value) || 0 })
                }
                required
              />
              <input
                type="datetime-local"
                placeholder="Start Time"
                value={exam.startTime}
                onChange={(e) =>
                  setExam({ ...exam, startTime: e.target.value })
                }
                required
              />
              <input
                type="datetime-local"
                placeholder="End Time"
                value={exam.endTime}
                onChange={(e) => setExam({ ...exam, endTime: e.target.value })}
                required
              />
              <div className="form-buttons">
                <button type="submit">Create Exam</button>
                <button type="button" onClick={() => setVisibleSection(null)}>
                  Cancel
                </button>
              </div>
            </form>
          ) : (
            <div className="exam-creation-pane">
              <div className="exam-info">
                <h3>Adding Questions to Exam:</h3>
                <p>
                  <strong>Exam ID:</strong> {examCreation.currentExamId}
                </p>
                <p>
                  <strong>Exam Title:</strong> {examCreation.examTitle}
                </p>
              </div>

              <form
                onSubmit={async (e) => {
                  e.preventDefault();
                  try {
                    const questionData = {
                      ...question,
                      examId: examCreation.currentExamId,
                    };

                    await api.post(
                      `/api/teachers/exams/${examCreation.currentExamId}/questions`,
                      questionData
                    );

                    // Fetch updated questions immediately after adding
                    if (examCreation.currentExamId) {
                      fetchExamQuestions(examCreation.currentExamId);
                    }

                    // Clear only the question form, keeping the exam creation state
                    setQuestion({
                      examId: examCreation.currentExamId || 0,
                      content: "",
                      type: "MCQ",
                      options: "",
                      correctAnswer: "",
                    });
                    setMessage("Question added successfully");
                  } catch (error: any) {
                    console.error("Error adding question:", error);
                    setMessage(
                      error.response?.data?.message || "Error adding question"
                    );
                  }
                }}
              >
                <textarea
                  placeholder="Question Content"
                  value={question.content}
                  onChange={(e) =>
                    setQuestion({ ...question, content: e.target.value })
                  }
                  required
                />
                <select
                  value={question.type}
                  onChange={(e) =>
                    setQuestion({
                      ...question,
                      type: e.target.value as "MCQ" | "ShortAnswer",
                    })
                  }
                >
                  <option value="MCQ">Multiple Choice</option>
                  <option value="ShortAnswer">Short Answer</option>
                </select>
                {question.type === "MCQ" && (
                  <textarea
                    placeholder="Options (comma-separated)"
                    value={question.options}
                    onChange={(e) =>
                      setQuestion({ ...question, options: e.target.value })
                    }
                    required
                  />
                )}
                <input
                  type="text"
                  placeholder="Correct Answer"
                  value={question.correctAnswer}
                  onChange={(e) =>
                    setQuestion({ ...question, correctAnswer: e.target.value })
                  }
                  required
                />
                <div className="exam-creation-buttons">
                  <button type="submit">Add Question</button>
                  <button type="button" onClick={handleCompleteExam}>
                    Complete Exam
                  </button>
                  <button type="button" onClick={() => setVisibleSection(null)}>
                    Cancel
                  </button>
                </div>
              </form>

              <div className="current-questions">
                <h4>Questions Added:</h4>
                {questions.length > 0 ? (
                  <table className="data-table">
                    <thead>
                      <tr>
                        <th>Content</th>
                        <th>Type</th>
                        <th>Options</th>
                        <th>Correct Answer</th>
                      </tr>
                    </thead>
                    <tbody>
                      {questions.map((q, index) => (
                        <tr key={index}>
                          <td>{q.content}</td>
                          <td>{q.type}</td>
                          <td>{q.options}</td>
                          <td>{q.correctAnswer}</td>
                        </tr>
                      ))}
                    </tbody>
                  </table>
                ) : (
                  <p>No questions added yet</p>
                )}
              </div>
            </div>
          )}

          <div className="list-section">
            <h3>Your Exams</h3>
            <button onClick={fetchTeacherExams}>Show Exams</button>
            {exams.length > 0 && (
              <table className="data-table">
                <thead>
                  <tr>
                    <th>ID</th>
                    <th>Title</th>
                    <th>Description</th>
                    <th>Date</th>
                    <th>Course ID</th>
                    <th>Duration (min)</th>
                    <th>Start Time</th>
                    <th>End Time</th>
                    <th>Actions</th>
                  </tr>
                </thead>
                <tbody>
                  {exams.map((exam) => (
                    <tr key={exam.examId}>
                      <td>{exam.examId}</td>
                      <td>{exam.title}</td>
                      <td>{exam.description}</td>
                      <td>{new Date(exam.date).toLocaleString()}</td>
                      <td>{exam.courseId}</td>
                      <td>{exam.duration}</td>
                      <td>{new Date(exam.startTime).toLocaleString()}</td>
                      <td>{new Date(exam.endTime).toLocaleString()}</td>
                      <td>
                        <button
                          onClick={() => {
                            const updatedTitle = prompt(
                              "Enter new title:",
                              exam.title
                            );
                            const updatedDesc = prompt(
                              "Enter new description:",
                              exam.description
                            );
                            const updatedDate = prompt(
                              "Enter new date (YYYY-MM-DD HH:mm):",
                              new Date(exam.date).toISOString().slice(0, 16)
                            );
                            const updatedDuration = prompt(
                              "Enter new duration (minutes):",
                              exam.duration.toString()
                            );
                            const updatedStartTime = prompt(
                              "Enter new start time (YYYY-MM-DD HH:mm):",
                              new Date(exam.startTime)
                                .toISOString()
                                .slice(0, 16)
                            );
                            const updatedEndTime = prompt(
                              "Enter new end time (YYYY-MM-DD HH:mm):",
                              new Date(exam.endTime).toISOString().slice(0, 16)
                            );

                            if (
                              updatedTitle &&
                              updatedDesc &&
                              updatedDate &&
                              updatedDuration &&
                              updatedStartTime &&
                              updatedEndTime
                            ) {
                              const updatedExam = {
                                ...exam,
                                title: updatedTitle,
                                description: updatedDesc,
                                date: new Date(updatedDate).toISOString(),
                                duration: parseInt(updatedDuration),
                                startTime: new Date(
                                  updatedStartTime
                                ).toISOString(),
                                endTime: new Date(updatedEndTime).toISOString(),
                              };
                              handleUpdateExam(updatedExam);
                            }
                          }}
                        >
                          Update
                        </button>
                      </td>
                    </tr>
                  ))}
                </tbody>
              </table>
            )}
          </div>
        </div>
      )}

      {visibleSection === "questions" && (
        <div className="dashboard-section">
          <h2>Question Management</h2>
          <form onSubmit={handleAddQuestion}>
            <input
              type="number"
              placeholder="Exam ID"
              value={question.examId || ""}
              onChange={(e) =>
                setQuestion({
                  ...question,
                  examId: parseInt(e.target.value) || 0,
                })
              }
              required
            />
            <textarea
              placeholder="Question Content"
              value={question.content}
              onChange={(e) =>
                setQuestion({ ...question, content: e.target.value })
              }
              required
            />
            <select
              value={question.type}
              onChange={(e) =>
                setQuestion({
                  ...question,
                  type: e.target.value as "MCQ" | "ShortAnswer",
                })
              }
            >
              <option value="MCQ">Multiple Choice</option>
              <option value="ShortAnswer">Short Answer</option>
            </select>
            {question.type === "MCQ" && (
              <textarea
                placeholder="Options (comma-separated)"
                value={question.options}
                onChange={(e) =>
                  setQuestion({ ...question, options: e.target.value })
                }
                required
              />
            )}
            <input
              type="text"
              placeholder="Correct Answer"
              value={question.correctAnswer}
              onChange={(e) =>
                setQuestion({ ...question, correctAnswer: e.target.value })
              }
              required
            />
            <div className="form-buttons">
              <button type="submit">Add Question</button>
              <button type="button" onClick={() => setVisibleSection(null)}>
                Close
              </button>
            </div>
          </form>

          <div className="list-section">
            <h3>Questions List</h3>
            <div className="action-inputs">
              <input
                type="number"
                placeholder="Enter Exam ID"
                id="fetchQuestionsExamId"
              />
              <button
                onClick={() => {
                  const examId = parseInt(
                    (
                      document.getElementById(
                        "fetchQuestionsExamId"
                      ) as HTMLInputElement
                    ).value
                  );
                  if (examId) fetchExamQuestions(examId);
                }}
              >
                Show Questions
              </button>
            </div>
            {questions.length > 0 && (
              <table className="data-table">
                <thead>
                  <tr>
                    <th>ID</th>
                    <th>Content</th>
                    <th>Type</th>
                    <th>Options</th>
                    <th>Correct Answer</th>
                    <th>Actions</th>
                  </tr>
                </thead>
                <tbody>
                  {questions.map((q) => (
                    <tr key={q.questionId}>
                      <td>{q.questionId}</td>
                      <td>{q.content}</td>
                      <td>{q.type}</td>
                      <td>{q.options}</td>
                      <td>{q.correctAnswer}</td>
                      <td>
                        <button
                          onClick={() => {
                            if (q.questionId)
                              handleDeleteQuestion(q.questionId);
                          }}
                        >
                          Delete
                        </button>
                      </td>
                    </tr>
                  ))}
                </tbody>
              </table>
            )}
          </div>
        </div>
      )}

      {visibleSection === "courses" && (
        <div className="dashboard-section">
          <h2>Course Management</h2>
          <button onClick={fetchTeacherCourses}>Load Courses</button>
          {courses.length > 0 && (
            <table className="data-table">
              <thead>
                <tr>
                  <th>Course ID</th>
                  <th>Course Name</th>
                  <th>Description</th>
                </tr>
              </thead>
              <tbody>
                {courses.map((course) => (
                  <tr key={course.courseId}>
                    <td>{course.courseId}</td>
                    <td>{course.courseName}</td>
                    <td>{course.description}</td>
                  </tr>
                ))}
              </tbody>
            </table>
          )}
        </div>
      )}

      {visibleSection === "students" && (
        <div className="dashboard-section">
          <h2>Student Management</h2>
          {courses.length === 0 ? (
            <div>
              <button onClick={fetchTeacherCourses}>Load Courses First</button>
            </div>
          ) : (
            courses.map((course) => (
              <div key={course.courseId} className="course-students">
                <h3>{course.courseName}</h3>
                <button onClick={() => fetchEnrolledStudents(course.courseId)}>
                  View Students
                </button>
                {students.filter(
                  (student) => student.courseId === course.courseId
                ).length > 0 && (
                  <table className="data-table">
                    <thead>
                      <tr>
                        <th>Student ID</th>
                        <th>Name</th>
                        <th>Email</th>
                      </tr>
                    </thead>
                    <tbody>
                      {students
                        .filter(
                          (student) => student.courseId === course.courseId
                        )
                        .map((student) => (
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
            ))
          )}
        </div>
      )}

      {visibleSection === "statistics" && (
        <div className="dashboard-section">
          <h2>Statistics</h2>
          <div className="stats-container">
            <div className="exam-stats">
              <h3>Exam Statistics</h3>
              <div className="action-inputs">
                <input
                  type="number"
                  placeholder="Enter Exam ID"
                  id="fetchStatsExamId"
                />
                <button
                  onClick={() => {
                    const examId = parseInt(
                      (
                        document.getElementById(
                          "fetchStatsExamId"
                        ) as HTMLInputElement
                      ).value
                    );
                    if (examId) fetchExamStatistics(examId);
                  }}
                >
                  View Stats
                </button>
              </div>
              {examStats && (
                <div className="stats-display">
                  <p>Average Score: {examStats.averageScore.toFixed(2)}%</p>
                  <p>Highest Score: {examStats.highestScore.toFixed(2)}%</p>
                  <p>Lowest Score: {examStats.lowestScore.toFixed(2)}%</p>
                  <p>Total Submissions: {examStats.totalSubmissions}</p>
                </div>
              )}
            </div>

            <div className="question-stats">
              <h3>Question Statistics</h3>
              <div className="action-inputs">
                <input
                  type="number"
                  placeholder="Enter Question ID"
                  id="fetchStatsQuestionId"
                />
                <button
                  onClick={() => {
                    const questionId = parseInt(
                      (
                        document.getElementById(
                          "fetchStatsQuestionId"
                        ) as HTMLInputElement
                      ).value
                    );
                    if (questionId) fetchQuestionStatistics(questionId);
                  }}
                >
                  View Stats
                </button>
              </div>
              {questionStats && (
                <div className="stats-display">
                  <p>Total Attempts: {questionStats.totalAttempts}</p>
                  <p>Correct Answers: {questionStats.correctAnswers}</p>
                  <p>Success Rate: {questionStats.successRate.toFixed(2)}%</p>
                </div>
              )}
            </div>
          </div>
        </div>
      )}
    </div>
  );
};

export default TeacherDashboard;
