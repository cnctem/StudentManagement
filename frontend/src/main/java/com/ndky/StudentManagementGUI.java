package com.ndky;

import com.ndky.gui.*;

import javax.swing.*;
import java.awt.*;

public class StudentManagementGUI extends JFrame {
    private JTabbedPane tabbedPane;
    private StudentPanel studentPanel;
    private CoursePanel coursePanel;
    private ScorePanel scorePanel;
    private StudentStatisticsPanel studentStatisticsPanel;
    private CourseStatisticsPanel courseStatisticsPanel;

    public StudentManagementGUI() {
        setTitle("学生成绩管理系统");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1200, 800);
        setLocationRelativeTo(null);
        initializeComponents();
        setupLayout();
    }

    private void initializeComponents() {
        tabbedPane = new JTabbedPane();
        studentPanel = new StudentPanel();
        coursePanel = new CoursePanel();
        scorePanel = new ScorePanel();
        studentStatisticsPanel = new StudentStatisticsPanel();
        courseStatisticsPanel = new CourseStatisticsPanel();
    }

    private void setupLayout() {
        tabbedPane.addTab("学生管理", studentPanel);
        tabbedPane.addTab("课程管理", coursePanel);
        tabbedPane.addTab("成绩管理", scorePanel);
        tabbedPane.addTab("学生统计", studentStatisticsPanel);
        tabbedPane.addTab("课程统计", courseStatisticsPanel);

        add(tabbedPane);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception e) {
                e.printStackTrace();
            }
            new StudentManagementGUI().setVisible(true);
        });
    }
}