package com.ndky.studentmanagement.controller;

import com.ndky.studentmanagement.entity.Score;
import com.ndky.studentmanagement.service.ScoreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/score")
public class ScoreController {

    @Autowired
    private ScoreService scoreService;

    @GetMapping("/list")
    public ResponseEntity<List<Score>> getAllScores() {
        return ResponseEntity.ok(scoreService.list());
    }

    @GetMapping("/student/{sno}")
    public ResponseEntity<List<Score>> getStudentScores(@PathVariable Integer sno) {
        return ResponseEntity.ok(scoreService.getStudentScores(sno));
    }

    @GetMapping("/course/{cno}")
    public ResponseEntity<List<Score>> getCourseScores(@PathVariable Integer cno) {
        return ResponseEntity.ok(scoreService.getCourseScores(cno));
    }

    @PostMapping
    public ResponseEntity<Boolean> addScore(@RequestBody Score score) {
        return ResponseEntity.ok(scoreService.save(score));
    }

    @PutMapping
    public ResponseEntity<Boolean> updateScore(@RequestBody Score score) {
        return ResponseEntity.ok(scoreService.updateById(score));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Boolean> deleteScore(@PathVariable Integer id) {
        return ResponseEntity.ok(scoreService.removeById(id));
    }
} 