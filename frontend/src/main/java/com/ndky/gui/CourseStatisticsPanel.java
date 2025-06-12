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

public class CourseStatisticsPanel extends JPanel {
    private JTextField courseCnoField;
    private JButton queryScoresButton;
    private JButton calculateStatsButton;
    private JTextArea resultArea;
    private JPanel chartPanelContainer;
    private ScoreService scoreService;

    public CourseStatisticsPanel() {
        scoreService = new ScoreService();
        setLayout(new BorderLayout());
        initializeComponents();
        setupLayout();
        setupListeners();
    }

    private void initializeComponents() {
        courseCnoField = new JTextField(10);
        queryScoresButton = new JButton("查询课程所有学生成绩");
        calculateStatsButton = new JButton("计算课程统计");
        resultArea = new JTextArea();
        resultArea.setEditable(false);
        chartPanelContainer = new JPanel(new BorderLayout());
        chartPanelContainer.setPreferredSize(new Dimension(500, 400));
    }

    private void setupLayout() {
        JPanel controlPanel = new JPanel(new FlowLayout());
        controlPanel.add(new JLabel("课程号:"));
        controlPanel.add(courseCnoField);
        controlPanel.add(queryScoresButton);
        controlPanel.add(calculateStatsButton);

        JPanel resultPanel = new JPanel(new GridLayout(1, 2, 10, 0));
        JScrollPane textScrollPane = new JScrollPane(resultArea);
        textScrollPane.setPreferredSize(new Dimension(300, 400));
        resultPanel.add(chartPanelContainer);
        resultPanel.add(textScrollPane);

        add(controlPanel, BorderLayout.NORTH);
        add(resultPanel, BorderLayout.CENTER);
    }

    private void setupListeners() {
        queryScoresButton.addActionListener(e -> queryCourseScores());
        calculateStatsButton.addActionListener(e -> calculateCourseStats());
    }

    private void queryCourseScores() {
        String cno = courseCnoField.getText().trim();
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
            resultArea.setText(result.toString());
            showCourseScoreChart(scores);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "查询课程成绩失败: " + e.getMessage(), "错误", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    private void calculateCourseStats() {
        String cno = courseCnoField.getText().trim();
        if (cno.isEmpty()) {
            JOptionPane.showMessageDialog(this, "请输入课程号", "提示", JOptionPane.WARNING_MESSAGE);
            return;
        }
        try {
            List<Score> scores = scoreService.getCourseScores(Integer.parseInt(cno));
            if (scores.isEmpty()) {
                resultArea.setText("未找到课程号为 " + cno + " 的成绩记录。");
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
            }

            double average = studentCount > 0 ? totalScore / studentCount : 0;
            details.append(String.format("平均分: %.2f\n", average));
            details.append("最高分: ").append(maxScore == Integer.MIN_VALUE ? "无" : maxScore).append("\n");
            details.append("最低分: ").append(minScore == Integer.MAX_VALUE ? "无" : minScore).append("\n");
            details.append("总人数: ").append(studentCount);

            resultArea.setText(details.toString());
            showCourseScoreChart(scores);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "计算课程统计失败: " + e.getMessage(), "错误", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    private void showCourseScoreChart(List<Score> scores) {
        if (scores.isEmpty()) {
            chartPanelContainer.removeAll();
            chartPanelContainer.revalidate();
            chartPanelContainer.repaint();
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

        // 设置图表背景为白色
        chart.setBackgroundPaint(Color.WHITE);
        chart.getPlot().setBackgroundPaint(Color.WHITE);
        chart.getPlot().setOutlinePaint(Color.WHITE);

        ChartPanel chartPanel = new ChartPanel(chart);
        chartPanel.setPreferredSize(new Dimension(500, 400));
        chartPanel.setMouseWheelEnabled(true);
        chartPanel.setBackground(Color.WHITE);

        chartPanelContainer.removeAll();
        chartPanelContainer.add(chartPanel, BorderLayout.CENTER);
        chartPanelContainer.revalidate();
        chartPanelContainer.repaint();
    }
} 