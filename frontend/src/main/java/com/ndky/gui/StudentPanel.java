package com.ndky.gui;

import com.ndky.model.Student;
import com.ndky.service.StudentService;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.util.List;

public class StudentPanel extends JPanel {
    private JTextField snoField;
    private JTextField nameField;
    private JTextField deptField;
    private JComboBox<String> sexComboBox;
    private JButton searchButton;
    private JButton resetButton;
    private JButton addButton;
    private JButton editButton;
    private JButton deleteButton;
    private JTable studentTable;
    private DefaultTableModel tableModel;
    private StudentService studentService;
    private JPanel paginationPanel;
    private JButton prevPageButton;
    private JButton nextPageButton;
    private JLabel pageInfoLabel;
    private int currentPage = 1;
    private int pageSize = 10;
    private int totalPages = 1;

    public StudentPanel() {
        studentService = new StudentService();
        setLayout(new BorderLayout());
        initializeComponents();
        setupLayout();
        setupListeners();
        // 初始化时重置搜索条件并显示所有学生数据
        resetSearch();
    }

    private void initializeComponents() {
        snoField = new JTextField(10);
        nameField = new JTextField(10);
        deptField = new JTextField(10);
        sexComboBox = new JComboBox<>(new String[]{"全部", "男", "女"});
        searchButton = new JButton("查询");
        resetButton = new JButton("重置");
        addButton = new JButton("添加");
        editButton = new JButton("编辑");
        deleteButton = new JButton("删除");
        prevPageButton = new JButton("上一页");
        nextPageButton = new JButton("下一页");
        pageInfoLabel = new JLabel("第 1 页 / 共 1 页");

        // 添加输入提示
        snoField.putClientProperty("JTextField.placeholderText", "输入学号（支持模糊查询）");
        nameField.putClientProperty("JTextField.placeholderText", "输入姓名（支持模糊查询）");
        deptField.putClientProperty("JTextField.placeholderText", "输入系别（支持模糊查询）");

        String[] columnNames = {"学号", "姓名", "性别", "年龄", "系别"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        studentTable = new JTable(tableModel);
        studentTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        
        // 设置表格列宽
        studentTable.getColumnModel().getColumn(0).setPreferredWidth(100); // 学号
        studentTable.getColumnModel().getColumn(1).setPreferredWidth(100); // 姓名
        studentTable.getColumnModel().getColumn(2).setPreferredWidth(50);  // 性别
        studentTable.getColumnModel().getColumn(3).setPreferredWidth(50);  // 年龄
        studentTable.getColumnModel().getColumn(4).setPreferredWidth(150); // 系别
    }

    private void setupLayout() {
        // 搜索面板
        JPanel searchPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // 第一行
        gbc.gridx = 0; gbc.gridy = 0;
        searchPanel.add(new JLabel("学号:"), gbc);
        gbc.gridx = 1;
        searchPanel.add(snoField, gbc);
        gbc.gridx = 2;
        searchPanel.add(new JLabel("姓名:"), gbc);
        gbc.gridx = 3;
        searchPanel.add(nameField, gbc);
        gbc.gridx = 4;
        searchPanel.add(new JLabel("系别:"), gbc);
        gbc.gridx = 5;
        searchPanel.add(deptField, gbc);

        // 第二行
        gbc.gridx = 0; gbc.gridy = 1;
        searchPanel.add(new JLabel("性别:"), gbc);
        gbc.gridx = 1;
        searchPanel.add(sexComboBox, gbc);

        // 按钮面板
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.add(searchButton);
        buttonPanel.add(resetButton);
        buttonPanel.add(addButton);
        buttonPanel.add(editButton);
        buttonPanel.add(deleteButton);

        // 分页面板
        paginationPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        paginationPanel.add(prevPageButton);
        paginationPanel.add(pageInfoLabel);
        paginationPanel.add(nextPageButton);

        // 控制面板
        JPanel controlPanel = new JPanel(new BorderLayout());
        controlPanel.add(searchPanel, BorderLayout.CENTER);
        controlPanel.add(buttonPanel, BorderLayout.SOUTH);

        // 表格面板
        JScrollPane scrollPane = new JScrollPane(studentTable);

        add(controlPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
        add(paginationPanel, BorderLayout.SOUTH);
    }

    private void setupListeners() {
        // 添加实时搜索功能
        DocumentListener searchListener = new DocumentListener() {
            public void changedUpdate(DocumentEvent e) { search(); }
            public void removeUpdate(DocumentEvent e) { search(); }
            public void insertUpdate(DocumentEvent e) { search(); }
        };

        snoField.getDocument().addDocumentListener(searchListener);
        nameField.getDocument().addDocumentListener(searchListener);
        deptField.getDocument().addDocumentListener(searchListener);
        sexComboBox.addActionListener(e -> search());

        searchButton.addActionListener(e -> search());
        resetButton.addActionListener(e -> resetSearch());
        addButton.addActionListener(e -> showAddDialog());
        editButton.addActionListener(e -> showEditDialog());
        deleteButton.addActionListener(e -> deleteStudent());
        prevPageButton.addActionListener(e -> {
            if (currentPage > 1) {
                currentPage--;
                search();
            }
        });
        nextPageButton.addActionListener(e -> {
            if (currentPage < totalPages) {
                currentPage++;
                search();
            }
        });
    }

    private void resetSearch() {
        // 重置所有搜索条件
        snoField.setText("");
        nameField.setText("");
        deptField.setText("");
        sexComboBox.setSelectedIndex(0);
        currentPage = 1;
        
        // 显示所有学生数据
        try {
            List<Student> students = studentService.searchStudents("", "", "", null, currentPage, pageSize);
            totalPages = studentService.getTotalPages("", "", "", null, pageSize);
            updateTable(students);
            updatePaginationInfo();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "重置查询失败: " + ex.getMessage(), "错误", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void search() {
        String sno = snoField.getText().trim();
        String name = nameField.getText().trim();
        String dept = deptField.getText().trim();
        String sex = sexComboBox.getSelectedItem().toString();
        // 当选择"全部"时，将sex设置为null
        if (sex.equals("全部")) {
            sex = null;
        }

        try {
            List<Student> students = studentService.searchStudents(sno, name, dept, sex, currentPage, pageSize);
            totalPages = studentService.getTotalPages(sno, name, dept, sex, pageSize);
            updateTable(students);
            updatePaginationInfo();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "查询学生信息失败: " + ex.getMessage(), "错误", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void updatePaginationInfo() {
        pageInfoLabel.setText(String.format("第 %d 页 / 共 %d 页", currentPage, totalPages));
        prevPageButton.setEnabled(currentPage > 1);
        nextPageButton.setEnabled(currentPage < totalPages);
    }

    private void updateTable(List<Student> students) {
        tableModel.setRowCount(0);
        for (Student student : students) {
            Object[] row = {
                student.getSno(),
                student.getSname(),
                student.getSsex(),
                student.getSage(),
                student.getSdept()
            };
            tableModel.addRow(row);
        }
        
        // 如果没有找到记录，显示提示信息
        if (students.isEmpty()) {
            tableModel.addRow(new Object[]{"无数据", "", "", "", ""});
        }
    }

    private void showAddDialog() {
        JTextField snoField = new JTextField();
        JTextField nameField = new JTextField();
        JComboBox<String> sexComboBox = new JComboBox<>(new String[]{"男", "女"});
        JTextField ageField = new JTextField();
        JTextField deptField = new JTextField();

        JPanel panel = new JPanel(new GridLayout(5, 2, 5, 5));
        panel.add(new JLabel("学号:"));
        panel.add(snoField);
        panel.add(new JLabel("姓名:"));
        panel.add(nameField);
        panel.add(new JLabel("性别:"));
        panel.add(sexComboBox);
        panel.add(new JLabel("年龄:"));
        panel.add(ageField);
        panel.add(new JLabel("系别:"));
        panel.add(deptField);

        int result = JOptionPane.showConfirmDialog(this, panel, "添加学生", JOptionPane.OK_CANCEL_OPTION);
        if (result == JOptionPane.OK_OPTION) {
            try {
                // 验证输入
                String snoStr = snoField.getText().trim();
                String name = nameField.getText().trim();
                String ageStr = ageField.getText().trim();
                String dept = deptField.getText().trim();

                if (snoStr.isEmpty() || name.isEmpty() || ageStr.isEmpty() || dept.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "所有字段都必须填写", "错误", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                int sno = Integer.parseInt(snoStr);
                int age = Integer.parseInt(ageStr);

                if (age < 0 || age > 150) {
                    JOptionPane.showMessageDialog(this, "年龄必须在0-150之间", "错误", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                Student student = new Student();
                student.setSno(sno);
                student.setSname(name);
                student.setSsex(sexComboBox.getSelectedItem().toString());
                student.setSage(age);
                student.setSdept(dept);

                studentService.addStudent(student);
                refreshTable();
                JOptionPane.showMessageDialog(this, "添加学生成功！");
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "学号和年龄必须是数字", "错误", JOptionPane.ERROR_MESSAGE);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "添加学生失败: " + ex.getMessage(), "错误", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void showEditDialog() {
        int selectedRow = studentTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "请选择要编辑的学生", "提示", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int sno = (int) tableModel.getValueAt(selectedRow, 0);
        try {
            Student student = studentService.getStudent(sno);
            if (student == null) {
                JOptionPane.showMessageDialog(this, "未找到该学生", "错误", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // 创建编辑面板
            JPanel panel = new JPanel(new GridBagLayout());
            GridBagConstraints gbc = new GridBagConstraints();
            gbc.insets = new Insets(5, 5, 5, 5);
            gbc.fill = GridBagConstraints.HORIZONTAL;

            // 学号（只显示，不可编辑）
            gbc.gridx = 0; gbc.gridy = 0;
            panel.add(new JLabel("学号:"), gbc);
            gbc.gridx = 1;
            JTextField snoField = new JTextField(String.valueOf(student.getSno()));
            snoField.setEditable(false);
            panel.add(snoField, gbc);

            // 姓名
            gbc.gridx = 0; gbc.gridy = 1;
            panel.add(new JLabel("姓名:"), gbc);
            gbc.gridx = 1;
            JTextField nameField = new JTextField(student.getSname());
            panel.add(nameField, gbc);

            // 性别
            gbc.gridx = 0; gbc.gridy = 2;
            panel.add(new JLabel("性别:"), gbc);
            gbc.gridx = 1;
            JComboBox<String> sexComboBox = new JComboBox<>(new String[]{"男", "女"});
            sexComboBox.setSelectedItem(student.getSsex());
            panel.add(sexComboBox, gbc);

            // 年龄
            gbc.gridx = 0; gbc.gridy = 3;
            panel.add(new JLabel("年龄:"), gbc);
            gbc.gridx = 1;
            JTextField ageField = new JTextField(String.valueOf(student.getSage()));
            panel.add(ageField, gbc);

            // 系别
            gbc.gridx = 0; gbc.gridy = 4;
            panel.add(new JLabel("系别:"), gbc);
            gbc.gridx = 1;
            JTextField deptField = new JTextField(student.getSdept());
            panel.add(deptField, gbc);

            int result = JOptionPane.showConfirmDialog(this, panel, "编辑学生", JOptionPane.OK_CANCEL_OPTION);
            if (result == JOptionPane.OK_OPTION) {
                try {
                    // 验证输入
                    String name = nameField.getText().trim();
                    String ageStr = ageField.getText().trim();
                    String dept = deptField.getText().trim();

                    if (name.isEmpty() || ageStr.isEmpty() || dept.isEmpty()) {
                        JOptionPane.showMessageDialog(this, "所有字段都必须填写", "错误", JOptionPane.ERROR_MESSAGE);
                        return;
                    }

                    int age = Integer.parseInt(ageStr);
                    if (age < 0 || age > 150) {
                        JOptionPane.showMessageDialog(this, "年龄必须在0-150之间", "错误", JOptionPane.ERROR_MESSAGE);
                        return;
                    }

                    student.setSname(name);
                    student.setSsex(sexComboBox.getSelectedItem().toString());
                    student.setSage(age);
                    student.setSdept(dept);

                    studentService.updateStudent(student);
                    refreshTable();
                    JOptionPane.showMessageDialog(this, "更新学生信息成功！");
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(this, "年龄必须是数字", "错误", JOptionPane.ERROR_MESSAGE);
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this, "更新学生信息失败: " + ex.getMessage(), "错误", JOptionPane.ERROR_MESSAGE);
                }
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "获取学生信息失败: " + ex.getMessage(), "错误", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void deleteStudent() {
        int selectedRow = studentTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "请选择要删除的学生", "提示", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this, "确定要删除该学生吗？", "确认", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            try {
                int sno = (int) tableModel.getValueAt(selectedRow, 0);
                studentService.deleteStudent(sno);
                refreshTable();
                JOptionPane.showMessageDialog(this, "删除学生成功！");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "删除学生失败: " + ex.getMessage(), "错误", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    public void refreshTable() {
        try {
            currentPage = 1;
            List<Student> students = studentService.getStudentsByPage(currentPage, pageSize);
            totalPages = studentService.getTotalPages(null, null, null, null, pageSize);
            updateTable(students);
            updatePaginationInfo();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "刷新学生列表失败: " + ex.getMessage(), "错误", JOptionPane.ERROR_MESSAGE);
        }
    }
} 