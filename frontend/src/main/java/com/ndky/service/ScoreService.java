package com.ndky.service;

import com.ndky.model.Score;
import com.ndky.util.HttpUtil;
import org.json.JSONArray;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;

public class ScoreService {
    public List<Score> getAllScores() throws Exception {
        String response = HttpUtil.sendGetRequest("/score/list");
        JSONArray jsonArray = new JSONArray(response);
        List<Score> scores = new ArrayList<>();
        
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject json = jsonArray.getJSONObject(i);
            Score score = new Score(
                json.optInt("sno", 0),
                json.optString("sname", ""),
                json.optInt("cno", 0),
                json.optString("cname", ""),
                json.optInt("grade", 0),
                json.optString("sdept", ""),
                json.optDouble("ccredit", 0.0)
            );
            scores.add(score);
        }
        return scores;
    }

    public List<Score> getStudentScores(int sno) throws Exception {
        String response = HttpUtil.sendGetRequest("/score/student/" + sno);
        JSONArray jsonArray = new JSONArray(response);
        List<Score> scores = new ArrayList<>();
        
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject json = jsonArray.getJSONObject(i);
            Score score = new Score(
                json.optInt("sno", 0),
                json.optString("sname", ""),
                json.optInt("cno", 0),
                json.optString("cname", ""),
                json.optInt("grade", 0),
                json.optString("sdept", ""),
                json.optDouble("ccredit", 0.0)
            );
            scores.add(score);
        }
        return scores;
    }

    public List<Score> getCourseScores(int cno) throws Exception {
        String response = HttpUtil.sendGetRequest("/score/course/" + cno);
        JSONArray jsonArray = new JSONArray(response);
        List<Score> scores = new ArrayList<>();
        
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject json = jsonArray.getJSONObject(i);
            Score score = new Score(
                json.optInt("sno", 0),
                json.optString("sname", ""),
                json.optInt("cno", 0),
                json.optString("cname", ""),
                json.optInt("grade", 0),
                json.optString("sdept", ""),
                json.optDouble("ccredit", 0.0)
            );
            scores.add(score);
        }
        return scores;
    }
} 