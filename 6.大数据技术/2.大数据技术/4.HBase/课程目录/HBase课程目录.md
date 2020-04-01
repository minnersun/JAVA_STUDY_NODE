## 课程目录

------

### 第一天

#### HBase概述

> HBase是一套**分布式的，可扩展的，非关系型的数据库**
>
> HBase能够存储**`billions of rows X millions of columns`**
>
> HBase采取了**列级别**的方式来存储
>
> > 存储的时候以**键值对**的形式来存储
>
> HBase适合于存储**半结构化，稀疏**的数据
>
> HBase基于HDFS来存储	---HDFS不允许修改
>
> > HBase提供了**‘改’**的能力	--时间戳
>
> HBase锁定一条数据	-需要时间戳
>
> > **行键+列名+版本**，称之为是一个**cell**

#### HBase与Mysql的对比

> Mysql	-行级别
>
> HBase	-列级别
>
> > 写
> >
> > > 行级别的效率更高，行级别的**磁盘**空间是**连续的**
> > >
> > > 列级别需要花费大量的时间进行**寻址**
> >
> > 读
> >
> > > 如果进行**全表**查询，则**行级别**的效率更高
> > >
> > > 如果进行**字段**查询，则**列级别**的效率更高

#### 数据的三种结构

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

#### HBase的基本概念

> namespace
>
> > 类似于关系型数据库中的**database**
> >
> > 每一张表都属于一个**namespace**
> >
> > 如果没有指定**namespace**，那么默认在**default**中
>
> rowkey	-行键
>
> > 类似于传统数据库中的**主键**
> >
> > 行键不属于任何的列族
> >
> > **建表的时候不需要指定行键**
>
> Column family
>
> > 类似于传统关系型数据库中的**单个表**
> >
> > 在HBase中，一个表**至少要包含一个列族**
> >
> > > 开发中的列族**一般不超过3个**
> >
> > **表建好后，列族的个数就不能再改动**
> >
> > > 列族中的**列可以包含0到多个**
> >
> > **列族中的列可以增删**

#### HBase命令

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
> > 删除命名空间`hbasedemo`







### 第二天

#### HBase的API操作

> 详见老师笔记或笔记整理

#### 字典序，自然序

> 自然序
>
> > 升序排序
>
> 字典序
>
> > 相同的放一块
> >
> > 先比较第一位，在比较第二位

#### Zookeeper的作用

> `关于对HBase，HMaster，HRegionServer的作用`

> **HBase**启动的时候，会在**Zookeeper**上注册一个**/hbase**节点
>
> **HMaster**在启动之后，会在**Zookeeper**下注册
>
> > **active HMaster**会注册到**/hbase/master**节点下
>
> > **backup HMaster**会注册到**/hbase/backup-master**节点下
> >
> > > 如果**Zookeeper**监控到**master**节点消失，就会从**backup-HMaster**中选取一个节点切换状态
>
> **HRegionServer**会在**Zookeeper**的**/hbase/rs**节点下注册一个**临时节点**
>
> > **Zookeeper**监控**HRegionServer**的存活情况
> >
> > **HRegionServer**会向**Zookeeper**发送**心跳**
> >
> > **Zookeeper**如果没有收到心跳，这个节点会**消失**

#### HMaster

> 课前资料，老师笔记中有图解

##### HMaster的作用

> `对于HRegionServer，HBase的作用`

> 管理**HRegionServer**
>
> 管理表结构（DDL）
>
> > 如果是对表中数据进行操作(DML),不经过HMaster
>
> 管理维护**HBase**的元数据
>
> > **namespace，table，column family等**
>
> > HBase的**元数据**
> >
> > > **HBase0.94**之前
> > >
> > > > 元数据存储在Zookeeper中的**-ROOT-**目录下
> > > >
> > > > > 详细过程	略
> > >
> > > **HBase0.94之后**
> > >
> > > > **.meta.**文件不再允许被切分
> > > >
> > > > 客户端**自动缓存.meta.**的位置
> > > >
> > > > 客户端**自动缓存HRegion**的位置
> > > >
> > > > > 随着HBase集群运行时间的延长，客户缓存的位置会越来愈多，读写效率会越来越高
> > > >
> > > > > 但如果这个过程中**HRegion转移或者客户端宕机，那么缓存失效，则会重新建立**
> > >
> > > > 详细过程	略

