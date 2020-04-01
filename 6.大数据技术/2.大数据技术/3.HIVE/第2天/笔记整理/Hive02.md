## Hive02

-----

### 元数据

> Hive中，库名，表名，字段名，分区等信息都是HIVE的元数据
>
> Hive中的元数据，如果是存在于**关系型数据库**中，如果不指定，默认使用的数据库是**Derby**，可以手动更换为**mysql**
>
> 元数据的默认字符集	`ISO8859-1`

> 切换为Mysql数据库之后，如果在启动Hive的过程中，出现`READ COMMITED or UNREAD COMMITED`
>
> > `vim /usr/my.cnf`
> >
> > 在文件末尾添加: `binlog_format=mixed`
> >
> > 重启mysql：`service mysql restart`



### 复杂数据类型

#### 数组类型

> 建表语句
>
> > 建表 案例一
> >
> > > `create external table ex(vals array<int>) row format delimited fields terminated by '\t' collection items terminated by ',' location '/ex'`
> >
> > 建表 案例二
> >
> > > `create external table ex1(info1 array<int>,info2 array<string>) row format delimited fields terminated by '\t' collection items terminated by ',' location '/ex'`

> 查询数据
>
> > `select vals[5] from ex`
>
> 非空查询
>
> > `select vals[5] from ex where vals[5] is not null`

#### MAP类型

> MAP类型，列的分隔符必须是\t

> 建表 案例一
>
> > `create external table m1(vals map<string,int>) row format delimited fields terminated by '\t' map keys terminated by ',' location '/map'`
>
> 建表 案例二
>
> > `create external table ex(vals map<string,string>) row format delimited fields terminated by '\t' map keys terminated by ' ' location '/ex'`

> 查询数据
>
> > `select vals['tom'] from ex where vals['tom'] is not null`

#### struct类型

> struct类型的数据可以通过'列名.字段名'的方式访问

> 建表 案例
>
> > `create external table score(info struct<name:string,chinese:int,math:int,english:int>) row format delimited collection items terminated by ' ' location '/score'`

> 查询 案例
>
> > `select vals.age from ex where vals.name='tom'`



### 视图	-view

> 视图的目的是为了**提高查询效率**
>
> 视图的创建实际上只是保留一份**元数据**
>
> 如果视图存储在磁盘上则被称之为**物化视图**；如果存储在内存中则被成为**虚拟试图**，Hive只支持**虚拟视图**
>
> Hive在视图进行封装的时候，封装的查询语句并不执行，只有在**第一次使用视图**的时候**才会触发封装的这条语句**

> 创建指令
>
> > `create view viewname as select [ ]  from [ ]`



### explode

> 将数组的每一个元素都提取出来作为单独的一行

> 案例 单词统计
>
> > 1.先建立外部表管理已经存在的这个文件
> >
> > > `create external table words(line stirng) location '/words'`
> >
> > 2.将数据进行切分
> >
> > > `select spilt(line,' ') from worlds`
> >
> > 3.将数组中的每一个元素独立成一个字
> >
> > > `select explode(split(line,' ')) from worlds`
> >
> > 4.将相同的单词放到一组，统计每一组单词出现的个数
> >
> > > `select w,count(w) from (select explode(split(line,' ')) from worlds) ws group by w`



### SerDe

> SerDe机制提供了**正则表达式**对不规则数据来进行提取
>
> 在提取过程中，正则表达式中的**每一个捕获组对应Hive表中的一个字段**

> 案例
>
> > `create table log ( host string, time string,request string,paths string,way string,stat int) `
> >
> > `row format serde 'org.apache.hadoop.hive.serde2.RegexSerDe' `
> >
> > `with serdeproperties ( "input.regex" = "(.*) \-\- \\[(.*)\\] \"(.*) (.*) (.*)\" ([0-9]+) \-") stored as textfile;`



### 索引

> 建立索引的目的是为了提高查询效率
>
> 因为Hive中没有主键的概念，所以默认是没有索引的

> `create index s_index on table stu(id) as 'org.apache.hadoop.hive.ql.index.compact.CompactIndexHandler' with deferred rebuild in table stu_index;`
>
> > 创建索引表`stu_index`
> >
> > 为stu表中的id字段创建索引，索引名为`s_index`
>
> `alter index s_index on stu rebuild;`
>
> > 为索引表添加数据
>
> `drop index s_index on stu;`
>
> > 删除缩影



### beeline

> 远程连接Hive

> 以服务的形式启动Hive
>
> > `sh hive --service hiveserver2 &`
>
> 在任意节点连接Hive
>
> > `beeline -u jdbc:hive2://hadoop01:10000/hivedemo   -n root`
> >
> > > 连接Hadoop01主机，用户为hivedemo（匿名用户）