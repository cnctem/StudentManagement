package com.ndky.service;

import com.ndky.model.Student;
import com.ndky.util.HttpUtil;
import org.json.JSONArray;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;

public class StudentService {
    public List<Student> getAllStudents() throws Exception {
        String response = HttpUtil.sendGet("/student/list");
        return parseStudentList(response);
    }

    public List<Student> getStudentsByPage(int pageNum, int pageSize) throws Exception {
        String response = HttpUtil.sendGet(String.format("/student/page?pageNum=%d&pageSize=%d", pageNum, pageSize));
        return parseStudentList(response);
    }

    public List<Student> searchStudents(String sno, String name, String dept, String sex, 
            int pageNum, int pageSize) throws Exception {
        StringBuilder url = new StringBuilder("/student/search?");
        boolean hasParam = false;

        if (sno != null && !sno.isEmpty()) {
            url.append("sno=").append(java.net.URLEncoder.encode(sno, "UTF-8"));
            hasParam = true;
        }
        if (name != null && !name.isEmpty()) {
            if (hasParam) url.append("&");
            url.append("name=").append(java.net.URLEncoder.encode(name, "UTF-8"));
            hasParam = true;
        }
        if (dept != null && !dept.isEmpty()) {
            if (hasParam) url.append("&");
            url.append("dept=").append(java.net.URLEncoder.encode(dept, "UTF-8"));
            hasParam = true;
        }
        if (sex != null && !sex.isEmpty()) {
            if (hasParam) url.append("&");
            url.append("sex=").append(java.net.URLEncoder.encode(sex, "UTF-8"));
            hasParam = true;
        }
        if (hasParam) url.append("&");
        url.append("pageNum=").append(pageNum).append("&pageSize=").append(pageSize);

        String response = HttpUtil.sendGet(url.toString());
        return parseStudentList(response);
    }

    public int getTotalPages(String sno, String name, String dept, String sex, 
            int pageSize) throws Exception {
        StringBuilder url = new StringBuilder("/student/count?");
        boolean hasParam = false;

        if (sno != null && !sno.isEmpty()) {
            url.append("sno=").append(java.net.URLEncoder.encode(sno, "UTF-8"));
            hasParam = true;
        }
        if (name != null && !name.isEmpty()) {
            if (hasParam) url.append("&");
            url.append("name=").append(java.net.URLEncoder.encode(name, "UTF-8"));
            hasParam = true;
        }
        if (dept != null && !dept.isEmpty()) {
            if (hasParam) url.append("&");
            url.append("dept=").append(java.net.URLEncoder.encode(dept, "UTF-8"));
            hasParam = true;
        }
        if (sex != null && !sex.isEmpty()) {
            if (hasParam) url.append("&");
            url.append("sex=").append(java.net.URLEncoder.encode(sex, "UTF-8"));
        }

        String response = HttpUtil.sendGet(url.toString());
        JSONObject json = new JSONObject(response);
        int total = json.getInt("total");
        return (total + pageSize - 1) / pageSize;
    }

    public Student getStudent(int sno) throws Exception {
        String response = HttpUtil.sendGet("/student/get/" + sno);
        return parseStudent(response);
    }

    public void addStudent(Student student) throws Exception {
        JSONObject json = new JSONObject();
        json.put("sno", student.getSno());
        json.put("sname", student.getSname());
        json.put("ssex", student.getSsex());
        json.put("sage", student.getSage());
        json.put("sdept", student.getSdept());

        HttpUtil.sendPost("/student/add", json.toString());
    }

    public void updateStudent(Student student) throws Exception {
        JSONObject json = new JSONObject();
        json.put("sno", student.getSno());
        json.put("sname", student.getSname());
        json.put("ssex", student.getSsex());
        json.put("sage", student.getSage());
        json.put("sdept", student.getSdept());

        HttpUtil.sendPut("/student/update", json.toString());
    }

    public void deleteStudent(int sno) throws Exception {
        HttpUtil.sendDelete("/student/delete/" + sno);
    }

    private List<Student> parseStudentList(String jsonStr) {
        List<Student> students = new ArrayList<>();
        try {
            JSONObject json = new JSONObject(jsonStr);
            JSONArray array = json.getJSONArray("students");
            for (int i = 0; i < array.length(); i++) {
                JSONObject obj = array.getJSONObject(i);
                Student student = new Student();
                student.setSno(obj.getInt("sno"));
                student.setSname(obj.getString("sname"));
                student.setSsex(obj.getString("ssex"));
                student.setSage(obj.getInt("sage"));
                student.setSdept(obj.getString("sdept"));
                students.add(student);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return students;
    }

    private Student parseStudent(String response) {
        JSONObject json = new JSONObject(response);
        Student student = new Student();
        student.setSno(json.getInt("sno"));
        student.setSname(json.getString("sname"));
        student.setSsex(json.getString("ssex"));
        student.setSage(json.getInt("sage"));
        student.setSdept(json.getString("sdept"));
        return student;
    }
} 