package com.ndky.service;

import com.ndky.model.Score;
import com.ndky.util.HttpUtil;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.json.JSONArray;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;

public class ScoreService {
    private final Gson gson = new Gson();

    public List<Score> getAllScores() throws Exception {
        String response = HttpUtil.sendGet("/score/list");
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

    public List<Score> getStudentScores(int sno) {
        String response = HttpUtil.sendGet("/score/student/" + sno);
        return gson.fromJson(response, new TypeToken<List<Score>>(){}.getType());
    }

    public List<Score> getCourseScores(int cno) {
        String response = HttpUtil.sendGet("/score/course/" + cno);
        return gson.fromJson(response, new TypeToken<List<Score>>(){}.getType());
    }

    public boolean addScore(Score score) {
        String json = gson.toJson(score);
        String response = HttpUtil.sendPost("/score/add", json);
        return Boolean.parseBoolean(response);
    }

    public boolean updateScore(Score score) {
        String json = gson.toJson(score);
        String response = HttpUtil.sendPut("/score/update", json);
        return Boolean.parseBoolean(response);
    }

    public boolean deleteScore(int sno, int cno) {
        String response = HttpUtil.sendDelete("/score/delete/" + sno + "/" + cno);
        return Boolean.parseBoolean(response);
    }
} 