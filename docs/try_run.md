# 启动服务流程

## 1. 启动后端服务
```zsh
cd backend
mvn spring-boot:run
```
- 等待控制台出现 `StudentManagementApplication in xxx seconds`后端启动成功。
- 数据库会自动初始化，执行 `Doc/Student.sql` 脚本。

## 2. 启动前端服务
新建终端
```zsh
cd frontend
mvn exec:java -Dexec.mainClass="com.ndky.StudentManagementGUI"
```

