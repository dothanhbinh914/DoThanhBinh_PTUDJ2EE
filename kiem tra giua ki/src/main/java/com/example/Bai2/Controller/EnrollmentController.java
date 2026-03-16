package com.example.Bai2.Controller;

import com.example.Bai2.Model.Course;
import com.example.Bai2.Model.Enrollment;
import com.example.Bai2.Model.Student;
import com.example.Bai2.service.CourseService;
import com.example.Bai2.service.EnrollmentService;
import com.example.Bai2.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/enroll")
public class EnrollmentController {

    @Autowired
    private EnrollmentService enrollmentService;

    @Autowired
    private CourseService courseService;

    @Autowired
    private StudentService studentService;

    @GetMapping("/course/{courseId}")
    public String enrollCourse(@PathVariable Long courseId, Authentication authentication) {
        Student student = resolveStudent(authentication);
        Course course = courseService.getCourseById(courseId);
        enrollmentService.enroll(student, course);
        return "redirect:/home?enrolled";
    }

    @GetMapping("/my-courses")
    public String myCourses(Authentication authentication, Model model) {
        Student student = resolveStudent(authentication);
        List<Enrollment> enrollments = enrollmentService.getEnrollmentsByStudent(student);
        model.addAttribute("enrollments", enrollments);
        model.addAttribute("student", student);
        return "enroll/my-courses";
    }

    private Student resolveStudent(Authentication authentication) {
        if (authentication.getPrincipal() instanceof OAuth2User oAuth2User) {
            String email = oAuth2User.getAttribute("email");
            return studentService.findByEmail(email);
        }
        return studentService.findByUsername(authentication.getName());
    }
}
