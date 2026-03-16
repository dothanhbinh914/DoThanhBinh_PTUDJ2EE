package com.example.Bai2.Controller;

import com.example.Bai2.Model.Category;
import com.example.Bai2.Model.Course;
import com.example.Bai2.service.CategoryService;
import com.example.Bai2.service.CourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Controller
@RequestMapping("/admin/courses")
public class CourseController {

    @Autowired
    private CourseService courseService;

    @Autowired
    private CategoryService categoryService;

    @GetMapping
    public String listCourses(Model model) {
        model.addAttribute("courses", courseService.getAllCourses());
        return "admin/courses";
    }

    @GetMapping("/add")
    public String showAddForm(Model model) {
        model.addAttribute("course", new Course());
        model.addAttribute("categories", categoryService.getAllCategories());
        return "admin/add-course";
    }

    @PostMapping("/add")
    public String addCourse(@ModelAttribute Course course,
                            @RequestParam(value = "imageFile", required = false) MultipartFile imageFile,
                            Model model) {
        try {
            courseService.saveCourse(course, imageFile);
            return "redirect:/admin/courses?success=add";
        } catch (IOException e) {
            model.addAttribute("error", "Lỗi khi tải ảnh lên: " + e.getMessage());
            model.addAttribute("categories", categoryService.getAllCategories());
            return "admin/add-course";
        }
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        Course course = courseService.getCourseById(id);
        List<Category> categories = categoryService.getAllCategories();
        model.addAttribute("course", course);
        model.addAttribute("categories", categories);
        return "admin/edit-course";
    }

    @PostMapping("/edit")
    public String updateCourse(@ModelAttribute Course course,
                               @RequestParam(value = "imageFile", required = false) MultipartFile imageFile,
                               @RequestParam(value = "existingImage", required = false) String existingImage,
                               Model model) {
        try {
            if (imageFile == null || imageFile.isEmpty()) {
                course.setImage(existingImage);
            }
            courseService.saveCourse(course, imageFile);
            return "redirect:/admin/courses?success=edit";
        } catch (IOException e) {
            model.addAttribute("error", "Lỗi khi tải ảnh lên: " + e.getMessage());
            model.addAttribute("categories", categoryService.getAllCategories());
            return "admin/edit-course";
        }
    }

    @GetMapping("/delete/{id}")
    public String deleteCourse(@PathVariable Long id) {
        courseService.deleteCourse(id);
        return "redirect:/admin/courses?success=delete";
    }
}
