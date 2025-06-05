package com.ndky.gui;

import com.ndky.model.Student;
import com.ndky.service.StudentService;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class StudentPanel extends JPanel {
    private JTextField snoField;
    private JTextField nameField;
    private JTextField deptField;
    private JButton searchButton;
    private JButton addButton;
    private JButton editButton;
    private JButton deleteButton;
    private JTable studentTable;
    private DefaultTableModel tableModel;
    private StudentService studentService;

    public StudentPanel() {
        studentService = new StudentService();
        setLayout(new BorderLayout());
        initializeComponents();
        setupLayout();
        setupListeners();
        refreshTable();
    }

    private void initializeComponents() {
        snoField = new JTextField(10);
        nameField = new JTextField(10);
        deptField = new JTextField(10);
        searchButton = new JButton("查询");
        addButton = new JButton("添加");
        editButton = new JButton("编辑");
        deleteButton = new JButton("删除");

        String[] columnNames = {"学号", "姓名", "性别", "年龄", "系别"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        studentTable = new JTable(tableModel);
        studentTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    }

    private void setupLayout() {
        // 搜索面板
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        searchPanel.add(new JLabel("学号:"));
        searchPanel.add(snoField);
        searchPanel.add(new JLabel("姓名:"));
        searchPanel.add(nameField);
        searchPanel.add(new JLabel("系别:"));
        searchPanel.add(deptField);
        searchPanel.add(searchButton);

        // 按钮面板
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.add(addButton);
        buttonPanel.add(editButton);
        buttonPanel.add(deleteButton);

        // 控制面板
        JPanel controlPanel = new JPanel(new BorderLayout());
        controlPanel.add(searchPanel, BorderLayout.WEST);
        controlPanel.add(buttonPanel, BorderLayout.EAST);

        // 表格面板
        JScrollPane scrollPane = new JScrollPane(studentTable);

        add(controlPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
    }

    private void setupListeners() {
        searchButton.addActionListener(e -> searchStudents());
        addButton.addActionListener(e -> showAddDialog());
        editButton.addActionListener(e -> showEditDialog());
        deleteButton.addActionListener(e -> deleteStudent());
    }

    private void searchStudents() {
        String sno = snoField.getText().trim();
        String name = nameField.getText().trim();
        String dept = deptField.getText().trim();

        try {
            List<Student> students;
            if (!sno.isEmpty() || !name.isEmpty() || !dept.isEmpty()) {
                // 如果有任何搜索条件，使用条件查询
                students = studentService.searchStudents(sno, name, dept);
            } else {
                // 如果没有搜索条件，获取所有学生
                students = studentService.getAllStudents();
            }
            updateTable(students);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "查询学生信息失败: " + ex.getMessage(), "错误", JOptionPane.ERROR_MESSAGE);
        }
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

            JTextField nameField = new JTextField(student.getSname());
            JComboBox<String> sexComboBox = new JComboBox<>(new String[]{"男", "女"});
            sexComboBox.setSelectedItem(student.getSsex());
            JTextField ageField = new JTextField(String.valueOf(student.getSage()));
            JTextField deptField = new JTextField(student.getSdept());

            JPanel panel = new JPanel(new GridLayout(4, 2, 5, 5));
            panel.add(new JLabel("姓名:"));
            panel.add(nameField);
            panel.add(new JLabel("性别:"));
            panel.add(sexComboBox);
            panel.add(new JLabel("年龄:"));
            panel.add(ageField);
            panel.add(new JLabel("系别:"));
            panel.add(deptField);

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
            List<Student> students = studentService.getAllStudents();
            updateTable(students);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "刷新学生列表失败: " + ex.getMessage(), "错误", JOptionPane.ERROR_MESSAGE);
        }
    }
} 