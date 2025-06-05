package com.ndky.gui;

import com.ndky.model.Course;
import com.ndky.service.CourseService;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class CoursePanel extends JPanel {
    private JTable courseTable;
    private CourseService courseService;

    public CoursePanel() {
        courseService = new CourseService();
        setLayout(new BorderLayout());
        initializeComponents();
        setupLayout();
        refreshTable();
    }

    private void initializeComponents() {
        courseTable = new JTable();
    }

    private void setupLayout() {
        JScrollPane scrollPane = new JScrollPane(courseTable);
        add(scrollPane, BorderLayout.CENTER);
    }

    public void refreshTable() {
        try {
            List<Course> courses = courseService.getAllCourses();
            String[] columnNames = {"课程号", "课程名称", "前修课程", "学分"};
            Object[][] data = new Object[courses.size()][4];

            for (int i = 0; i < courses.size(); i++) {
                Course course = courses.get(i);
                data[i][0] = course.getCno();
                data[i][1] = course.getCname();
                data[i][2] = course.getCpno();
                data[i][3] = course.getCcredit();
            }

            courseTable.setModel(new DefaultTableModel(data, columnNames));
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "加载课程数据失败: " + e.getMessage(), "错误", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }
} 