## Hive

---

### 概述

> Hive是基于Hadoop的**数据仓库**工具
>
> Hive提供了类Sql语句来对分布式系统的数据进行数据**提取、转化、加载**（ETL	Extract-Transform-Load） 
>
> Hive将SQL语句在底层转化为**MapReduce**程序来操作数据
>
> 本质上是一种**大数据离线分析**工具

#### 数据库和数据仓库的区别

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

####  细节

> Hive中每建立一个**库**，都会在**HDFS**对应的创建一个**目录**
>
> Hive中每一张**表**在**HDFS**上都对应一个**目录**
>
> Hive中**没有主键**的概念
>
> 当利用Hive**管理本地数据**的时候，会将要管理的数据上传到**HDFS**上
>
> 当一张表已经创建好后，这个表中的**字段间隔符**就不能更改
>
> 从Hive查询数据向本地目录中写的时候只能覆盖

### 基础命令

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

#### 基础命令2

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



### 表结构

#### 内部表和外部表

> 内部表
>
> > **在Hive中创建的**放在`/user/hive/warehouse`目录下的表称之为内部表，需要`手动创建手动添加`数据
>
> 外部表
>
> > 如果需要管理HDFS中已存在的数据，那么建立外部表
> >
> > 外部表只能指定到目录，不能指定具体文件
> >
> > > 管理目录下的所有文件
>
> > 外部表创建命令
> >
> > > `create external table stu (id int,name string) row format delimited fields terminated by ' ' location '/目录路径'`

##### 内部表和外部表的区别

> 对于内部表，在**删除该表**的时候，HDFS对应的目录节点**会被**删除
>
> 对于外部表，在**删除该表**的时候，HDFS对应的目录节点**不会**删除



#### 分区表	-partitioned 

> 分区表可以通过添加指定字段来提高Hive的查询效率
>
> 数据量较大的情况下，往往会添加分区表来避免全表查询

##### 分区表指令

> `create table book(id int,name string) partitioned by (category string) row format delimated fields terminated by '\t'`
>
> > 创建book表，以category作为分区
> >
> > category字段可以不在字段列表中，生成的表中自动就会具有该字段
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

##### 动态分区

> 如果在进行查询的时候一个表中的数据是未分区的一个表中的数据是已经分区的
>
> 此时**从未分区的表**中**查询**数据向**已分区表**中**添加**是不行的
>
> 所以此时需要开启动态分区。

###### 步骤

> `set hive.exec.dynamic.partition = true;`
>
> `set hive.exec.dynamic.partition.mode = nostrict;`
>
> 加载数据
>
> > `insert into table stu2 partition(class) select id, name, class from stu distribute by class;`
> >
> > > 查询的字段按照表字段的顺序，分区放于最后



##### 分桶

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
