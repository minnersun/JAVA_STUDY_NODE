## Hive目录

-----

### 第一天

#### 概述

> Hive事基于Hadoop的**数据仓库**工具
>
> Hive提供了**类Sql语句**来对分布式系统的数据进行**提取，转化，加载**(ETL	Extract-Transform-Load)
>
> Hive将SQL语句在底层转化为**MapReduce程序**来操作数据
>
> 本质上是一个**大数据离线分析**工具

##### 数据库和数据仓库的区别

|                | 数据仓库                      | 数据库                        |
| -------------- | ----------------------------- | ----------------------------- |
| 数据量         | >=TB                          | <=GB                          |
| 数据种类和来源 | 数据种类和来源相对丰富        | 数据种类和来源比较单一        |
| 事务           | 弱事务或者是不支持事务        | 强调事务                      |
| 数据操作       | 一次写入多次读取              | 完整的增删改查的能力          |
| 数据行为       | 历史数据                      | 实时捕获数据                  |
| 冗余           | 人为的制造冗余                | 数据要尽量的精简，避免冗余    |
| 支持系统       | OLAP   - 联机**分析处理**系统 | OLTP   - 联机**事务处理**系统 |
| 面向人员       | 市场、管理等非技术人员        | 开发人员、DBA等技术人员       |

##### 细节

> Hive中每建立一个**库**，都会在**HDFS**对应的地方创建一个目录
>
> Hive中每一张**表**在**HDFS**上都对应一个**目录**
>
> Hive中**没有主键**的概念
>
> 当利用Hive管理本地数据的时候，会将要管理的数据上传到**HDFS**上
>
> 当一张表已经创建好后，这个表中的**字段间隔符**，就不能改变
>
> 从Hive查询数据向本地目录中写的时候**只能覆盖**

#### 基础命令

##### 基础命令1

> `show databases`
>
> > 查看数据库
>
> `create databases park`
>
> > 创建park数据库
> >
> > 存在与hadoop节点下的`/user/hive/warehouse`目录下
>
> `use park`
>
> > 进入park数据库
>
> `show tables`
>
> > 查看当前数据库下所有表
>
> `create table stu(id int,name string)`
>
> > 创建stu表，及表中的字段
>
> `insert into stu values(1，'zhang')`
>
> > 向stu表中插入数据
>
> `select * from stu`
>
> > 查看表数据
>
> `drop table stu`
>
> > 删除表
>
> `desc stu`
>
> > 查看stu表结构

##### 基础命令2

> `load data local inpath '/home/software/1.txt' into table stu`
>
> > 加载指定的文件数据到指定的表中
> >
> > 执行完成后stu目录下多了一个`1.txt`
>
> `create table stu1(id int,name string) row format delimited fields terminated by ' '`
>
> > 创建stu1表，并指定分隔符为空格
>
> `create table stu2 like stu`
>
> > 创建stu2，表结构与stu相同
> >
> > 不复制数据
>
> `insert overwrite table stu2 select * from stu`
>
> > 将stu表数据插入到stu2的表中
>
> `insert overwrite local directory '/home/stu' row format delimited fields terminated by ' ' select * from stu`
>
> > 将stu表中查询的数据写到本地的/home/stu目录下
>
> `insert overwrite directory '/stu' row format delimited fields terminated by ' 'select * from stu`
>
> > 将stu表中查询的数据写到HDFS的stu目录下
>
> `from stu insert overwrite table stu1 select * insert overwriite table stu2 select *`
>
> > 将stu表中查询的数据谢翱stu1以及stu2两张表中
>
> `alter table stu rename to stu2`
>
> > 表stu重命名为stu2
>
> `alter table stu add columns(age int)`
>
> > 为表增加一个列字段age，类型为int
>
> `exit`
>
> > 退出hive

#### 表结构

> 内部表
>
> > 在**Hive中创建**的放在`/user/hive/warehouse`目录下的表称之为内部表，**需要手动添加**数据
>
> 外部表
>
> > 如果需要**管理HDFS中已存在的数据**，需要建立外部表
> >
> > 外部表**只能指定到目录**，不能指定具体文件
>
> > 外部表创建命令
> >
> > > `create external table stu(id int,name string) row format delimited fields terminated by ' ' location '/目录路径'`

##### 内部表与外部表的区别

> 对于内部表，在**删除该表**的时候，HDFS对应的目录节点**会被**删除
>
> 对于外部表，在**删除该表**的时候，HDFS对应的目录节点**不会**删除

