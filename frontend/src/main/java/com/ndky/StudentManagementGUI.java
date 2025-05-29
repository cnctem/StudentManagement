package com.ndky;

import javax.swing.*;
import java.awt.*;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import org.json.JSONArray;
import org.json.JSONObject;

public class StudentManagementGUI extends JFrame {
    private JTable studentTable;
    private JTextField searchField;
    private JButton searchButton;
    private JButton addButton;
    private JButton editButton;
    private JButton deleteButton;
    private JButton refreshButton;

    public StudentManagementGUI() {
        setTitle("学生信息管理系统");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // 主面板
        JPanel mainPanel = new JPanel(new BorderLayout());

        // 顶部搜索面板
        JPanel searchPanel = new JPanel();
        searchField = new JTextField(20);
        searchButton = new JButton("搜索");
        searchPanel.add(new JLabel("学生姓名:"));
        searchPanel.add(searchField);
        searchPanel.add(searchButton);

        // 按钮面板
        JPanel buttonPanel = new JPanel();
        addButton = new JButton("添加");
        editButton = new JButton("编辑");
        deleteButton = new JButton("删除");
        refreshButton = new JButton("刷新");
        buttonPanel.add(addButton);
        buttonPanel.add(editButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(refreshButton);

        // 表格
        studentTable = new JTable();
        JScrollPane scrollPane = new JScrollPane(studentTable);

        // 添加组件到主面板
        mainPanel.add(searchPanel, BorderLayout.NORTH);
        mainPanel.add(scrollPane, BorderLayout.CENTER);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        add(mainPanel);

        // 按钮事件
        searchButton.addActionListener(e -> searchStudents());
        addButton.addActionListener(e -> showAddDialog());
        editButton.addActionListener(e -> showEditDialog());
        deleteButton.addActionListener(e -> deleteStudent());
        refreshButton.addActionListener(e -> refreshTable());

        // 初始化表格数据
        refreshTable();
    }

    private void refreshTable() {
        try {
            String response = sendGetRequest("http://localhost:8080/api/student/list");
            JSONArray jsonArray = new JSONArray(response);

            String[] columnNames = {"学号", "姓名", "性别", "年龄", "院系"};
            Object[][] data = new Object[jsonArray.length()][5];

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject student = jsonArray.getJSONObject(i);
                data[i][0] = student.getInt("sno");
                data[i][1] = student.getString("sname");
                data[i][2] = student.getString("ssex");
                data[i][3] = student.getInt("sage");
                data[i][4] = student.getString("sdept");
            }

            studentTable.setModel(new javax.swing.table.DefaultTableModel(data, columnNames));
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "加载数据失败: " + e.getMessage(), "错误", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    private void searchStudents() {
        String name = searchField.getText().trim();
        if (name.isEmpty()) {
            refreshTable();
            return;
        }

        try {
            String encodedName = URLEncoder.encode(name, "UTF-8");
            String response = sendGetRequest("http://localhost:8080/api/student/search?name=" + encodedName);
            JSONArray jsonArray = new JSONArray(response);

            String[] columnNames = {"学号", "姓名", "性别", "年龄", "院系"};
            Object[][] data = new Object[jsonArray.length()][5];

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject student = jsonArray.getJSONObject(i);
                data[i][0] = student.getInt("sno");
                data[i][1] = student.getString("sname");
                data[i][2] = student.getString("ssex");
                data[i][3] = student.getInt("sage");
                data[i][4] = student.getString("sdept");
            }

            studentTable.setModel(new javax.swing.table.DefaultTableModel(data, columnNames));
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "搜索失败: " + e.getMessage(), "错误", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    private void showAddDialog() {
        JDialog dialog = new JDialog(this, "添加学生", true);
        dialog.setSize(400, 300);
        dialog.setLayout(new GridLayout(6, 2));

        JTextField snoField = new JTextField();
        JTextField nameField = new JTextField();
        JComboBox<String> sexComboBox = new JComboBox<>(new String[]{"男", "女"});
        JTextField ageField = new JTextField();
        JTextField deptField = new JTextField();

        dialog.add(new JLabel("学号:"));
        dialog.add(snoField);
        dialog.add(new JLabel("姓名:"));
        dialog.add(nameField);
        dialog.add(new JLabel("性别:"));
        dialog.add(sexComboBox);
        dialog.add(new JLabel("年龄:"));
        dialog.add(ageField);
        dialog.add(new JLabel("院系:"));
        dialog.add(deptField);

        JButton saveButton = new JButton("保存");
        JButton cancelButton = new JButton("取消");

        saveButton.addActionListener(e -> {
            try {
                JSONObject student = new JSONObject();
                student.put("sno", Integer.parseInt(snoField.getText()));
                student.put("sname", nameField.getText());
                student.put("ssex", sexComboBox.getSelectedItem());
                student.put("sage", Integer.parseInt(ageField.getText()));
                student.put("sdept", deptField.getText());

                String response = sendPostRequest("http://localhost:8080/api/student/add", student.toString());
                if (response.contains("true")) {
                    JOptionPane.showMessageDialog(dialog, "添加成功", "成功", JOptionPane.INFORMATION_MESSAGE);
                    dialog.dispose();
                    refreshTable();
                } else {
                    JOptionPane.showMessageDialog(dialog, "添加失败", "错误", JOptionPane.ERROR_MESSAGE);
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(dialog, "添加失败: " + ex.getMessage(), "错误", JOptionPane.ERROR_MESSAGE);
                ex.printStackTrace();
            }
        });

        cancelButton.addActionListener(e -> dialog.dispose());

        dialog.add(saveButton);
        dialog.add(cancelButton);

        dialog.setVisible(true);
    }

    private void showEditDialog() {
        int selectedRow = studentTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "请先选择要编辑的学生", "提示", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int sno = (int) studentTable.getValueAt(selectedRow, 0);

        try {
            String response = sendGetRequest("http://localhost:8080/api/student/get/" + sno);
            JSONObject student = new JSONObject(response);

            JDialog dialog = new JDialog(this, "编辑学生", true);
            dialog.setSize(400, 300);
            dialog.setLayout(new GridLayout(6, 2));

            JTextField snoField = new JTextField(String.valueOf(student.getInt("sno")));
            snoField.setEditable(false);
            JTextField nameField = new JTextField(student.getString("sname"));
            JComboBox<String> sexComboBox = new JComboBox<>(new String[]{"男", "女"});
            sexComboBox.setSelectedItem(student.getString("ssex"));
            JTextField ageField = new JTextField(String.valueOf(student.getInt("sage")));
            JTextField deptField = new JTextField(student.getString("sdept"));

            dialog.add(new JLabel("学号:"));
            dialog.add(snoField);
            dialog.add(new JLabel("姓名:"));
            dialog.add(nameField);
            dialog.add(new JLabel("性别:"));
            dialog.add(sexComboBox);
            dialog.add(new JLabel("年龄:"));
            dialog.add(ageField);
            dialog.add(new JLabel("院系:"));
            dialog.add(deptField);

            JButton saveButton = new JButton("保存");
            JButton cancelButton = new JButton("取消");

            saveButton.addActionListener(e -> {
                try {
                    JSONObject updatedStudent = new JSONObject();
                    updatedStudent.put("sno", Integer.parseInt(snoField.getText()));
                    updatedStudent.put("sname", nameField.getText());
                    updatedStudent.put("ssex", sexComboBox.getSelectedItem());
                    updatedStudent.put("sage", Integer.parseInt(ageField.getText()));
                    updatedStudent.put("sdept", deptField.getText());

                    String updateResponse = sendPutRequest("http://localhost:8080/api/student/update", updatedStudent.toString());
                    if (updateResponse.contains("true")) {
                        JOptionPane.showMessageDialog(dialog, "更新成功", "成功", JOptionPane.INFORMATION_MESSAGE);
                        dialog.dispose();
                        refreshTable();
                    } else {
                        JOptionPane.showMessageDialog(dialog, "更新失败", "错误", JOptionPane.ERROR_MESSAGE);
                    }
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(dialog, "更新失败: " + ex.getMessage(), "错误", JOptionPane.ERROR_MESSAGE);
                    ex.printStackTrace();
                }
            });

            cancelButton.addActionListener(e -> dialog.dispose());

            dialog.add(saveButton);
            dialog.add(cancelButton);

            dialog.setVisible(true);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "获取学生信息失败: " + e.getMessage(), "错误", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    private void deleteStudent() {
        int selectedRow = studentTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "请先选择要删除的学生", "提示", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int sno = (int) studentTable.getValueAt(selectedRow, 0);
        int confirm = JOptionPane.showConfirmDialog(this, "确定要删除学号为 " + sno + " 的学生吗?", "确认删除", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            try {
                String response = sendDeleteRequest("http://localhost:8080/api/student/delete/" + sno);
                if (response.contains("true")) {
                    JOptionPane.showMessageDialog(this, "删除成功", "成功", JOptionPane.INFORMATION_MESSAGE);
                    refreshTable();
                } else {
                    JOptionPane.showMessageDialog(this, "删除失败", "错误", JOptionPane.ERROR_MESSAGE);
                }
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "删除失败: " + e.getMessage(), "错误", JOptionPane.ERROR_MESSAGE);
                e.printStackTrace();
            }
        }
    }

    // HTTP请求方法
    private String sendGetRequest(String urlStr) throws Exception {
        URL url = new URL(urlStr);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.setRequestProperty("Accept", "application/json");

        BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream(), StandardCharsets.UTF_8));
        String inputLine;
        StringBuilder response = new StringBuilder();

        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();

        return response.toString();
    }

    private String sendPostRequest(String urlStr, String jsonInput) throws Exception {
        URL url = new URL(urlStr);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Content-Type", "application/json");
        conn.setRequestProperty("Accept", "application/json");
        conn.setDoOutput(true);

        try(OutputStream os = conn.getOutputStream()) {
            byte[] input = jsonInput.getBytes(StandardCharsets.UTF_8);
            os.write(input, 0, input.length);
        }

        BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream(), StandardCharsets.UTF_8));
        String inputLine;
        StringBuilder response = new StringBuilder();

        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();

        return response.toString();
    }

    private String sendPutRequest(String urlStr, String jsonInput) throws Exception {
        URL url = new URL(urlStr);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("PUT");
        conn.setRequestProperty("Content-Type", "application/json");
        conn.setRequestProperty("Accept", "application/json");
        conn.setDoOutput(true);

        try(OutputStream os = conn.getOutputStream()) {
            byte[] input = jsonInput.getBytes(StandardCharsets.UTF_8);
            os.write(input, 0, input.length);
        }

        BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream(), StandardCharsets.UTF_8));
        String inputLine;
        StringBuilder response = new StringBuilder();

        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();

        return response.toString();
    }

    private String sendDeleteRequest(String urlStr) throws Exception {
        URL url = new URL(urlStr);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("DELETE");
        conn.setRequestProperty("Accept", "application/json");

        BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream(), StandardCharsets.UTF_8));
        String inputLine;
        StringBuilder response = new StringBuilder();

        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();

        return response.toString();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            StudentManagementGUI gui = new StudentManagementGUI();
            gui.setVisible(true);
        });
    }
}