package com.ndky.gui;

import javax.swing.*;
import java.awt.*;

public class StudentManagementGUI extends JFrame {
    private StudentPanel studentPanel;
    private CoursePanel coursePanel;
    private StudentStatisticsPanel studentStatisticsPanel;
    private CourseStatisticsPanel courseStatisticsPanel;
    private ScoreManagementPanel scoreManagementPanel;
    private JTabbedPane tabbedPane;

    public StudentManagementGUI() {
        setTitle("学生成绩管理系统");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);
        setLocationRelativeTo(null);
        
        initializeComponents();
        setupLayout();
    }

    private void initializeComponents() {
        studentPanel = new StudentPanel();
        coursePanel = new CoursePanel();
        studentStatisticsPanel = new StudentStatisticsPanel();
        courseStatisticsPanel = new CourseStatisticsPanel();
        scoreManagementPanel = new ScoreManagementPanel();
        
        tabbedPane = new JTabbedPane();
        tabbedPane.addTab("学生管理", studentPanel);
        tabbedPane.addTab("课程管理", coursePanel);
        tabbedPane.addTab("学生统计", studentStatisticsPanel);
        tabbedPane.addTab("课程统计", courseStatisticsPanel);
        tabbedPane.addTab("成绩管理", scoreManagementPanel);
    }

    private void setupLayout() {
        setLayout(new BorderLayout());
        add(tabbedPane, BorderLayout.CENTER);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            StudentManagementGUI gui = new StudentManagementGUI();
            gui.setVisible(true);
        });
    }
} 