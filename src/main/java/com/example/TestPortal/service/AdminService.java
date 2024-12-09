package com.example.TestPortal.service;

import com.example.TestPortal.model.Student;
import com.example.TestPortal.model.Teacher;
import com.example.TestPortal.model.Course;
import com.example.TestPortal.repository.AdminRepository;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class AdminService {
    private final AdminRepository adminRepository;

    public AdminService(AdminRepository adminRepository) {
        this.adminRepository = adminRepository;
    }

    // Student Management
    public void addStudent(Student student) {
        adminRepository.addStudent(student.getName(), student.getEmail(), student.getPassword());
    }

    public Student getStudentById(int id) {
        return adminRepository.getStudentById(id);
    }

    public List<Student> getAllStudents() {
        return adminRepository.getAllStudents();
    }

    public void deleteStudent(int id) {
        adminRepository.deleteStudent(id);
    }

    public void updateStudent(int id, Student student) {
        adminRepository.updateStudent(id, student.getName(), student.getEmail(), student.getPassword());
    }

    // Teacher Management
    public void addTeacher(Teacher teacher) {
        adminRepository.addTeacher(teacher.getName(), teacher.getEmail(), teacher.getPassword());
    }

    public Teacher getTeacherById(int id) {
        return adminRepository.getTeacherById(id);
    }

    public List<Teacher> getAllTeachers() {
        return adminRepository.getAllTeachers();
    }

    public void deleteTeacher(int id) {
        adminRepository.deleteTeacher(id);
    }

    public void updateTeacher(int id, Teacher teacher) {
        adminRepository.updateTeacher(id, teacher.getName(), teacher.getEmail(), teacher.getPassword());
    }

    // Course Management
    public void addCourse(Course course) {
        adminRepository.addCourse(course.getCourseName(), course.getDescription(), course.getTeacherId());
    }

    public Course getCourseById(int id) {
        return adminRepository.getCourseById(id);
    }

    public List<Course> getAllCourses() {
        return adminRepository.getAllCourses();
    }

    public void assignTeacherToCourse(int courseId, int teacherId) {
        adminRepository.assignTeacherToCourse(courseId, teacherId);
    }

    // Enrollment Management
    public void enrollStudent(int studentId, int courseId) {
        adminRepository.enrollStudent(studentId, courseId);
    }

    public void removeStudentFromCourse(int studentId, int courseId) {
        adminRepository.removeStudentFromCourse(studentId, courseId);
    }

    public void deleteCourse(int id) {
        adminRepository.deleteCourse(id);
    }

    public void updateCourse(int id, Course course) {
        adminRepository.updateCourse(id, course.getCourseName(), 
            course.getDescription(), course.getTeacherId());
    }
} 