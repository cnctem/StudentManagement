package com.ndky.gui;

import com.ndky.model.Score;
import com.ndky.service.ScoreService;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.CategoryLabelPositions;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.data.category.DefaultCategoryDataset;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class StudentStatisticsPanel extends JPanel {
    private JTextField snoField;
    private JButton queryButton;
    private JButton calculateButton;
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
        snoField = new JTextField(10);
        queryButton = new JButton("查询成绩");
        calculateButton = new JButton("计算平均分");
        resultArea = new JTextArea();
        resultArea.setEditable(false);
        chartPanelContainer = new JPanel(new BorderLayout());
        chartPanelContainer.setPreferredSize(new Dimension(500, 400));
    }

    private void setupLayout() {
        // 控制面板
        JPanel controlPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        controlPanel.add(new JLabel("学号:"));
        controlPanel.add(snoField);
        controlPanel.add(queryButton);
        controlPanel.add(calculateButton);

        // 结果面板
        JScrollPane textScrollPane = new JScrollPane(resultArea);
        textScrollPane.setPreferredSize(new Dimension(300, 400));

        // 使用GridLayout将图表和文本区域并排显示，图表在左
        JPanel resultPanel = new JPanel(new GridLayout(1, 2, 10, 0));
        resultPanel.add(chartPanelContainer);  // 先添加图表
        resultPanel.add(textScrollPane);       // 再添加文本区域

        add(controlPanel, BorderLayout.NORTH);
        add(resultPanel, BorderLayout.CENTER);
    }

    private void setupListeners() {
        queryButton.addActionListener(e -> queryStudentScores());
        calculateButton.addActionListener(e -> calculateStudentAvg());
    }

    private void queryStudentScores() {
        String snoStr = snoField.getText().trim();
        if (snoStr.isEmpty()) {
            JOptionPane.showMessageDialog(this, "请输入学号", "提示", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            int sno = Integer.parseInt(snoStr);
            List<Score> scores = scoreService.getStudentScores(sno);
            if (scores.isEmpty()) {
                resultArea.setText("未找到该学生的成绩记录");
                chartPanelContainer.removeAll();
                chartPanelContainer.revalidate();
                chartPanelContainer.repaint();
                return;
            }

            // 显示成绩列表
            StringBuilder sb = new StringBuilder();
            sb.append("学号: ").append(sno).append("\n");
            sb.append("学生姓名: ").append(scores.get(0).getSname()).append("\n");
            sb.append("----------------------------------\n");
            sb.append("课程成绩列表:\n");
            for (Score score : scores) {
                sb.append(String.format("课程: %s\t成绩: %d\n", score.getCname(), score.getGrade()));
            }
            resultArea.setText(sb.toString());

            // 显示成绩分布柱状图
            showStudentScoreChart(scores);
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "学号必须是数字", "错误", JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "查询失败: " + ex.getMessage(), "错误", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void calculateStudentAvg() {
        String snoStr = snoField.getText().trim();
        if (snoStr.isEmpty()) {
            JOptionPane.showMessageDialog(this, "请输入学号", "提示", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            int sno = Integer.parseInt(snoStr);
            List<Score> scores = scoreService.getStudentScores(sno);
            if (scores.isEmpty()) {
                resultArea.setText("未找到该学生的成绩记录");
                return;
            }

            double sum = 0;
            for (Score score : scores) {
                sum += score.getGrade();
            }
            double avg = sum / scores.size();

            StringBuilder sb = new StringBuilder();
            sb.append("学号: ").append(sno).append("\n");
            sb.append("学生姓名: ").append(scores.get(0).getSname()).append("\n");
            sb.append("----------------------------------\n");
            sb.append(String.format("平均分: %.2f\n", avg));
            sb.append(String.format("总课程数: %d\n", scores.size()));
            resultArea.setText(sb.toString());
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "学号必须是数字", "错误", JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "计算失败: " + ex.getMessage(), "错误", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void showStudentScoreChart(List<Score> scores) {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        
        // 添加每个课程的成绩数据
        for (Score score : scores) {
            dataset.addValue(score.getGrade(), "成绩", score.getCname());
        }

        // 创建柱状图
        JFreeChart chart = ChartFactory.createBarChart(
            "学生成绩分布",           // 图表标题
            "课程",                  // 横轴标签
            "成绩",                  // 纵轴标签
            dataset,                // 数据集
            PlotOrientation.VERTICAL, // 图表方向
            true,                   // 是否显示图例
            true,                   // 是否显示工具提示
            false                   // 是否生成URL
        );

        // 设置图表样式
        CategoryPlot plot = chart.getCategoryPlot();
        plot.setBackgroundPaint(Color.WHITE);
        plot.setRangeGridlinePaint(Color.LIGHT_GRAY);
        
        // 设置柱状图样式
        BarRenderer renderer = (BarRenderer) plot.getRenderer();
        renderer.setSeriesPaint(0, new Color(79, 129, 189)); // 设置柱状图颜色
        
        // 设置坐标轴
        CategoryAxis domainAxis = plot.getDomainAxis();
        domainAxis.setCategoryLabelPositions(CategoryLabelPositions.UP_45); // 标签旋转45度
        
        NumberAxis rangeAxis = (NumberAxis) plot.getRangeAxis();
        rangeAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());
        rangeAxis.setRange(0, 100); // 设置成绩范围

        // 创建图表面板
        ChartPanel chartPanel = new ChartPanel(chart);
        chartPanel.setPreferredSize(new Dimension(500, 400));
        chartPanel.setMouseWheelEnabled(true);

        // 更新图表显示
        chartPanelContainer.removeAll();
        chartPanelContainer.add(chartPanel, BorderLayout.CENTER);
        chartPanelContainer.revalidate();
        chartPanelContainer.repaint();
    }
} 