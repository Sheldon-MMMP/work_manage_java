# 数据库建表脚本

## 执行方式

在项目根目录下，使用 `psql` 连接你的 PostgreSQL 后执行：

```bash
# 方式一：命令行直接执行（按 application.yaml 中的连接信息修改主机、库名、用户）
psql -h 192.168.3.128 -p 5432 -U manage_work -d manage_work -f src/main/resources/db/schema.sql

# 方式二：先进入 psql，再执行
psql -h 192.168.3.128 -p 5432 -U manage_work -d manage_work
\i src/main/resources/db/schema.sql
```

脚本使用 `CREATE TABLE IF NOT EXISTS`，重复执行不会报错，已存在的表不会被修改。

## 表说明

| 表名 | 说明 |
|------|------|
| users | 用户 |
| folders | 文件夹 |
| task_lists | 清单 |
| tasks | 任务 |
| time_records | 时间记录 |
| anniversaries | 纪念日 |
