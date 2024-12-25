-- 数据库操作相关
CREATE DATABASE test;  -- 创建数据库
SHOW DATABASES;  -- 显示当前数据库
DROP DATABASE test;   -- 删除当前库


-- 表操作相关
CREATE TABLE test(    -- 在test数据库创建表
    id int,
    Nme VARCHAR(100),
    age int
);
DESC test;    -- 获取指定表的结构信息
-- 下列都是修改表结构的语句，ALTER TABLE是 MySQL 中用于修改表结构的关键字组合
ALTER TABLE test ADD email VARCHAR(50);    -- 向表中添加字段
ALTER TABLE test DROP COLUMN email;     -- 删除表中的字段
ALTER TABLE test RENAME COLUMN Nme TO Name;   -- Nme 列名被成功更改为 Name
ALTER TABLE test MODIFY COLUMN age smallint;  -- age列修改为smallint数据类型
ALTER TABLE test ALTER COLUMN age SET DEFAULT 18;  -- 向某列设置默认值
DROP TABLE test;   -- 删除表



-- 数据的增删改查
-- 增，查
INSERT INTO test(id, Name,age) VALUES (1, 'zjm',56);  -- 向表中插入数据
SELECT * FROM test;  -- 查询某表所有信息
INSERT INTO test(id, Name, age) VALUES (1, 'zjm',18),(2,'zjm2',555);  -- 可以批量插入数据
INSERT INTO test(id, Name) VALUES (4, 'zjm578');   -- 向表中可用插入不完全的数据
INSERT INTO test(age) VALUE (46);
-- 改
UPDATE test SET Name = 'zjm6' WHERE age = 46;   -- 向表test中name列，其中age为46的更改
UPDATE test SET Name = 'zjm455';   -- 全改变，没有where条件，不安全
-- 删
DELETE FROM test WHERE Name = 'zjm6';   -- 删除表中某一栏的数据，where为条件
DELETE FROM test;  -- 删库跑路



-- 数据的导入和导出



-- 常见查询场景
SELECT * FROM test;
SELECT * FROM test WHERE age > 30;
SELECT * FROM test WHERE age < 30;
SELECT * FROM test WHERE age IN (18, 555);
SELECT * FROM test WHERE age >= 30 AND age < 177;
SELECT * FROM test WHERE age between 30 and 177;  -- 包含边界值
SELECT * FROM test WHERE Name like 'zjm%';  -- 模糊查询，其中查询name为zjm开头的数据
SELECT * FROM test WHERE Name like '%zjm%';  -- 模糊查询，查询name包括zjm字母的数据
SELECT COUNT(*) FROM test;    -- 统计行数
SELECT distinct age FROM test;   -- 带去重的查询
SELECT distinct age FROM test LIMIT 2;   -- 限制查询几条
SELECT SUBSTR(age, 1, 1) FROM test WHERE Name Like '%zjm' LIMIT 2;   -- SUBSTR截取字符串的一部分,从字符串的第一个字符位置开始截取长度为 1 个字符
SELECT SUBSTR(Name, 1, 4) AS NickName FROM test WHERE Name Like '%zjm';   -- 返回的结果会起别名
SELECT age, COUNT(*) from test GROUP BY age;    -- 分组查询，返回的内容为组的形式
SELECT age, COUNT(*) from test GROUP BY age HAVING age > 54;   -- 进行限制


-- 更高级的查询

-- INNER JOIN 关键字用于连接两个表，ON 后面指定连接的条件，也就是 test 表中的 id 列和 orders 表中的 customer_id 列相等时,将两个表中对应的行组合在一起进行查询
-- SELECT test.name, orders.order_amount FROM test INNER JOIN orders ON test.id = orders.customer_id;

-- 对 test 表按照年龄进行排序，并为每一行数据添加一个序号，显示序号、姓名和年龄信息。
SELECT ROW_NUMBER() OVER (ORDER BY age) AS row_num, name, age FROM test;

-- 分组后排序并限制每组结果数量（ROW_NUMBER() 窗口函数结合 GROUP BY 使用）
-- 首先在子查询中使用 ROW_NUMBER() 窗口函数，PARTITION BY department 表示按照 department 列进行分组（也就是每个部门为一个分区），在每个分区内再按照 performance 列值降序（DESC）排序，并给每行分配一个排名序号 ranking。然后在外部查询中通过 WHERE 条件筛选出排名序号小于等于 3 的记录
-- SELECT name, department, performance
-- FROM (
    -- SELECT name, department, performance,
           -- ROW_NUMBER() OVER (PARTITION BY department ORDER BY performance DESC) AS ranking
    -- FROM test
-- ) AS subquery
-- WHERE ranking <= 3;

