package com.example.Bai2.service;

import com.example.Bai2.Model.Role;
import com.example.Bai2.Model.Student;
import com.example.Bai2.repository.RoleRepository;
import com.example.Bai2.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(userRequest);

        String email = oAuth2User.getAttribute("email");
        String name = oAuth2User.getAttribute("name");

        // Tìm hoặc tạo student từ tài khoản Google
        Student student = studentRepository.findByEmail(email).orElseGet(() -> {
            Student newStudent = new Student();
            String username = email.split("@")[0];
            // Đảm bảo username là duy nhất
            if (studentRepository.existsByUsername(username)) {
                username = username + "_" + UUID.randomUUID().toString().substring(0, 4);
            }
            newStudent.setUsername(username);
            newStudent.setEmail(email);
            newStudent.setPassword(passwordEncoder.encode(UUID.randomUUID().toString()));
            Role studentRole = roleRepository.findByName("ROLE_STUDENT")
                    .orElseThrow(() -> new RuntimeException("Role STUDENT không tồn tại"));
            newStudent.setRoles(List.of(studentRole));
            return studentRepository.save(newStudent);
        });

        // Gán quyền từ database
        List<GrantedAuthority> authorities = student.getRoles().stream()
                .map(role -> new SimpleGrantedAuthority(role.getName()))
                .collect(Collectors.toList());

        return new DefaultOAuth2User(authorities, oAuth2User.getAttributes(), "email");
    }
}
