package com.example.Bai2.config;

import com.example.Bai2.Model.Account;
import com.example.Bai2.Model.Role;
import com.example.Bai2.repository.AccountRepository;
import com.example.Bai2.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        // Tạo role nếu chưa có
        Role adminRole = roleRepository.findByName("ROLE_ADMIN").orElseGet(() -> {
            Role r = new Role();
            r.setName("ROLE_ADMIN");
            return roleRepository.save(r);
        });

        Role userRole = roleRepository.findByName("ROLE_USER").orElseGet(() -> {
            Role r = new Role();
            r.setName("ROLE_USER");
            return roleRepository.save(r);
        });

        // Tạo tài khoản admin nếu chưa có
        if (accountRepository.findByLoginName("admin").isEmpty()) {
            Account admin = new Account();
            admin.setLogin_name("admin");
            admin.setPassword(passwordEncoder.encode("admin123"));
            admin.setRoles(List.of(adminRole));
            accountRepository.save(admin);
            System.out.println(">>> Đã tạo tài khoản: admin / admin123 [ROLE_ADMIN]");
        }

        // Tạo tài khoản user1 nếu chưa có
        if (accountRepository.findByLoginName("user1").isEmpty()) {
            Account user = new Account();
            user.setLogin_name("user1");
            user.setPassword(passwordEncoder.encode("user123"));
            user.setRoles(List.of(userRole));
            accountRepository.save(user);
            System.out.println(">>> Đã tạo tài khoản: user1 / user123 [ROLE_USER]");
        }

        // Tạo tài khoản admin1 nếu chưa có
        if (accountRepository.findByLoginName("admin1").isEmpty()) {
            Account admin1 = new Account();
            admin1.setLogin_name("admin1");
            admin1.setPassword(passwordEncoder.encode("admin123"));
            admin1.setRoles(List.of(adminRole));
            accountRepository.save(admin1);
            System.out.println(">>> Đã tạo tài khoản: admin1 / admin123 [ROLE_ADMIN]");
        }
    }
}
