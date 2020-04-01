## Hive

--------

###HIVE的优化

#### 面试题

#### 如何提高数据库（HIVE）查询效率

> 使用子查询，减少数据量，提高任务数
>
> 建立索引
>
> 创建视图
>
> > 可以减少查询的字段





#### map side join

> 连接两个表
>
> >  一个比较小的表和一个特别大的表的时候
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



#### join语句优化

> `select m.cid,u.id from order m join customer u on m.cid=u.id where m.dt='20160801'`
>
> > 优化前
>
> `select m.cid,u.id from (select cid from order where dt=’20160801’)m join customer u on m.cid = u.id`



#### group by 优化

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





#### count distinct优化

> 如果出现了**聚合函数**,那么利用**子查询**进行去重，或者其他操作，最后再进行聚合

> 优化前
>
> > `select count(distinct id) from tablename`
>
> 优化后
>
> > `select count(*) from (select distinct id from tablename) tmp`





#### 调整切片数量

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





#### JVM重用

> `set mapred.job.reuse.jvm.num.tasks=20(默认是1个）`
>
> > JVM重用适用于**小文件比较多**
> >
> > 或者**task任务**特别多的场景
>
> JVM重用可以使得一个JVM进程在同一个JOB中重新使用N次后才会销毁





#### 启用严格模式

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



#### 关闭推测执行机制

> 不适用于数据倾斜
>
> 使用于任务分配不均（极少出现0）

> `set mapreduce.map.speculative=false`
>
> `set mapreduce.reduce.speculative=false`
>
> `set hive.mapred.reduce.tasks.speculative.execution=false`





### Sqoop

> 是Apache 提供的工具，用于**hdfs**和**关系型数据库**之间数据的导入和导入
>
> > 可以从hdfs导出数据到关系型数据库，也可以从关系型数据库导入数据到hdfs

> 基础指令见课前资料





### Hive执行流程

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

