## HBase

------

### 概述

> HBase是一套**分布式的，可扩展的，非关系型数据库**
>
> HBase能存储**`billions of rows X millions of columns`**
>
> HBase是Doug仿照Google的**Big Table**来设计实现的
>
> HBase中，数据采取了**列级别**的方式来存储
>
> > 并且存储的时候是以**键值对**的形式来存储
>
> HBase适合于存储**半结构化**，**稀疏**的数据
>
> HBase中，要**删除**一个表，需要**先禁用**这个表
>
> HBase提供了完整的**增删改查**的能力
>
> HBase是基于HDFS来存储数据的，HDFS不允许修改数据,但是**HBase提供了'改'的能力**
>
> > 实际是在文件末尾追加，HBase在查询的时候，如果不指定，默认找的是**最新版本的数据**	--**时间戳**
>
> 如果只通过行键+列明，并不能锁定**唯一**的一条数据，需要添加时间戳
>
> > 通过**行键+列名+版本**，锁定唯一的一条数据，称之为是一个**cell**

#### Hbase与Mysql的对比

> MySQL	- 行级别
>
> HBase	-列级别
>
> > 写
> >
> > > 如果**添数据**行级别的效率更高
> > >
> > > 因为行级别对**磁盘**的利用是**连续的**
> > >
> > > > 如果向**列级别**中添加则需要花费大量的**寻址时间**
> >
> > 读
> >
> > > 如果进行**全表**查询，则**行级别**的效率更高
> > >
> > > 如果进行**字段**查询，则**列级别**的效率更高

#### HBase的安装方式

> 单机
>
> > HBase不是存储再Hadoo中，而是存储在磁盘上
>
> 伪分布式
>
> 完全分布式

#### 结构化数据，半结构化数据，非结构化数据

> 结构化数据
>
> > 使用关系型数据库表示和存储
> >
> > 每一行数据的属性都是相同的
>
> 半结构化数据
>
> > 可以使用HBase进行存储
> >
> > 具有一定的结构性
> >
> > > XML，HTML
>
> 非结构化数据
>
> > 没有固定的结构



### 基本概念

#### rowkey	-行键

> 行键类似于传统关系型数据库中的**主键**

> 不同于主键之处在于，行键不属于任何列族

> **建表的时候不需要指定行键**，在添加数据的时候才指定行键

#### column fammily

> 类似于传统关系型数据库中的**单个表**
>
> 在HBase中，**一个表中至少要包含一个列族**，实际开发中，一个表中的列组数量**一般不超过三个**
>
> 列族在定义表的时候就需要指定，**表建好后，列族的个数就不能改动了**
>
> > 每一个列族可以包含0到多个列
>
> HBase不强调列，**列可以动态的增删**
>
> >  每一个列必须属于一个列族

#### namespace

> 类似于关系型数据库中的**database**
>
> 在HBase中，每一个表都属于一个**namespace**
>
> 如果一个表没有指定namespace，那么在**default**中

### 命令

> `get 'person','p1'`
>
> > 获取person表中行键为p1的值
>
> `get 'person','p1','basic'`
>
> > 获取person表中，行键为p1，列族为basic的值
>
> `get 'person','p1','basic:name'`
>
> > 获取person表中，行键为p1，列为basic：name的值
>
> `create 'person','basic','info'`
>
> > 创建person表，包含basic和info列族
>
> `put 'person','p1','basic:age',15`
>
> > 向person表中添加数据，行键为p1，列为basic:age，值为15
>
> `scan 'person'`
>
> > 获取person整表
>
> `scan 'person',{COLUMNS=>['info']}`
>
> > 获取person表中info列族的值
>
> `scan 'person',{COLUMN=>['basic:name']}`
>
> > 获取person表中，列basic:name的值
>
> `delete 'person','p2','info:addr'`
>
> > 删除person表中，行键为p2，列为info:add的值
>
> `deleteall 'person','p1'`
>
> > 删除person表中，行键为p1的数据
>
> `disable 'person'`
>
> > 禁用表
>
> `delete 'person'`
>
> > 删除表
>
> `create 'person',{NAME=>'basic'，VERSIONS=>3},{NAME=>'info'，VERSIONS=>4}`
>
> > 创建person表，包含两个列族：basic以及info
> >
> > 其中basic保留3个版本的数据
> >
> > info保留4个版本的数据
>
> `get 'person','p1',{COLUMN=>'basic:name',VERSIONS=>4}`
>
> > 获取person表中，basic:name列的第4个版本的数据
>
> `create 'hbasedemo：orders'，'basic'`
>
> > 在hbasedemo名称空间下，创建一个orders表
>
> `list_namespace_table 'hbasedemo'`
>
> > 列出hbasedemo名称空间下所有的表
>
> `drop_namespace 'hbasedemo'`
>
> > 删除命名空间hbasedemo

