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

public class StudentStatisticsPanel extends JPanel {
    private JTextField studentSnoField;
    private JButton queryScoresButton;
    private JButton calculateAvgButton;
    private JTextArea resultArea;
    private JPanel chartPanelContainer;
    private ScoreService scoreService;

    public StudentStatisticsPanel() {
        scoreService = new ScoreService();
        setLayout(new BorderLayout());
        initializeComponents();
        setupLayout();
        setupListeners();
    }

    private void initializeComponents() {
        studentSnoField = new JTextField(10);
        queryScoresButton = new JButton("查询学生所有成绩");
        calculateAvgButton = new JButton("计算学生平均分");
        resultArea = new JTextArea();
        resultArea.setEditable(false);
        chartPanelContainer = new JPanel(new BorderLayout());
        chartPanelContainer.setPreferredSize(new Dimension(500, 400));
    }

    private void setupLayout() {
        JPanel controlPanel = new JPanel(new FlowLayout());
        controlPanel.add(new JLabel("学号:"));
        controlPanel.add(studentSnoField);
        controlPanel.add(queryScoresButton);
        controlPanel.add(calculateAvgButton);

        JPanel resultPanel = new JPanel(new GridLayout(1, 2, 10, 0));
        JScrollPane textScrollPane = new JScrollPane(resultArea);
        textScrollPane.setPreferredSize(new Dimension(300, 400));
        resultPanel.add(chartPanelContainer);
        resultPanel.add(textScrollPane);

        add(controlPanel, BorderLayout.NORTH);
        add(resultPanel, BorderLayout.CENTER);
    }

    private void setupListeners() {
        queryScoresButton.addActionListener(e -> queryStudentScores());
        calculateAvgButton.addActionListener(e -> calculateStudentAvg());
    }

    private void queryStudentScores() {
        String sno = studentSnoField.getText().trim();
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
            resultArea.setText(result.toString());
            showStudentScoreChart(scores);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "查询学生成绩失败: " + e.getMessage(), "错误", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    private void calculateStudentAvg() {
        String sno = studentSnoField.getText().trim();
        if (sno.isEmpty()) {
            JOptionPane.showMessageDialog(this, "请输入学号", "提示", JOptionPane.WARNING_MESSAGE);
            return;
        }
        try {
            List<Score> scores = scoreService.getStudentScores(Integer.parseInt(sno));
            if (scores.isEmpty()) {
                resultArea.setText("未找到学号为 " + sno + " 的学生成绩。");
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

            resultArea.setText(details.toString());
            showStudentScoreChart(scores);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "计算平均分失败: " + e.getMessage(), "错误", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    private void showStudentScoreChart(List<Score> scores) {
        if (scores.isEmpty()) {
            chartPanelContainer.removeAll();
            chartPanelContainer.revalidate();
            chartPanelContainer.repaint();
            return;
        }

        DefaultPieDataset dataset = new DefaultPieDataset();
        for (Score score : scores) {
            dataset.setValue(score.getCname(), score.getGrade());
        }

        JFreeChart chart = ChartFactory.createPieChart(
            "学生各科成绩分布",
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