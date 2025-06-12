package com.ndky.studentmanagement.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ndky.studentmanagement.entity.Student;
import com.ndky.studentmanagement.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    public Page<Student> listStudents() {
        Page<Student> page = new Page<>(1, Integer.MAX_VALUE);
        return studentService.page(page);
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
    public ResponseEntity<Map<String, Object>> searchStudents(
            @RequestParam(required = false) String sno,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String dept,
            @RequestParam(required = false) String sex,
            @RequestParam(defaultValue = "1") int pageNum,
            @RequestParam(defaultValue = "10") int pageSize) {
        try {
            // 检查是否所有条件都为空
            boolean allEmpty = (sno == null || sno.isEmpty()) &&
                             (name == null || name.isEmpty()) &&
                             (dept == null || dept.isEmpty()) &&
                             (sex == null || sex.isEmpty());
            
            List<Student> students;
            if (allEmpty) {
                // 如果所有条件都为空，获取所有学生
                Page<Student> page = new Page<>(pageNum, pageSize);
                page = studentService.page(page);
                students = page.getRecords();
            } else {
                // 否则进行条件查询
                students = studentService.searchStudents(sno, name, dept, sex, pageNum, pageSize);
            }
            
            Map<String, Object> response = new HashMap<>();
            response.put("students", students);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    // 获取符合条件的记录总数
    @GetMapping("/count")
    public ResponseEntity<Map<String, Object>> countStudents(
            @RequestParam(required = false) String sno,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String dept,
            @RequestParam(required = false) String sex) {
        try {
            // 检查是否所有条件都为空
            boolean allEmpty = (sno == null || sno.isEmpty()) &&
                             (name == null || name.isEmpty()) &&
                             (dept == null || dept.isEmpty()) &&
                             (sex == null || sex.isEmpty());
            
            long total;
            if (allEmpty) {
                // 如果所有条件都为空，获取总数
                total = studentService.count();
            } else {
                // 否则进行条件统计
                total = studentService.countStudents(sno, name, dept, sex);
            }
            
            return ResponseEntity.ok(Map.of("total", total));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }
}
