package com.ndky.studentmanagement.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ndky.studentmanagement.entity.Student;

import java.util.List;

public interface StudentService extends IService<Student> {
    public List<Student> searchStudents(String sno, String name, String dept, String sex, 
            int pageNum, int pageSize);

    public int countStudents(String sno, String name, String dept, String sex);
}