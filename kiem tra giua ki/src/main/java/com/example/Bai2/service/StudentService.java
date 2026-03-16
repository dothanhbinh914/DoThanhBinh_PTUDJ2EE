package com.example.Bai2.service;

import com.example.Bai2.Model.Role;
import com.example.Bai2.Model.Student;
import com.example.Bai2.repository.RoleRepository;
import com.example.Bai2.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class StudentService implements UserDetailsService {

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Student student = studentRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Không tìm thấy tài khoản: " + username));

        List<GrantedAuthority> authorities = student.getRoles().stream()
                .map(role -> new SimpleGrantedAuthority(role.getName()))
                .collect(Collectors.toList());

        return new org.springframework.security.core.userdetails.User(
                student.getUsername(), student.getPassword(), authorities);
    }

    public void registerStudent(String username, String rawPassword, String email) {
        Student student = new Student();
        student.setUsername(username);
        student.setPassword(passwordEncoder.encode(rawPassword));
        student.setEmail(email);

        Role studentRole = roleRepository.findByName("ROLE_STUDENT")
                .orElseThrow(() -> new RuntimeException("Role STUDENT không tồn tại"));
        student.setRoles(List.of(studentRole));

        studentRepository.save(student);
    }

    public boolean existsByUsername(String username) {
        return studentRepository.existsByUsername(username);
    }

    public boolean existsByEmail(String email) {
        return studentRepository.existsByEmail(email);
    }

    public Student findByUsername(String username) {
        return studentRepository.findByUsername(username).orElse(null);
    }

    public Student findByEmail(String email) {
        return studentRepository.findByEmail(email).orElse(null);
    }
}
