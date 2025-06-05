package com.ndky.gui;

import com.ndky.model.Score;
import com.ndky.service.ScoreService;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class ScorePanel extends JPanel {
    private JTable scoreTable;
    private JTextField studentSnoSearchField;
    private JTextField courseCnoSearchField;
    private JButton scoreSearchButton;
    private ScoreService scoreService;

    public ScorePanel() {
        scoreService = new ScoreService();
        setLayout(new BorderLayout());
        initializeComponents();
        setupLayout();
        setupListeners();
        refreshTable();
    }

    private void initializeComponents() {
        studentSnoSearchField = new JTextField(10);
        courseCnoSearchField = new JTextField(10);
        scoreSearchButton = new JButton("搜索成绩");
        scoreTable = new JTable();
    }

    private void setupLayout() {
        JPanel searchPanel = new JPanel();
        searchPanel.add(new JLabel("学号:"));
        searchPanel.add(studentSnoSearchField);
        searchPanel.add(new JLabel("课程号:"));
        searchPanel.add(courseCnoSearchField);
        searchPanel.add(scoreSearchButton);

        JScrollPane scrollPane = new JScrollPane(scoreTable);

        add(searchPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
    }

    private void setupListeners() {
        scoreSearchButton.addActionListener(e -> searchScores());
    }

    public void refreshTable() {
        try {
            List<Score> scores = scoreService.getAllScores();
            updateTableModel(scores);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "加载成绩数据失败: " + e.getMessage(), "错误", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    private void searchScores() {
        String sno = studentSnoSearchField.getText().trim();
        String cno = courseCnoSearchField.getText().trim();

        try {
            List<Score> scores;
            if (!sno.isEmpty() && !cno.isEmpty()) {
                // 如果同时输入了学号和课程号，先获取学生成绩，再过滤课程
                scores = scoreService.getStudentScores(Integer.parseInt(sno));
                scores.removeIf(score -> score.getCno() != Integer.parseInt(cno));
            } else if (!sno.isEmpty()) {
                scores = scoreService.getStudentScores(Integer.parseInt(sno));
            } else if (!cno.isEmpty()) {
                scores = scoreService.getCourseScores(Integer.parseInt(cno));
            } else {
                scores = scoreService.getAllScores();
            }
            updateTableModel(scores);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "搜索成绩失败: " + e.getMessage(), "错误", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    private void updateTableModel(List<Score> scores) {
        String[] columnNames = {"学号", "姓名", "课程号", "课程名称", "成绩", "院系", "学分"};
        Object[][] data = new Object[scores.size()][7];

        for (int i = 0; i < scores.size(); i++) {
            Score score = scores.get(i);
            data[i][0] = score.getSno();
            data[i][1] = score.getSname();
            data[i][2] = score.getCno();
            data[i][3] = score.getCname();
            data[i][4] = score.getGrade();
            data[i][5] = score.getSdept();
            data[i][6] = score.getCcredit();
        }

        scoreTable.setModel(new DefaultTableModel(data, columnNames));
    }
} 