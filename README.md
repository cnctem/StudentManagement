# 学生管理系统 (Student Management System)

## 项目简介
这是一个基于Java Swing开发的学生管理系统，采用前后端分离架构，提供完整的学生信息、课程管理和成绩管理功能。系统具有友好的图形用户界面，支持数据的增删改查和统计分析。

## 功能特性
- 学生管理
  - 学生信息的增删改查
  - 学生信息导入导出
  - 学生信息统计分析
- 课程管理
  - 课程信息的增删改查
  - 课程信息导入导出
  - 课程统计分析
- 成绩管理
  - 成绩录入与修改
  - 成绩查询与统计
  - 成绩报表导出
- 数据可视化
  - 学生成绩分布图表
  - 课程成绩趋势分析
  - 综合成绩排名展示

## 技术栈
### 前端
- Java Swing
- JFreeChart (数据可视化)
- Apache POI (Excel导入导出)

### 后端
- Spring Boot
- SQLite
- MyBatis
- RESTful API

## 系统要求
- JDK 1.8+
- Maven 3.6+
- SQLite 3

## 快速开始

### 1. 克隆项目
```bash
git clone https://github.com/yourusername/StudentManagement.git
cd StudentManagement
```

### 2. 编译项目
```bash
# 编译后端
cd backend
mvn clean package

# 编译前端
cd ../frontend
mvn clean package
```

### 3. 运行项目
```bash
# 启动后端服务
cd backend
java -jar target/backend-1.0.0.jar

# 启动前端应用
cd ../frontend
java -jar target/frontend-1.0.0.jar
```

## 项目结构
```
StudentManagement/
├── frontend/                # 前端项目
│   ├── src/                # 源代码
│   └── pom.xml            # Maven配置
├── backend/                # 后端项目
│   ├── src/               # 源代码
│   └── pom.xml           # Maven配置
└── docs/                  # 项目文档
```

## 开发指南
详细的开发文档请参考 `docs/` 目录下的文档：
- [前端开发指南](docs/frontend_dev.md)
- [后端开发指南](docs/backend_dev.md)
- [开发日志](docs/开发日志.md)

## 版本历史
- v1.0.0 (2024-03-xx)
  - 初始版本发布
  - 实现基本的学生、课程、成绩管理功能


