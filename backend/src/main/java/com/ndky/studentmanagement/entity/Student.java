package com.ndky.studentmanagement.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("student")
public class Student {
    @TableId(type = IdType.INPUT)
    private Integer sno;
    private String sname;
    private String ssex;
    private Integer sage;
    private String sdept;
}