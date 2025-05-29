package com.ndky.studentmanagement.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ndky.studentmanagement.entity.Student;
import com.ndky.studentmanagement.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/student")
public class StudentController {

    @Autowired
    private StudentService studentService;

    // 添加学生
    @PostMapping("/add")
    public boolean addStudent(@RequestBody Student student) {
        return studentService.save(student);
    }

    // 删除学生
    @DeleteMapping("/delete/{id}")
    public boolean deleteStudent(@PathVariable Integer id) {
        return studentService.removeById(id);
    }

    // 更新学生信息
    @PutMapping("/update")
    public boolean updateStudent(@RequestBody Student student) {
        return studentService.updateById(student);
    }

    // 查询所有学生
    @GetMapping("/list")
    public List<Student> listStudents() {
        return studentService.list();
    }

    // 根据ID查询学生
    @GetMapping("/get/{id}")
    public Student getStudentById(@PathVariable Integer id) {
        return studentService.getById(id);
    }

    // 分页查询
    @GetMapping("/page")
    public Page<Student> pageStudents(@RequestParam(defaultValue = "1") Integer pageNum,
                                      @RequestParam(defaultValue = "10") Integer pageSize) {
        Page<Student> page = new Page<>(pageNum, pageSize);
        return studentService.page(page);
    }

    // 条件查询
    @GetMapping("/search")
    public List<Student> searchStudents(@RequestParam(required = false) String name,
                                        @RequestParam(required = false) String dept) {
        QueryWrapper<Student> queryWrapper = new QueryWrapper<>();
        if (name != null && !name.isEmpty()) {
            queryWrapper.like("sname", name);
        }
        if (dept != null && !dept.isEmpty()) {
            queryWrapper.eq("sdept", dept);
        }
        return studentService.list(queryWrapper);
    }
}
