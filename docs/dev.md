# 学生管理系统开发文档

## 1. 项目概述

本项目是一个基于 Spring Boot 的学生管理系统，采用前后端分离的架构设计。系统主要用于管理学生信息、课程信息、成绩信息等教育相关数据。

## 2. 技术栈

### 2.1 后端技术栈
- Spring Boot 2.7.0
- MyBatis-Plus 3.5.1
- SQLite 3.43.2
- Lombok
- JSON 20231013

### 2.2 前端技术栈
- Java Swing/AWT

## 3. 项目结构
```
StudentManagement/
├── backend/                # 后端项目目录
│   ├── src/               # 源代码
│   └── pom.xml           # Maven 配置文件
├── frontend/              # 前端项目目录
│   ├── src/              # 源代码
│   └── pom.xml          # Maven 配置文件
├── docs/                 # 文档目录
└── pom.xml              # 主项目 Maven 配置文件
```

## 4. 开发环境要求
- JDK 1.8
- Maven 3.6+
- SQLite 3.43.2

## 5. 快速开始

### 5.1 后端启动
1. 进入 backend 目录
2. 执行 `mvn spring-boot:run`
3. 系统会自动创建 `student.db` 文件并初始化数据

### 5.2 前端启动
1. 进入 frontend 目录
2. 执行 `mvn spring-boot:run`

## 6. 数据库设计

### 6.1 数据库说明
- 使用 SQLite 作为数据库
- 数据库文件位置：项目根目录下的 `student.db`
- 无需额外安装数据库服务器
- 支持自动创建数据库和表结构

### 6.2 主要数据表
- 学生表（student）
  - sno: INTEGER PRIMARY KEY
  - sname: TEXT
  - ssex: TEXT
  - sage: INTEGER
  - sdept: TEXT

## 7. API 接口设计

### 7.1 RESTful API 规范
- GET /api/students - 获取学生列表
- POST /api/students - 创建新学生
- PUT /api/students/{id} - 更新学生信息
- DELETE /api/students/{id} - 删除学生

## 8. 部署说明

### 8.1 后端部署
1. 打包：`mvn clean package`
2. 运行：`java -jar target/backend-1.0-SNAPSHOT.jar`
3. 确保运行目录有写入权限（用于创建数据库文件）

### 8.2 前端部署
1. 打包：`mvn clean package`
2. 运行：`java -jar target/frontend-1.0-SNAPSHOT.jar`

## 9. 数据库维护

### 9.1 备份
- 定期备份 `student.db` 文件
- 建议在系统维护时进行备份

### 9.2 恢复
- 停止应用
- 替换 `student.db` 文件
- 重启应用