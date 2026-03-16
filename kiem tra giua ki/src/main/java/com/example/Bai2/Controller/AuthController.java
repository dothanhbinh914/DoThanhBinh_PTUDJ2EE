package com.example.Bai2.Controller;

import com.example.Bai2.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class AuthController {

    @Autowired
    private StudentService studentService;

    @GetMapping("/register")
    public String showRegisterForm() {
        return "auth/register";
    }

    @PostMapping("/register")
    public String register(@RequestParam String username,
                           @RequestParam String password,
                           @RequestParam String email,
                           Model model) {

        if (studentService.existsByUsername(username)) {
            model.addAttribute("error", "Username đã tồn tại!");
            return "auth/register";
        }

        if (studentService.existsByEmail(email)) {
            model.addAttribute("error", "Email đã được sử dụng!");
            return "auth/register";
        }

        studentService.registerStudent(username, password, email);
        return "redirect:/login?registered";
    }
}
