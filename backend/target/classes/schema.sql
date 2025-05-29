-- 创建学生表
CREATE TABLE IF NOT EXISTS student (
    sno INTEGER PRIMARY KEY,
    sname TEXT NOT NULL,
    ssex TEXT NOT NULL,
    sage INTEGER NOT NULL,
    sdept TEXT NOT NULL
);

-- 插入示例数据
INSERT OR REPLACE INTO student (sno, sname, ssex, sage, sdept) VALUES
    (20170111, '某张', '男', 18, 'IS'),
    (201215121, '李勇', '男', 20, 'CS'),
    (201215122, '刘晨', '女', 19, 'CS'),
    (201215123, '王敏', '女', 18, 'MA'),
    (201215125, '张立', '男', 19, 'IS'); 