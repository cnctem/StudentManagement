package com.ndky.model;

public class Student {   // 学生类
    private int sno;        // 学号
    private String sname;   // 学生姓名
    private String ssex;    // 性别
    private int sage;       // 年龄
    private String sdept;   // 系别

    public Student() {}

    public Student(int sno, String sname, String ssex, int sage, String sdept) {
        this.sno = sno;
        this.sname = sname;
        this.ssex = ssex;
        this.sage = sage;
        this.sdept = sdept;
    }

    // 获取和设置
    public int getSno() { return sno; }
    public void setSno(int sno) { this.sno = sno; }
    public String getSname() { return sname; }
    public void setSname(String sname) { this.sname = sname; }
    public String getSsex() { return ssex; }
    public void setSsex(String ssex) { this.ssex = ssex; }
    public int getSage() { return sage; }
    public void setSage(int sage) { this.sage = sage; }
    public String getSdept() { return sdept; }
    public void setSdept(String sdept) { this.sdept = sdept; }
} 