##### HMaster在Zookeeper的流程

> **HMaster**在启动后会在**Zookeeper**上注册临时节点
>
> > active HBase		- hbase/master
> >
> > backup HBase		-/hbase/backup-master
>
> `sh hbase-daemon.sh start master`
>
> > 可以通过指令单独启动**HMaster**
>
> HMaster的状态**由启动顺序决定**
>
> > 先启动的节点成为**active**的状态
> >
> > 后启动的节点自动成为**backup**的状态
>
> 当**backup HMaster**启动的时候，**active HMaster**会检测到这个节点
>
> > **active HMaster**会监控**Zookeeper**上**/hbase/backup-master**节点的变价
>
> **active HMaster**和**backup HMaster**之间要进行热备
>
> > 所以实际过程中，一般**HMaster**的个数不超过3个

#### HRegionServer

> 负责管理**HRgion**，每一个**HRegion**最多能管理1000个**HRegion**
>
> HRegionServer包含了
>
> > WAL
> >
> > BlockCache
> >
> > 0或多个HRegion
>
> WAL	- Write Ahead Log -HLog
>
> > 用于记录写操作
> >
> > HRegion接收到写操作的时候，先将操作记录到**WAL**中，记录成功后，再写到**memStore**中
>
> BlockCache	- 读缓存	-默认为128M
>
> > 为了提高度效率
>
> > BlockCache在缓存的时候，采用了**局部性**原理
> >
> > > 时间局部性：
> > >
> > > > 如果一条数据被读取过，那么认为则条数据再次读取的概率会高于没有读取过的数据
> > > >
> > > > > 读取过的数据就会被缓存
> >
> > > 空间局限性
> > >
> > > > 如果一条数据被读取过，那么认为这条数据相邻的数据被读取的概率会高于其他数据
> > > >
> > > > > 这条数据相邻的数据也会被缓存
>
> > 采用局部性的目的实际上是为了提高命中率
> >
> > 采用LRU策略
> >
> > > 删除最长时间不用的数据

#### HRegion

> `HRegion的切分，HStore，memStore，HFile`

> **HRegion**是分布式额最小单位，但并不是存储的最小范围
>
> > **HRegion**是存储在**DataNode**上，由**HRegionServer**来管理
> >
> > 所以在实际开发中，将**HRegionService**和**DataNode**部署在相同的节点上

##### HRegion的切分

> 从行键上，将表进行切分，会切出一个或者多个**HRegion**

> 每一个**HRegion**都会放在一个**HRegionServer**上
>
> 行键是**排序**的，所以**同一张表之间的数据是有序的**
>
> > 字典序
>
> HRegion达到一定大小（**默认10G**）的时候，平均分裂为2个**HRegion**
>
> > 其中的一个**HRegion**会转移到其他节点上
>
> **切分**HRegion的意义
>
> > 能够给提高HBase**的吞吐量**
> >
> > 能够对数据进行**分布式的存储**

##### HStore		-memStore，HFile

> 每一个**HRegion**包含一个到多个**HStore**，**HStore**的数量由列族决定
>
> > 每一个列族对应一个**HStore**
>
> 每一个**HStore**中包含一个**memStore(写缓存)**以及0到多个**StoreFile/Hfile**



#### 写流程