-- 使用 CASE WHEN 语句进行条件判断与统计
-- 在 SELECT 语句中使用 CASE WHEN 语句进行条件判断，根据不同的年龄条件将员工划分到不同的年龄组（age_group）中，然后通过 GROUP BY 按照这个划分后的年龄组进行分组，再使用 COUNT(*) 统计每个年龄组内的员工数量
SELECT
    CASE
        WHEN age <= 30 THEN '青年'
        WHEN age <= 50 THEN '中年'
        ELSE '老年'
    END AS age_group,
    COUNT(*) AS count_people
FROM test
GROUP BY
    CASE
        WHEN age <= 30 THEN '青年'
        WHEN age <= 50 THEN '中年'
        ELSE '老年'
    END;

-- 使用 UNION 或 UNION ALL 进行多结果集合并（以 UNION 为例）
-- 假设有两个结构相似的表 test1（包含 name，age 等列）和 test2（同样包含 name，age 等列），现在要将这两个表中年龄大于 25 的员工信息合并在一起，去除重复记录后展示出来。
SELECT name, age
FROM test
WHERE age > 25
UNION
SELECT name, age
FROM test1
WHERE age > 25;

-- 使用 LAG() 或 LEAD() 函数进行数据偏移查询（以 LAG() 为例）
-- 在 test 表中，查询每个员工的姓名、年龄以及比他年龄小一岁的员工的姓名（如果存在的话），用于对比相邻年龄员工的情况
-- LAG() 是一个窗口函数，LAG(name, 1)就是比当前行年龄小一岁的员工的姓名,起别名prev_name在查询结果展示
SELECT name, age, LAG(name, 1) OVER (ORDER BY age) AS prev_name FROM test;



-- MySQL子查询
SELECT * FROM test WHERE age > (SELECT AVG(age) FROM test);  -- AVG为平均，查询大于平均年龄的数据
SELECT * ,(SELECT AVG(age) FROM test) AS avgAge FROM test WHERE age > (SELECT AVG(age) FROM test);    -- 在查询结果中把平均年龄的结果起了一个别名展示出来然后查询大于平均年龄的数据
CREATE TABLE test1 SELECT * FROM test WHERE age > (SELECT AVG(age) FROM test);   -- 创建一个test1表，把表中大于平均年龄的数据导入
INSERT INTO test1 SELECT * FROM test WHERE age < (SELECT AVG(age) FROM test);  -- 把test中小于平均年龄的数据插入



-- 表关联查询

-- inner join 内连接
-- 会返回两个表中满足关联条件的行的组合。只有在两个表中关联字段值相等的记录才会被选取出来参与结果集的构建。
 /*
SELECT 列名列表 FROM 表1
INNER JOIN 表2 ON 表1.关联字段 = 表2.关联字段;
  */

-- LEFT JOIN 左连接  RIGHT JOIN 右连接 也类似
-- 左连接以 “左表”（即 FROM 关键字后面指定的表 1）为基础，返回左表中的所有行以及与右表（表 2）中满足关联条件的行进行匹配组合。如果右表中没有对应的匹配行，那么对应列的值将显示为 NULL。
/*
SELECT 列名列表 FROM 表1
LEFT JOIN 表2 ON 表1.关联字段 = 表2.关联字段;
 */

-- 全连接
-- MySQL 本身不直接支持，可通过 UNION 等方式模拟实现
-- 全连接会返回两个表中所有行的组合，不管这些行在另一个表中是否有匹配的记录。如果某一行在另一个表中没有匹配行，对应的列就会填充 NULL 值
/*
SELECT 列名列表 FROM 表1
LEFT JOIN 表2 ON 表1.关联字段 = 表2.关联字段
UNION
SELECT 列名列表 FROM 表1
RIGHT JOIN 表2 ON 表1.关联字段 = 表2.关联字段;
*/

-- CROSS JOIN 进行笛卡尔积查询
-- 假设有 colors 表（包含 color_name 列）和 shapes 表（包含 shape_name 列），现在要获取所有颜色和形状的组合情况，比如红色的圆形、蓝色的方形等
/*
SELECT colors.color_name, shapes.shape_name
FROM colors
CROSS JOIN shapes;
*/


