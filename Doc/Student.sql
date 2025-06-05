-- SQLite数据库初始化脚本

-- 创建学生表
CREATE TABLE IF NOT EXISTS student (
    sno INTEGER PRIMARY KEY,
    sname TEXT NOT NULL,
    ssex TEXT CHECK(ssex IN ('男', '女')),
    sage INTEGER,
    sdept TEXT
);

-- 创建课程表
CREATE TABLE IF NOT EXISTS course (
    cno INTEGER PRIMARY KEY,
    cname TEXT NOT NULL,
    cpno INTEGER,
    ccredit REAL
);

-- 创建成绩表
CREATE TABLE IF NOT EXISTS score (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    sno INTEGER,
    sname TEXT,
    cno INTEGER,
    cname TEXT,
    grade INTEGER,
    sdept TEXT,
    ccredit REAL,
    FOREIGN KEY (sno) REFERENCES student(sno),
    FOREIGN KEY (cno) REFERENCES course(cno)
);

-- 插入示例数据
INSERT INTO student (sno, sname, ssex, sage, sdept) VALUES
(201215121, '李勇', '男', 20, 'CS'),
(201215122, '刘晨', '女', 19, 'CS'),
(201215123, '王敏', '女', 18, 'MA'),
(201215125, '张立', '男', 19, 'IS');

-- 先插入没有前置课程的课程
INSERT INTO course (cno, cname, cpno, ccredit) VALUES
(2, '数学', NULL, 2),
(6, '数据处理', NULL, 2);

-- 再插入有前置课程的课程
INSERT INTO course (cno, cname, cpno, ccredit) VALUES
(7, 'PASCAL语言', 6, 4),
(5, '数据结构', 7, 4),
(1, '数据库', 5, 4),
(3, '信息系统', 1, 4),
(4, '操作系统', 6, 3);

-- 插入成绩数据
INSERT INTO score (sno, sname, cno, cname, grade, sdept, ccredit) VALUES
(201215121, '李勇', 1, '数据库', 92, 'CS', 4),
(201215121, '李勇', 2, '数学', 85, 'CS', 2),
(201215121, '李勇', 3, '信息系统', 88, 'CS', 4),
(201215122, '刘晨', 2, '数学', 90, 'CS', 2),
(201215122, '刘晨', 3, '信息系统', 80, 'CS', 4);
