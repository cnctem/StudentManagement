package com.ndky.service;

import com.ndky.model.Student;
import com.ndky.util.HttpUtil;
import org.json.JSONArray;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;

public class StudentService {
    public List<Student> getAllStudents() throws Exception {
        String response = HttpUtil.sendGetRequest("/student/list");
        return parseStudentList(response);
    }

    public List<Student> searchStudents(String sno, String name, String dept) throws Exception {
        StringBuilder url = new StringBuilder("/student/search?");
        boolean hasParam = false;

        if (sno != null && !sno.isEmpty()) {
            url.append("sno=").append(sno);
            hasParam = true;
        }
        if (name != null && !name.isEmpty()) {
            if (hasParam) url.append("&");
            url.append("name=").append(name);
            hasParam = true;
        }
        if (dept != null && !dept.isEmpty()) {
            if (hasParam) url.append("&");
            url.append("dept=").append(dept);
        }

        String response = HttpUtil.sendGetRequest(url.toString());
        return parseStudentList(response);
    }

    public Student getStudent(int sno) throws Exception {
        String response = HttpUtil.sendGetRequest("/student/get/" + sno);
        return parseStudent(response);
    }

    public void addStudent(Student student) throws Exception {
        JSONObject json = new JSONObject();
        json.put("sno", student.getSno());
        json.put("sname", student.getSname());
        json.put("ssex", student.getSsex());
        json.put("sage", student.getSage());
        json.put("sdept", student.getSdept());

        HttpUtil.sendPostRequest("/student/add", json.toString());
    }

    public void updateStudent(Student student) throws Exception {
        JSONObject json = new JSONObject();
        json.put("sno", student.getSno());
        json.put("sname", student.getSname());
        json.put("ssex", student.getSsex());
        json.put("sage", student.getSage());
        json.put("sdept", student.getSdept());

        HttpUtil.sendPutRequest("/student/update", json.toString());
    }

    public void deleteStudent(int sno) throws Exception {
        HttpUtil.sendDeleteRequest("/student/delete/" + sno);
    }

    private List<Student> parseStudentList(String jsonStr) {
        List<Student> students = new ArrayList<>();
        JSONArray jsonArray = new JSONArray(jsonStr);
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject json = jsonArray.getJSONObject(i);
            Student student = new Student();
            student.setSno(json.getInt("sno"));
            student.setSname(json.getString("sname"));
            student.setSsex(json.getString("ssex"));
            student.setSage(json.getInt("sage"));
            student.setSdept(json.getString("sdept"));
            students.add(student);
        }
        return students;
    }

    private Student parseStudent(String jsonStr) {
        JSONObject json = new JSONObject(jsonStr);
        Student student = new Student();
        student.setSno(json.getInt("sno"));
        student.setSname(json.getString("sname"));
        student.setSsex(json.getString("ssex"));
        student.setSage(json.getInt("sage"));
        student.setSdept(json.getString("sdept"));
        return student;
    }
} 