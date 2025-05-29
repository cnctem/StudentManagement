package com.ndky.studentmanagement.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ndky.studentmanagement.entity.Student;
import com.ndky.studentmanagement.mapper.StudentMapper;
import com.ndky.studentmanagement.service.StudentService;
import org.springframework.stereotype.Service;

@Service
public class StudentServiceImpl extends ServiceImpl<StudentMapper, Student> implements StudentService {
}