package com.ndky.studentmanagement.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ndky.studentmanagement.entity.Score;
import com.ndky.studentmanagement.mapper.ScoreMapper;
import com.ndky.studentmanagement.service.ScoreService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ScoreServiceImpl extends ServiceImpl<ScoreMapper, Score> implements ScoreService {
    
    @Override
    public List<Score> getStudentScores(Integer sno) {
        QueryWrapper<Score> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("sno", sno);
        return list(queryWrapper);
    }

    @Override
    public List<Score> getCourseScores(Integer cno) {
        QueryWrapper<Score> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("cno", cno);
        return list(queryWrapper);
    }
} 