package com.example.Bai2.service;

import com.example.Bai2.Model.Course;
import com.example.Bai2.Model.Enrollment;
import com.example.Bai2.Model.Student;
import com.example.Bai2.repository.EnrollmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class EnrollmentService {

    @Autowired
    private EnrollmentRepository enrollmentRepository;

    public void enroll(Student student, Course course) {
        if (enrollmentRepository.existsByStudentAndCourse(student, course)) {
            return;
        }
        Enrollment enrollment = new Enrollment();
        enrollment.setStudent(student);
        enrollment.setCourse(course);
        enrollment.setEnrollDate(LocalDate.now());
        enrollmentRepository.save(enrollment);
    }

    public List<Enrollment> getEnrollmentsByStudent(Student student) {
        return enrollmentRepository.findByStudent(student);
    }

    public boolean isEnrolled(Student student, Course course) {
        return enrollmentRepository.existsByStudentAndCourse(student, course);
    }
}
