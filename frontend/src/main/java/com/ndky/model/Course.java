package com.ndky.model;

public class Course {   // 课程类
    private int cno;        // 课程号
    private String cname;   // 课程名
    private int cpno;       // 前修课程
    private double ccredit; // 学分

    public Course() {}

    public Course(int cno, String cname, int cpno, double ccredit) {
        this.cno = cno;
        this.cname = cname;
        this.cpno = cpno;
        this.ccredit = ccredit;
    }

    // 获取和设置
    public int getCno() { return cno; }
    public void setCno(int cno) { this.cno = cno; }
    public String getCname() { return cname; }
    public void setCname(String cname) { this.cname = cname; }
    public int getCpno() { return cpno; }
    public void setCpno(int cpno) { this.cpno = cpno; }
    public double getCcredit() { return ccredit; }
    public void setCcredit(double ccredit) { this.ccredit = ccredit; }
} 