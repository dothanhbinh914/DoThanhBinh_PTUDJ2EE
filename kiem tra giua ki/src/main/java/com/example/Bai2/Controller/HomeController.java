package com.example.Bai2.Controller;

import com.example.Bai2.Model.Course;
import com.example.Bai2.Model.Student;
import com.example.Bai2.service.CourseService;
import com.example.Bai2.service.EnrollmentService;
import com.example.Bai2.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Set;
import java.util.stream.Collectors;

@Controller
public class HomeController {

    @Autowired
    private CourseService courseService;

    @Autowired
    private EnrollmentService enrollmentService;

    @Autowired
    private StudentService studentService;

    @GetMapping({"/", "/home", "/courses"})
    public String home(
            @RequestParam(defaultValue = "") String keyword,
            @RequestParam(defaultValue = "0") int page,
            Model model,
            Authentication authentication) {

        Page<Course> coursePage = courseService.getCourses(keyword, page, 5);

        model.addAttribute("courses", coursePage.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", coursePage.getTotalPages());
        model.addAttribute("keyword", keyword);

        // Truyền danh sách ID học phần đã đăng ký của sinh viên
        if (authentication != null && authentication.isAuthenticated()) {
            Student student = resolveStudent(authentication);
            if (student != null) {
                Set<Long> enrolledIds = enrollmentService.getEnrollmentsByStudent(student)
                        .stream()
                        .map(e -> e.getCourse().getId())
                        .collect(Collectors.toSet());
                model.addAttribute("enrolledIds", enrolledIds);
            }
        }

        return "index";
    }

    @GetMapping("/login")
    public String login() {
        return "auth/login";
    }

    @GetMapping("/access-denied")
    public String accessDenied() {
        return "access-denied";
    }

    private Student resolveStudent(Authentication authentication) {
        if (authentication.getPrincipal() instanceof OAuth2User oAuth2User) {
            String email = oAuth2User.getAttribute("email");
            return studentService.findByEmail(email);
        }
        return studentService.findByUsername(authentication.getName());
    }
}
