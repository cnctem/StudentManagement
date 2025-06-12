package com.ndky.gui;

import com.ndky.model.Score;
import com.ndky.service.ScoreService;
import com.ndky.service.StudentService;
import com.ndky.service.CourseService;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class ScoreManagementPanel extends JPanel {
    private JTextField studentNoField;
    private JTextField courseNoField;
    private JTextField scoreField;
    private JButton selectCourseButton;
    private JButton addScoreButton;
    private JButton updateScoreButton;
    private JTextArea resultArea;
    private ScoreService scoreService;
    private StudentService studentService;
    private CourseService courseService;

    public ScoreManagementPanel() {
        scoreService = new ScoreService();
        studentService = new StudentService();
        courseService = new CourseService();
        setLayout(new BorderLayout());
        initializeComponents();
        setupLayout();
        setupListeners();
    }

    private void initializeComponents() {
        studentNoField = new JTextField(10);
        courseNoField = new JTextField(10);
        scoreField = new JTextField(10);
        selectCourseButton = new JButton("选课");
        addScoreButton = new JButton("录入成绩");
        updateScoreButton = new JButton("修改成绩");
        resultArea = new JTextArea();
        resultArea.setEditable(false);
    }

    private void setupLayout() {
        // 控制面板
        JPanel controlPanel = new JPanel(new GridLayout(4, 2, 5, 5));
        controlPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        controlPanel.add(new JLabel("学号:"));
        controlPanel.add(studentNoField);
        controlPanel.add(new JLabel("课程号:"));
        controlPanel.add(courseNoField);
        controlPanel.add(new JLabel("成绩:"));
        controlPanel.add(scoreField);
        
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        buttonPanel.add(selectCourseButton);
        buttonPanel.add(addScoreButton);
        buttonPanel.add(updateScoreButton);
        controlPanel.add(buttonPanel);

        // 结果面板
        JScrollPane scrollPane = new JScrollPane(resultArea);
        scrollPane.setPreferredSize(new Dimension(400, 300));

        // 主面板布局
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        mainPanel.add(controlPanel, BorderLayout.NORTH);
        mainPanel.add(scrollPane, BorderLayout.CENTER);

        add(mainPanel, BorderLayout.CENTER);
    }

    private void setupListeners() {
        selectCourseButton.addActionListener(e -> selectCourse());
        addScoreButton.addActionListener(e -> addScore());
        updateScoreButton.addActionListener(e -> updateScore());
    }

    private void selectCourse() {
        String snoStr = studentNoField.getText().trim();
        String cnoStr = courseNoField.getText().trim();
        
        if (snoStr.isEmpty() || cnoStr.isEmpty()) {
            JOptionPane.showMessageDialog(this, "请输入学号和课程号", "提示", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            int sno = Integer.parseInt(snoStr);
            int cno = Integer.parseInt(cnoStr);
            
            // 检查学生和课程是否存在
            if (studentService.getStudent(sno) == null) {
                JOptionPane.showMessageDialog(this, "学生不存在", "错误", JOptionPane.ERROR_MESSAGE);
                return;
            }
            if (courseService.getCourse(cno) == null) {
                JOptionPane.showMessageDialog(this, "课程不存在", "错误", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // 检查是否已经选过这门课
            List<Score> existingScores = scoreService.getStudentScores(sno);
            for (Score score : existingScores) {
                if (score.getCno() == cno) {
                    JOptionPane.showMessageDialog(this, "该学生已经选过这门课程", "提示", JOptionPane.WARNING_MESSAGE);
                    return;
                }
            }

            // 创建新的选课记录（成绩初始为0）
            Score newScore = new Score();
            newScore.setSno(sno);
            newScore.setCno(cno);
            newScore.setGrade(0);
            
            scoreService.addScore(newScore);
            resultArea.setText("选课成功！\n学号: " + sno + "\n课程号: " + cno);
            
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "学号和课程号必须是数字", "错误", JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "选课失败: " + ex.getMessage(), "错误", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void addScore() {
        String snoStr = studentNoField.getText().trim();
        String cnoStr = courseNoField.getText().trim();
        String scoreStr = scoreField.getText().trim();
        
        if (snoStr.isEmpty() || cnoStr.isEmpty() || scoreStr.isEmpty()) {
            JOptionPane.showMessageDialog(this, "请填写完整信息", "提示", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            int sno = Integer.parseInt(snoStr);
            int cno = Integer.parseInt(cnoStr);
            int score = Integer.parseInt(scoreStr);
            
            if (score < 0 || score > 100) {
                JOptionPane.showMessageDialog(this, "成绩必须在0-100之间", "错误", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // 检查是否已经录入过成绩
            List<Score> existingScores = scoreService.getStudentScores(sno);
            for (Score existingScore : existingScores) {
                if (existingScore.getCno() == cno && existingScore.getGrade() > 0) {
                    JOptionPane.showMessageDialog(this, "该学生的这门课程成绩已经录入，请使用修改功能", "提示", JOptionPane.WARNING_MESSAGE);
                    return;
                }
            }

            // 更新成绩
            Score scoreObj = new Score();
            scoreObj.setSno(sno);
            scoreObj.setCno(cno);
            scoreObj.setGrade(score);
            
            scoreService.updateScore(scoreObj);
            resultArea.setText("成绩录入成功！\n学号: " + sno + "\n课程号: " + cno + "\n成绩: " + score);
            
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "学号、课程号和成绩必须是数字", "错误", JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "成绩录入失败: " + ex.getMessage(), "错误", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void updateScore() {
        String snoStr = studentNoField.getText().trim();
        String cnoStr = courseNoField.getText().trim();
        String scoreStr = scoreField.getText().trim();
        
        if (snoStr.isEmpty() || cnoStr.isEmpty() || scoreStr.isEmpty()) {
            JOptionPane.showMessageDialog(this, "请填写完整信息", "提示", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            int sno = Integer.parseInt(snoStr);
            int cno = Integer.parseInt(cnoStr);
            int score = Integer.parseInt(scoreStr);
            
            if (score < 0 || score > 100) {
                JOptionPane.showMessageDialog(this, "成绩必须在0-100之间", "错误", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // 检查是否存在该成绩记录
            List<Score> existingScores = scoreService.getStudentScores(sno);
            boolean found = false;
            for (Score existingScore : existingScores) {
                if (existingScore.getCno() == cno) {
                    found = true;
                    break;
                }
            }

            if (!found) {
                JOptionPane.showMessageDialog(this, "未找到该学生的这门课程记录", "错误", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // 更新成绩
            Score scoreObj = new Score();
            scoreObj.setSno(sno);
            scoreObj.setCno(cno);
            scoreObj.setGrade(score);
            
            scoreService.updateScore(scoreObj);
            resultArea.setText("成绩修改成功！\n学号: " + sno + "\n课程号: " + cno + "\n新成绩: " + score);
            
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "学号、课程号和成绩必须是数字", "错误", JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "成绩修改失败: " + ex.getMessage(), "错误", JOptionPane.ERROR_MESSAGE);
        }
    }
} 