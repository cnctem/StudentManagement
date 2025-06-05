package com.ndky.service;

import com.ndky.model.Course;
import com.ndky.util.HttpUtil;
import org.json.JSONArray;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;

public class CourseService {
    public List<Course> getAllCourses() throws Exception {
        String response = HttpUtil.sendGetRequest("/course/list");
        JSONArray jsonArray = new JSONArray(response);
        List<Course> courses = new ArrayList<>();
        
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject json = jsonArray.getJSONObject(i);
            Course course = new Course(
                json.optInt("cno", 0),
                json.optString("cname", ""),
                json.optInt("cpno", 0),
                json.optDouble("ccredit", 0.0)
            );
            courses.add(course);
        }
        return courses;
    }

    public Course getCourse(int cno) throws Exception {
        String response = HttpUtil.sendGetRequest("/course/get/" + cno);
        JSONObject json = new JSONObject(response);
        return new Course(
            json.optInt("cno", 0),
            json.optString("cname", ""),
            json.optInt("cpno", 0),
            json.optDouble("ccredit", 0.0)
        );
    }
} 