##### 分区表	-partitioned 

> 分区表可以通过添加指定字段来提高Hive的查询效率
>
> 数据量较大的情况下，往往会添加分区表来避免全表查询

###### 分区表指令

> `create table book(id int,name string) partitioned by (category string) row format delimated fields terminated by '\t'`
>
> > 创建boot表，以category作为分区
> >
> > **category字段可以不在字段列表中**，生成的表中自动就会具有该字段
>
> `load data local inpath '/home/cn.txt' overwrite into table book partition (category='cn')`
>
> > 将本地文件`cn.txt`添加到book表中，分区字段为cn
>
> `select * from book where category='cn'`
>
> > 查看分区cn的数据
>
> `alter table book add partition(category='jp') location '/user/hive/warehouse/park.db/book/categoroy='jp'`
>
> > 手动添加分区的修复方式
>
> `show partitions iteblog`
>
> > 查看分区
>
> `msck repair table book`
>
> > 修复分区
>
> `alter table book drop partition(category='cn')`
>
> > 删除分区
>
> `alter table book partition(category='french') rename to partition(categoty='hh')`
>
> > 修改分区名字

###### 动态分区

> 如果在进行查询的时候一个表中的数据是未分区的一个表中的数据是已经分区的
>
> 此时**从未分区的表**中**查询**数据向**已分区表**中**添加**是不行的
>
> 所以此时需要开启动态分区。

> 步骤
>
> > `set hive.exec.dynamic.partition = true;`
> >
> > `set hive.exec.dynamic.partition.mode = nostrict;`
> >
> > 加载数据
> >
> > > `insert into table stu2 partition(class) select id, name, class from stu distribute by class;`
> > >
> > > > 查询的字段按照表字段的顺序，**分区放于最后**



##### 分桶	-clustered 

> 分桶主要实现数据的**抽样**，方便进行数据测试
>
> 分桶通过**Hash算法**，将数据存放在不同的桶中
>
> 分桶默认不开启，**需要手动开启**
>
> > `set hive.enforce.bucketing=true`
>
> 分桶不能以外部文件的方式导入数据，**只能从表中导入**

###### 分桶语法

> 一个表既可以**分区**，也可以**分桶**
>
> 向分桶表中添加数据，**只能**使用`insert`，不能通过`load`
>
> > 如果通过`load`，则分桶无效

> `create table teacher(name string) clustered by (name) into 3 buckets row format delimated fields terminated by ' '`
>
> > 创建teacher表，以name作为分桶机制，分为3个桶
>
> `insert overwrite table teacher select * from tmp`
>
> > 将tmp表中的数据添加到teacher表中
> >
> > 实际上产生了3个文件，用于存储不分桶的数据
>
> `select * from teacher tablesample(bucket 1 out of 3 on name);`
>
> > 进行抽样，步长为3





### 第二天

#### 元数据

> Hive中，库名，表名，字段名，分区等信息都是HIVE的元数据
>
> Hive中的元数据，如果存在于**关系型数据库**中
>
> > 如果不指定，默认使用的是**Derby**，可以手动更换为**mysql**
>
> 元数据的字符集	**ISO8859-1**

> 切换为Mysql数据库之后，如果在启动Hive的过程中，出现`READ COMMITED or UNREAD COMMITED`
>
> > `vim /usr/my.cnf`
> >
> > 在文件末尾添加: `binlog_format=mixed`
> >
> > 重启mysql：`service mysql restart`



#### 复杂数据类型

##### 数组类型

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

##### MAP类型

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

##### struct类型

> struct类型的数据可以通过'列名.字段名'的方式访问

> 建表 案例
>
> > `create external table score(info struct<name:string,chinese:int,math:int,english:int>) row format delimited collection items terminated by ' ' location '/score'`

> 查询 案例
>
> > `select vals.age from ex where vals.name='tom'`



#### 视图	-view

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



#### explode

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



#### SerDe

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



#### 索引

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



#### beeline

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





### 第三天

#### 面试题

##### 如何提高数据库（HIVE）查询效率

> 使用子查询，减少数据量，提高任务数
>
> 建立索引
>
> 创建视图
>
> > 可以减少查询的字段



#### HIVE的优化

##### map side join

> 连接两个表
>
> > 一个比较小的表和一个特别大的表的时候
>
> 将比较小的table**放到内存中**，再对比较大的表格**进行map操作**，此时join就发生在map操作的时候
>
> 每当**扫描一个大的table中的数据**，就要去去**查看小表的数据**，哪条与之相符，**继而进行连接**
>
> 这样小表就不需要单独启动`MapTask`处理

