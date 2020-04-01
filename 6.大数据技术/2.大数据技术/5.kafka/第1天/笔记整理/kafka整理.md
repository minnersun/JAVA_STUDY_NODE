## kafka整理

-----

### 消息队列

#### 消息队列的模式

> Peer to Peer	
>
> > 例如RabbitMQ等。数据只能被消费一次
>
> Publish-subscribe
>
> > 例如kafuka等。数据可以被消费多次

#### 消息队列的作用

> 销峰限流
>
> 可以进行持久化	-保证消息在消费前不丢失
>
> 解耦	-实现生产者与消费者之间的解耦



### 简述

> 具有**消息持久化，高吞吐，实时，分布式**等特性
>
> 适用于**离线和实时**的消息消费
>
> > 例如
> >
> > > 网站活动跟踪
> > >
> > > 聚合统计系统运营数据（监控数据）
> > >
> > > 日志收集
> > >
> > > 等大量数据的收集场景



### 概念

#### broker：经纪人

> kafka集群中，给**节点起了代理名称**
>
> 每一个kafka**节点**都是一个broker
>
> kafka对broker进行编号，**编号**要不同

#### topic：主题

> **每一条消息都要进行分类**，分类时使用topic来指定
>
> 一个**topic至少包含一个分区**
>
> 默认情况下，主题在**删除**的时候不是立即删除，而是被**标记为删除状态**	
>
> > 一分钟左右移除

#### partition：分区

> 每一个分区对应一个目录
>
> > 例如
> >
> > > `log-0	-log主题0号分区`
> > >
> > > `log-1	-log主题1号分区`
> > >
> > > `log-2	-log主题2号分区`
>
> 如果有多个kafka节点，分区会**平均分布在节点上**
>
> 分区让kafka可以做到**分布式存储**

#### replication：副本

> 在kafka中，为了**防止数据丢失**，采用了副本策略
>
> 如果指定了多个副本，则每一个分区都会进行备份	-副本**以分区为单位**进行备份
>
> 副本数量**不能超过节点数量**

#### productor：生产者

> 向kafka队列中**添加**数据

#### consumer：消费者

> 从kafka队列中**获取** 数据

#### leader和follower 

> 在kafka中，当出现**多副本**的时候，副本之间会进行选举，选举出**leader副本**以及剩余的**follower副本**
>
> leader副本所在的节点不一定是leader节点-在kafka中，**所有的节点都是平级的**，没有leader和follower的说法
>
> **生产者和消费者只会和leader副本进行交互** 

#### Controller 

> 当构建好kafka集群的时候，会所及在某个节点上启动controller来**负责副本的选举**
>
> 副本的选举遵循**ISR策略**
>
> > 当**leader**和**follower**进行信息交互的时候，**follower**会给**leader**一个返回信号，此时**Controller**会记录返回顺序
>
> 当leader副本宕机的时候，controller首先从ISR的最头部选择副本成为leader；如果ISR队列为空，则认为所有的副本全部宕机，则先复活的副本就会成为leader

#### Consumer group

> 将一个或者多个消费者放在同一个组中
>
> kafka中的消息在消费者**组之间是共享的**
>
> 但是在**组内是竞争**	
>
> >  组间共享，组内竞争



### kafka配置（基本）

> 详见课前资料	->简介->参数配置

> `vim config server.properties`

> `broker.id`
>
> > 每一个broker拥有唯一的id
>
> `log.dirs`
>
> > 存放数据的路径，可以是多个，用`;`隔开
>
> `zookeeper.connect`
>
> > 连接zookeeper服务器
>
> `log.retention.hours=168`
>
> > 日志删除时间间隔
>
> `log.segment.bytes=1073741824`
>
> > 日志达到一定大小会产生一个新的文件
>
> `log.cleaner.enable=false`
>
> > 设置是否启用日志清理



### 基本指令

> `sh kafka-topics.sh --create --zookeeper hadoop01:2181 --replication-factor 1 --partitions 1 --topic log`
>
> > 连接`zookeeper`,创建一个叫`log`的**主题**，副本数量为`1`，分区为`1`
>
> `sh kafka-topics.sh --list --zookeeper hbase01:2181`
>
> > **查看**所有的Topic
>
> `sh kafka-console-producer.sh --broker-list hadoop01:9092 --topic log`
>
> > **启动生产者**，kafka对外**暴露**的端口是**9092**，**连接log主题**
>
> ` sh kafka-console-consumer.sh --zookeeper hbase01:2181 --topic log
> --from-beginning`
>
> > **启动消费者**，连接zookeeper，确定数据在哪个节点上
> >
> > topic确认从哪个主题获取数据
>
> `sh kafka-topics.sh --delete --zookeeper hbase01:2181 --topic  log`
>
> > **删除主题**
>
> `sh kafka-topics.sh --describe --zookeeper hadoop01:2181 --topic log`
>
> > **描述log主题**	包含**分区，副本**等信息

### kafka的机制

#### kafka offset机制

> offset偏移量
>
> > 记录在kafka服务器
>
> > 记录消费者从broker中**读取的位置**
> >
> > 下一次从此位置进行读取
> >
> > 保证数据不会被**重复消费**



#### kafka的索引机制

> kafka创建的是稀疏索引
>
> > 默认大小10M，如果超过，则会在**分区目录中**产生新的**索引文件**和**分区文件**
>
> **一个索引文件+一个分区文件 = 一个segment**
>
> > 例如
> >
> > > **00000000.index+0000000.log = segment**



### kafka语义

> 在代码中设置

#### 至少一次语义

> 生产者向broker发送消息
>
> > 如果生产者**收到了broker的确认**，则意味着消息已经**成功发送**
> >
> > 如果消费端收到**没有确认**，则会**重新发送**
> >
> > > 可能会导致数据的重复

#### 至多一次语义

> 生产者向broker**只发送一次数据**
>
> > 可能会产生数据的丢失

#### 精确语义

> 基于**至少一次+幂等性**实现
>
> **幂等性**
>
> > 操作一次和操作多次的结果是一样的
>
> > 实现原理
> >
> > > 给提交的数据进行**唯一标记**
> > >
> > > 例如web表单提交的**sessionid**



### CAP

> MS
>
> > 主从结构
>
> PAXOS
>
> > Zookeeper
>
> WNR
>
> > W+R>N

