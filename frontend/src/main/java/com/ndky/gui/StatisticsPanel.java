package com.ndky.gui;

import com.ndky.model.Score;
import com.ndky.service.ScoreService;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.data.general.DefaultPieDataset;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class StatisticsPanel extends JPanel {
    private JTextField statsStudentSnoField;
    private JTextField statsCourseCnoField;
    private JButton queryStudentScoresButton;
    private JButton queryCourseScoresButton;
    private JButton calculateStudentAvgButton;
    private JButton calculateCourseStatsButton;
    private JButton showScoreChartButton;
    private JTextArea statsResultArea;
    private JPanel chartPanelContainer;
    private ScoreService scoreService;

    public StatisticsPanel() {
        scoreService = new ScoreService();
        setLayout(new BorderLayout());
        initializeComponents();
        setupLayout();
        setupListeners();
    }

    private void initializeComponents() {
        statsStudentSnoField = new JTextField(10);
        statsCourseCnoField = new JTextField(10);
        queryStudentScoresButton = new JButton("查询学生所有成绩");
        queryCourseScoresButton = new JButton("查询课程所有学生成绩");
        calculateStudentAvgButton = new JButton("计算学生平均分");
        calculateCourseStatsButton = new JButton("计算课程统计");
        showScoreChartButton = new JButton("显示成绩分布饼图");
        statsResultArea = new JTextArea();
        statsResultArea.setEditable(false);
        chartPanelContainer = new JPanel(new BorderLayout());
        chartPanelContainer.setPreferredSize(new Dimension(500, 400));
    }

    private void setupLayout() {
        JPanel controlPanel = new JPanel(new FlowLayout());
        controlPanel.add(new JLabel("学号:"));
        controlPanel.add(statsStudentSnoField);
        controlPanel.add(new JLabel("课程号:"));
        controlPanel.add(statsCourseCnoField);
        controlPanel.add(queryStudentScoresButton);
        controlPanel.add(queryCourseScoresButton);
        controlPanel.add(calculateStudentAvgButton);
        controlPanel.add(calculateCourseStatsButton);
        controlPanel.add(showScoreChartButton);

        JPanel resultPanel = new JPanel(new GridLayout(1, 2, 10, 0));
        JScrollPane textScrollPane = new JScrollPane(statsResultArea);
        textScrollPane.setPreferredSize(new Dimension(300, 400));
        resultPanel.add(chartPanelContainer);
        resultPanel.add(textScrollPane);

        add(controlPanel, BorderLayout.NORTH);
        add(resultPanel, BorderLayout.CENTER);
    }

    private void setupListeners() {
        queryStudentScoresButton.addActionListener(e -> queryStudentScores());
        queryCourseScoresButton.addActionListener(e -> queryCourseScores());
        calculateStudentAvgButton.addActionListener(e -> calculateStudentAvg());
        calculateCourseStatsButton.addActionListener(e -> calculateCourseStats());
        showScoreChartButton.addActionListener(e -> showScoreDistributionChart());
    }

    private void queryStudentScores() {
        String sno = statsStudentSnoField.getText().trim();
        if (sno.isEmpty()) {
            JOptionPane.showMessageDialog(this, "请输入学号", "提示", JOptionPane.WARNING_MESSAGE);
            return;
        }
        try {
            List<Score> scores = scoreService.getStudentScores(Integer.parseInt(sno));
            StringBuilder result = new StringBuilder();
            if (scores.isEmpty()) {
                result.append("未找到学号为 ").append(sno).append(" 的学生成绩。");
            } else {
                result.append("学号为 ").append(sno).append(" 的学生成绩：\n");
                result.append("----------------------------------\n");
                for (Score score : scores) {
                    result.append("课程号: ").append(score.getCno())
                          .append(", 课程名称: ").append(score.getCname())
                          .append(", 成绩: ").append(score.getGrade())
                          .append("\n");
                }
            }
            statsResultArea.setText(result.toString());
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "查询学生成绩失败: " + e.getMessage(), "错误", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    private void queryCourseScores() {
        String cno = statsCourseCnoField.getText().trim();
        if (cno.isEmpty()) {
            JOptionPane.showMessageDialog(this, "请输入课程号", "提示", JOptionPane.WARNING_MESSAGE);
            return;
        }
        try {
            List<Score> scores = scoreService.getCourseScores(Integer.parseInt(cno));
            StringBuilder result = new StringBuilder();
            if (scores.isEmpty()) {
                result.append("未找到课程号为 ").append(cno).append(" 的成绩记录。");
            } else {
                result.append("课程号为 ").append(cno).append(" 的学生成绩：\n");
                result.append("----------------------------------\n");
                for (Score score : scores) {
                    result.append("学号: ").append(score.getSno())
                          .append(", 姓名: ").append(score.getSname())
                          .append(", 成绩: ").append(score.getGrade())
                          .append("\n");
                }
            }
            statsResultArea.setText(result.toString());
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "查询课程成绩失败: " + e.getMessage(), "错误", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    private void calculateStudentAvg() {
        String sno = statsStudentSnoField.getText().trim();
        if (sno.isEmpty()) {
            JOptionPane.showMessageDialog(this, "请输入学号", "提示", JOptionPane.WARNING_MESSAGE);
            return;
        }
        try {
            List<Score> scores = scoreService.getStudentScores(Integer.parseInt(sno));
            if (scores.isEmpty()) {
                statsResultArea.setText("未找到学号为 " + sno + " 的学生成绩。");
                return;
            }

            double totalScore = 0;
            int courseCount = 0;
            StringBuilder details = new StringBuilder();
            details.append("学号为 ").append(sno).append(" 的成绩统计：\n");
            details.append("----------------------------------\n");

            for (Score score : scores) {
                totalScore += score.getGrade();
                courseCount++;
                details.append("课程: ").append(score.getCname())
                      .append(", 成绩: ").append(score.getGrade()).append("\n");
            }

            double average = totalScore / courseCount;
            details.append("----------------------------------\n");
            details.append(String.format("平均分: %.2f\n", average));
            details.append("总课程数: ").append(courseCount);

            statsResultArea.setText(details.toString());
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "计算平均分失败: " + e.getMessage(), "错误", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    private void calculateCourseStats() {
        String cno = statsCourseCnoField.getText().trim();
        if (cno.isEmpty()) {
            JOptionPane.showMessageDialog(this, "请输入课程号", "提示", JOptionPane.WARNING_MESSAGE);
            return;
        }
        try {
            List<Score> scores = scoreService.getCourseScores(Integer.parseInt(cno));
            if (scores.isEmpty()) {
                statsResultArea.setText("未找到课程号为 " + cno + " 的成绩记录。");
                return;
            }

            double totalScore = 0;
            int studentCount = 0;
            int maxScore = Integer.MIN_VALUE;
            int minScore = Integer.MAX_VALUE;
            StringBuilder details = new StringBuilder();
            details.append("课程号为 ").append(cno).append(" 的成绩统计：\n");
            details.append("----------------------------------\n");

            for (Score score : scores) {
                int grade = score.getGrade();
                totalScore += grade;
                studentCount++;
                maxScore = Math.max(maxScore, grade);
                minScore = Math.min(minScore, grade);
                details.append("学生: ").append(score.getSname())
                      .append(", 成绩: ").append(grade).append("\n");
            }

            double average = totalScore / studentCount;
            details.append("----------------------------------\n");
            details.append(String.format("平均分: %.2f\n", average));
            details.append("最高分: ").append(maxScore).append("\n");
            details.append("最低分: ").append(minScore).append("\n");
            details.append("总人数: ").append(studentCount);

            statsResultArea.setText(details.toString());
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "计算课程统计失败: " + e.getMessage(), "错误", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    private void showScoreDistributionChart() {
        String cno = statsCourseCnoField.getText().trim();
        if (cno.isEmpty()) {
            JOptionPane.showMessageDialog(this, "请输入课程号", "提示", JOptionPane.WARNING_MESSAGE);
            return;
        }
        try {
            List<Score> scores = scoreService.getCourseScores(Integer.parseInt(cno));
            if (scores.isEmpty()) {
                statsResultArea.setText("未找到课程号为 " + cno + " 的成绩记录。");
                return;
            }

            int excellent = 0; // 90-100
            int good = 0;      // 80-89
            int fair = 0;      // 70-79
            int pass = 0;      // 60-69
            int fail = 0;      // 0-59

            for (Score score : scores) {
                int grade = score.getGrade();
                if (grade >= 90) excellent++;
                else if (grade >= 80) good++;
                else if (grade >= 70) fair++;
                else if (grade >= 60) pass++;
                else fail++;
            }

            DefaultPieDataset dataset = new DefaultPieDataset();
            if (excellent > 0) dataset.setValue("优秀(90-100)", excellent);
            if (good > 0) dataset.setValue("良好(80-89)", good);
            if (fair > 0) dataset.setValue("中等(70-79)", fair);
            if (pass > 0) dataset.setValue("及格(60-69)", pass);
            if (fail > 0) dataset.setValue("不及格(0-59)", fail);

            JFreeChart chart = ChartFactory.createPieChart(
                "课程成绩分布",
                dataset,
                true,
                true,
                false
            );

            ChartPanel chartPanel = new ChartPanel(chart);
            chartPanel.setPreferredSize(new Dimension(500, 400));
            chartPanel.setMouseWheelEnabled(true);

            chartPanelContainer.removeAll();
            chartPanelContainer.add(chartPanel, BorderLayout.CENTER);
            chartPanelContainer.revalidate();
            chartPanelContainer.repaint();

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "生成成绩分布饼图失败: " + e.getMessage(), "错误", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
            chartPanelContainer.removeAll();
            chartPanelContainer.revalidate();
            chartPanelContainer.repaint();
        }
    }
} 