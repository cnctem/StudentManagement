package com.ndky.studentmanagement.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("score")
public class Score {
    @TableId(type = IdType.AUTO)
    private Integer id;
    private Integer sno;
    private String sname;
    private Integer cno;
    private String cname;
    private Integer grade;
    private String sdept;
    private Double ccredit;
} 