> > 客户端发起请求到**Zookeeper**，获取到**.meta.**文件的位置
> >
> > > 客户端会缓存这个文件的位置
> >
> > 读取**.meta.**文件，发送请求给要操作的**HRegion**所在的**HRegionServer**
> >
> > **HRegionServer**接收到请求之后，会将请求记录到**WAL**中。在HBase0.96开始，允许并写(NIO管道机制)
> >
> > **WAL**记录成功之后，会将数据写道**memStore**中
> >
> > > 数据在**memStore**中会进行排序
> > >
> > > > 行键-》列族名-》列明-》时间戳
> > > >
> > > > > 行键，列族名，列	按字典序
> > > > >
> > > > > 时间戳为倒序
> >
> > **memStore**达到一定条件之后会冲刷到**StoreFile/HFile**中
> >
> > > **memStore**达到128M
> > >
> > > 一个**HRegionServer**上所有**memStore**占用的内存达到**物理内存的35%**
> > >
> > > 默认情况下，**WAL达到1G**
> >
> > 单个**HFile**中的数据是有序的，但是所有的HFile之间是局部有序的
>
> > **HFile**的格式	V1版本
> >
> > > **DataBlock：**存储数据
> > >
> > > > Magic是一个魔数
> > > >
> > > > > 本质上就是一个随机数，用于校验这个Block是否被更改
> > > >
> > > > 每一个**KeyValue**对应一条数据
> > > >
> > > > 每一个**DataBlock**都保持了起始行键
> > >
> > > 每一个**KeyValue**对应一条数据
> > >
> > > 每一个**DataBlock**都保持了起始行键和结束行键
> > >
> > > 一个**DataBlock**中有多个**KeyValue**
> > >
> > > **DataBlock**默认是64KB大小，调小**DataBlock**适合于字段查询，调大**DataBlock**适合于遍历
> > >
> > > **DataBlock**是**HBase**中数据存储的基本单元
> > >
> > > > 空间局部性在进行缓存的时候就是以**DataBlock**为单位缓存
> > > >
> > > > > 如果这个**DataBlock**中的某一条数据被读取，那么整个**DataBlock**就会被缓存
> > >
> > > **MetaBlock**：元数据信息
> > >
> > > **FileInfo**：对HFile的描述信息
> > >
> > > **DataIndex**：记录了每一个DataBlock的起始和结束字节位置
> > >
> > > **MetaIndex**：记录了每一个MetaBlock的起始和结束字节位置
> > >
> > > **Trailer**：固定的4个字节，记录了**DataIndex**以及**MetaIndex**的起始字节
>
> > 在**HFile**的高版本中，引入了**BloomFiler**	V2版本
> >
> > > 布隆过滤器
> > >
> > > > 由于Cell是升序排列
> > > >
> > > > 默认情况下，对行键采用**3个不同的哈希函数**计算哈希码进行映射
> > >
> > > 随着数据的增多，映射位置越来越多，导致误判率会越来越高，解决方案是增加数组的容量
> >
> > > 如果映射到0，说明这条数据数据一定不存在，但是如果映射到了1，只能说明这条数据可能存在
> > >
> > > > 布隆过滤器只能确定数据的不存在，但是不能确定该数据的存在







### 第三天

#### 读流程

> 数据可能存储在**BlockCache**，**memStore**以及**HFile**
>
> 读取数据的时候先从**BlockCache**中读取
>
> 如果**BlockCache**中没有，再视图从**memStore**中读取
>
> 如果**memStore**中没有，则从**HFile**读取数据，根据行键的范围以及布隆过滤器来筛选，筛选掉没有指定的**HFile**，剩余的**HFile**中不代表有这个数据

#### 合并机制

> minor compaction
>
> > 将相邻的几个小的HFile合并成一个大的HFile
>
> major compaction
>
> > 将这个HStore中所有的HFile合并成一个
>
> 在默认情况下，HBase使用的是minor compaction

> 在合并过程中，数据会进行整体的排序，会舍弃掉一部分数据
>
> > 被删除的数据，超过版本的数据