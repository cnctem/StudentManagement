package com.ndky.model;

public class Score {   // 成绩类
    private int sno;        // 学号
    private String sname;   // 学生姓名
    private int cno;        // 课程号
    private String cname;   // 课程名
    private int grade;      // 成绩
    private String sdept;   // 系别
    private double ccredit; // 学分

    public Score() {}

    public Score(int sno, String sname, int cno, String cname, int grade, String sdept, double ccredit) {
        this.sno = sno;
        this.sname = sname;
        this.cno = cno;
        this.cname = cname;
        this.grade = grade;
        this.sdept = sdept;
        this.ccredit = ccredit;
    }

    // 获取和设置
    public int getSno() { return sno; }
    public void setSno(int sno) { this.sno = sno; }
    public String getSname() { return sname; }
    public void setSname(String sname) { this.sname = sname; }
    public int getCno() { return cno; }
    public void setCno(int cno) { this.cno = cno; }
    public String getCname() { return cname; }
    public void setCname(String cname) { this.cname = cname; }
    public int getGrade() { return grade; }
    public void setGrade(int grade) { this.grade = grade; }
    public String getSdept() { return sdept; }
    public void setSdept(String sdept) { this.sdept = sdept; }
    public double getCcredit() { return ccredit; }
    public void setCcredit(double ccredit) { this.ccredit = ccredit; }
} 