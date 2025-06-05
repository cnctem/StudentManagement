# 学生成绩管理系统前端开发文档

## 1. 项目架构

### 1.1 包结构
```
com.ndky
├── StudentManagementGUI.java    # 主窗口类
├── gui                          # 界面组件包
│   ├── StudentPanel.java        # 学生管理面板
│   ├── CoursePanel.java         # 课程管理面板
│   ├── ScorePanel.java          # 成绩管理面板
│   ├── StudentStatisticsPanel.java  # 学生统计面板
│   └── CourseStatisticsPanel.java   # 课程统计面板
├── model                        # 数据模型包
│   ├── Student.java            # 学生模型
│   ├── Course.java             # 课程模型
│   └── Score.java              # 成绩模型
├── service                      # 业务逻辑包
│   ├── StudentService.java     # 学生服务
│   ├── CourseService.java      # 课程服务
│   └── ScoreService.java       # 成绩服务
└── util                         # 工具包
    └── HttpUtil.java           # HTTP请求工具类
```

### 1.2 技术栈
- Java 17：开发语言
- Swing：GUI界面开发
- JFreeChart：数据可视化
- JSON：数据序列化/反序列化
- HTTP：与后端通信
- Maven：项目管理

## 2. 核心功能模块

### 2.1 主窗口（StudentManagementGUI）
- 使用JTabbedPane实现多标签页界面
- 管理五个主要功能模块：学生管理、课程管理、成绩管理、学生统计、课程统计
- 窗口大小：1200x800
- 统一的系统外观

### 2.2 学生管理模块（StudentPanel）
- 功能：
  - 学生信息的增删改查
  - 按姓名搜索学生
  - 表格形式展示学生列表
- 实现：
  - 使用JTable展示学生数据
  - 实现CRUD操作的对话框
  - 集成StudentService处理业务逻辑

### 2.3 课程管理模块（CoursePanel）
- 功能：
  - 课程信息的增删改查
  - 课程列表展示
  - 课程信息验证
- 实现：
  - 使用JTable展示课程数据
  - 集成CourseService获取课程信息

### 2.4 成绩管理模块（ScorePanel）
- 功能：
  - 成绩信息的增删改查
  - 按学号和课程号筛选成绩
  - 成绩信息验证
- 实现：
  - 使用JTable展示成绩数据
  - 实现多条件搜索功能
  - 集成ScoreService处理成绩相关操作

### 2.5 学生统计模块（StudentStatisticsPanel）
- 功能：
  - 查询学生所有课程成绩
  - 计算学生平均分
  - 显示学生成绩分布图表
  - 支持按学号查询
- 实现：
  - 使用JFreeChart生成饼图
  - 实时计算统计数据
  - 动态更新图表显示

### 2.6 课程统计模块（CourseStatisticsPanel）
- 功能：
  - 查询课程所有学生成绩
  - 计算课程统计数据（平均分、最高分、最低分）
  - 显示课程成绩分布图表
  - 支持按课程号查询
- 实现：
  - 使用JFreeChart生成饼图
  - 成绩等级分类统计
  - 支持图表缩放

## 3. 数据模型

### 3.1 Student模型
```java
public class Student {
    private int sno;        // 学号
    private String sname;   // 姓名
    private String ssex;    // 性别
    private int sage;       // 年龄
    private String sdept;   // 院系
}
```

### 3.2 Course模型
```java
public class Course {
    private int cno;        // 课程号
    private String cname;   // 课程名称
    private int cpno;       // 前修课程
    private double ccredit; // 学分
}
```

### 3.3 Score模型
```java
public class Score {
    private int sno;        // 学号
    private String sname;   // 学生姓名
    private int cno;        // 课程号
    private String cname;   // 课程名称
    private int grade;      // 成绩
    private String sdept;   // 院系
    private double ccredit; // 学分
}
```

## 4. 服务层实现

### 4.1 HttpUtil
- 封装HTTP请求方法
- 支持GET、POST、PUT、DELETE操作
- 处理JSON数据格式
- 统一的错误处理机制

### 4.2 各Service类
- 封装与后端API的交互
- 处理数据转换和业务逻辑
- 提供统一的错误处理机制
- 支持异步数据加载

## 5. 界面设计

### 5.1 布局设计
- 采用BorderLayout作为主要布局管理器
- 使用JPanel进行组件分组
- 实现响应式布局
- 统一的组件间距

### 5.2 交互设计
- 统一的错误提示机制
- 操作确认对话框
- 数据实时刷新
- 图表交互优化

## 6. 开发规范

### 6.1 代码规范
- 遵循Java命名规范
- 使用统一的代码格式化
- 添加必要的注释
- 模块化设计

### 6.2 异常处理
- 统一的异常处理机制
- 用户友好的错误提示
- 日志记录关键操作
- 优雅的错误恢复

## 7. 部署说明

### 7.1 环境要求
- JDK 17+
- Maven 3.6+
- 后端服务正常运行

### 7.2 构建步骤
```bash
# 编译打包
mvn clean install

# 运行程序
mvn exec:java
```
