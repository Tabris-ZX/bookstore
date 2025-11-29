

//设置/查看book表的自增起始值,测试后恢复,防止脏数据
```mysql
ALTER TABLE books AUTO_INCREMENT = 0;

SHOW TABLE STATUS LIKE 'books';
```
