package com.example.Bai2.config;

import com.example.Bai2.Model.*;
import com.example.Bai2.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired private RoleRepository roleRepository;
    @Autowired private StudentRepository studentRepository;
    @Autowired private CategoryRepository categoryRepository;
    @Autowired private CourseRepository courseRepository;
    @Autowired private EnrollmentRepository enrollmentRepository;
    @Autowired private PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {

        // --- ROLES ---
        Role adminRole = roleRepository.findByName("ROLE_ADMIN").orElseGet(() -> {
            Role r = new Role(); r.setName("ROLE_ADMIN");
            return roleRepository.save(r);
        });
        Role studentRole = roleRepository.findByName("ROLE_STUDENT").orElseGet(() -> {
            Role r = new Role(); r.setName("ROLE_STUDENT");
            return roleRepository.save(r);
        });

        // --- STUDENTS ---
        if (!studentRepository.existsByUsername("admin")) {
            Student admin = new Student();
            admin.setUsername("admin");
            admin.setPassword(passwordEncoder.encode("admin123"));
            admin.setEmail("admin@example.com");
            admin.setRoles(List.of(adminRole));
            studentRepository.save(admin);
            System.out.println(">>> Tài khoản: admin / admin123 [ROLE_ADMIN]");
        }

        Student student1 = studentRepository.findByUsername("student1").orElseGet(() -> {
            Student s = new Student();
            s.setUsername("student1");
            s.setPassword(passwordEncoder.encode("student123"));
            s.setEmail("student1@example.com");
            s.setRoles(List.of(studentRole));
            System.out.println(">>> Tài khoản: student1 / student123 [ROLE_STUDENT]");
            return studentRepository.save(s);
        });

        // --- CATEGORIES & COURSES (chỉ tạo nếu chưa có) ---
        if (categoryRepository.count() == 0) {
            Category cntt = save("Công nghệ thông tin");
            Category kt   = save("Kinh tế - Quản trị");
            Category nn   = save("Ngoại ngữ");
            Category kyt  = save("Kỹ thuật");
            Category khtn = save("Khoa học tự nhiên");
            System.out.println(">>> Đã tạo 5 danh mục");

            if (courseRepository.count() == 0) {
                saveCourse("Lập trình Java cơ bản",         3, "Nguyễn Văn An",    cntt);
                saveCourse("Lập trình Web với Spring Boot", 3, "Trần Thị Bình",    cntt);
                saveCourse("Cơ sở dữ liệu",                 3, "Lê Văn Cường",     cntt);
                saveCourse("Mạng máy tính",                 2, "Phạm Thị Dung",    cntt);
                saveCourse("Cấu trúc dữ liệu & Giải thuật", 3, "Hoàng Văn Em",    cntt);
                saveCourse("Lập trình Python",              2, "Nguyễn Thị Fương", cntt);
                saveCourse("Phát triển ứng dụng Mobile",    3, "Trần Văn Giang",   cntt);
                saveCourse("Kinh tế vi mô",                 3, "Lê Thị Hoa",       kt);
                saveCourse("Kinh tế vĩ mô",                 3, "Phạm Văn Ích",     kt);
                saveCourse("Quản trị doanh nghiệp",         3, "Hoàng Thị Kim",    kt);
                saveCourse("Kế toán cơ bản",                2, "Nguyễn Văn Long",  kt);
                saveCourse("Tiếng Anh giao tiếp 1",         3, "Lê Văn Nam",       nn);
                saveCourse("Tiếng Anh giao tiếp 2",         3, "Phạm Thị Oanh",    nn);
                saveCourse("Tiếng Nhật cơ bản",             3, "Hoàng Văn Phú",    nn);
                saveCourse("Kỹ thuật điện tử",              3, "Nguyễn Thị Quỳnh", kyt);
                saveCourse("Kỹ thuật số",                   2, "Trần Văn Rạng",    kyt);
                saveCourse("Toán cao cấp A1",               4, "Lê Thị Sương",     khtn);
                saveCourse("Vật lý đại cương",              3, "Phạm Văn Tú",      khtn);
                saveCourse("Xác suất thống kê",             3, "Hoàng Thị Uyên",   khtn);
                System.out.println(">>> Đã tạo 19 học phần mẫu");

                // --- ENROLLMENTS mẫu cho student1 ---
                if (enrollmentRepository.count() == 0) {
                    List<String> enrollNames = List.of(
                            "Lập trình Java cơ bản",
                            "Cơ sở dữ liệu",
                            "Tiếng Anh giao tiếp 1",
                            "Toán cao cấp A1"
                    );
                    courseRepository.findAll().stream()
                            .filter(c -> enrollNames.contains(c.getName()))
                            .forEach(c -> {
                                Enrollment e = new Enrollment();
                                e.setStudent(student1);
                                e.setCourse(c);
                                e.setEnrollDate(LocalDate.now().minusDays(3));
                                enrollmentRepository.save(e);
                            });
                    System.out.println(">>> Đã tạo 4 enrollment mẫu cho student1");
                }
            }
        }
    }

    private Category save(String name) {
        Category c = new Category(); c.setName(name);
        return categoryRepository.save(c);
    }

    private void saveCourse(String name, int credits, String lecturer, Category category) {
        Course c = new Course();
        c.setName(name); c.setCredits(credits);
        c.setLecturer(lecturer); c.setCategory(category);
        courseRepository.save(c);
    }
}