> 设置的方式
>
> > `set hive.auto.convert.join=true`
>
> hive的参数
>
> > `hive.mapjoin.smalltable.filesize`
> >
> > > 默认值是25M
> >
> > 其中一个表小于25M时，自动启用mapjion
>
> 要求：在hive做join时，要求小表在前(左）



##### join语句优化

> `select m.cid,u.id from order m join customer u on m.cid=u.id where m.dt='20160801'`
>
> > 优化前
>
> `select m.cid,u.id from (select cid from order where dt=’20160801’)m join customer u on m.cid = u.id`



##### group by 优化

> 调优参数
>
> > `set hive.groupby.skewindata=true;`
>
> `group by`
>
> > 将相同的数据分为一组，每一个分组会交给一个ReduceTask
> >
> > 在group by过程中，可能会产生Reduce端的**数据倾斜**
>
> `set hive.groupby.skewindata=true;`
>
> > 可以将执行过程拆分成两个**MapReduce**来处理
> >
> > > 第一个MapReduce**将数据打乱**，尽量均匀
> > >
> > > 第二个MapReduce**进行最后的聚合**





##### count distinct优化

> 如果出现了**聚合函数**,那么利用**子查询**进行去重，或者其他操作，最后再进行聚合

> 优化前
>
> > `select count(distinct id) from tablename`
>
> 优化后
>
> > `select count(*) from (select distinct id from tablename) tmp`





##### 调整切片数量

> 默认情况下，一个切片的大小是128M，但是如果处理的文件中，**一行的字段个数比较多**
>
> 可以考虑适当的**减小切片的大小**来**增多MapTask的数量**

> `mapred.max.split.size`
>
> > 默认值为128M
>
> 对于MapTask的数量的调整要根据业务来定
>
> > 比如一个100MB的文件包含了有1千万条数据
> >
> > 此时可以调成10个MapTask则每个MapTask处理1百万条数据。





##### JVM重用

> `set mapred.job.reuse.jvm.num.tasks=20(默认是1个）`
>
> > JVM重用适用于**小文件比较多**
> >
> > 或者**task任务**特别多的场景
>
> JVM重用可以使得一个JVM进程在同一个JOB中重新使用N次后才会销毁





##### 启用严格模式

> `set hive.mapred.mode=strict`
>
> > 设置严格模式
> >
> > `unstrict`：为非严格模式
>
> 严格模式用户运行如下query的时候会报错
>
> > 分区表的查询没有使用分区字段来限制
> >
> > 使用了order by 但没有使用limit语句
> >
> > 产生了笛卡儿积



##### 关闭推测执行机制

> 不适用于数据倾斜
>
> 使用于任务分配不均（极少出现0）

> `set mapreduce.map.speculative=false`
>
> `set mapreduce.reduce.speculative=false`
>
> `set hive.mapred.reduce.tasks.speculative.execution=false`





#### Sqoop

> 是Apache 提供的工具，用于**hdfs**和**关系型数据库**之间数据的导入和导入
>
> > 可以从hdfs导出数据到关系型数据库，也可以从关系型数据库导入数据到hdfs

> 基础指令见课前资料





#### Hive执行流程

> 详见课前资料	简介-》流程
>
> 简略参看老师笔记中的图片

##### Driver

> 和客户端交互并且负责任务调度

##### Compiler

> 将sql转化为MapReduce

##### ExecutionEngine

> 和Hadoop对接的端口



#### ETL(Extract ,Transform,load)

> `Extract`
>
> > 将数据从数据源中获取整理
>
> `Transform`
>
> > 针对数据的任何操作
>
> `Load`
>
> > 将数据按照指定的格式导入或导出

##### ETL流程

> Flume收集日志
>
> > 不属于ETL
>
> 建立外部表去管理原始数据
>
> > E
>
> 从原始数据中抽取有用的字段	--- 清洗表
>
> > T
>
> 将字段进行整理	--- 事实表
>
> > T
>
> 根据不同的要求从整理好的表中再抽取字段
>
> > T
>
> 将HDFS上的数据导出到数据库中
>
> > L
>
> 将数据进行展示
>
> > 不属于ETL





### 案例	----网站流量分析

> 详见课前资料	--》网站流量分析
>
> 数据展示代码为：课前资料 -》Zebra

