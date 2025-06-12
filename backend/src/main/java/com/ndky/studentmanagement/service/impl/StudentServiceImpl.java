package com.ndky.studentmanagement.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ndky.studentmanagement.entity.Student;
import com.ndky.studentmanagement.mapper.StudentMapper;
import com.ndky.studentmanagement.service.StudentService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StudentServiceImpl extends ServiceImpl<StudentMapper, Student> implements StudentService {

    @Override
    public List<Student> searchStudents(String sno, String name, String dept, String sex, 
            int pageNum, int pageSize) {
        QueryWrapper<Student> queryWrapper = new QueryWrapper<>();
        
        if (sno != null && !sno.isEmpty()) {
            // 支持学号模糊查询
            queryWrapper.like("sno", sno);
        }
        if (name != null && !name.isEmpty()) {
            // 支持姓名模糊查询
            queryWrapper.like("sname", name);
        }
        if (dept != null && !dept.isEmpty()) {
            // 支持系别模糊查询
            queryWrapper.like("sdept", dept);
        }
        if (sex != null && !sex.isEmpty()) {
            // 性别精确匹配
            queryWrapper.eq("ssex", sex);
        }

        // 添加排序
        queryWrapper.orderByAsc("sno");

        Page<Student> page = new Page<>(pageNum, pageSize);
        return page(page, queryWrapper).getRecords();
    }

    @Override
    public int countStudents(String sno, String name, String dept, String sex) {
        QueryWrapper<Student> queryWrapper = new QueryWrapper<>();
        
        if (sno != null && !sno.isEmpty()) {
            queryWrapper.like("sno", sno);
        }
        if (name != null && !name.isEmpty()) {
            queryWrapper.like("sname", name);
        }
        if (dept != null && !dept.isEmpty()) {
            queryWrapper.like("sdept", dept);
        }
        if (sex != null && !sex.isEmpty()) {
            queryWrapper.eq("ssex", sex);
        }

        return (int) count(queryWrapper);
    }
}