package com.ndky.studentmanagement.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ndky.studentmanagement.entity.Score;

import java.util.List;

public interface ScoreService extends IService<Score> {
    List<Score> getStudentScores(Integer sno);
    List<Score> getCourseScores(Integer cno);
} 