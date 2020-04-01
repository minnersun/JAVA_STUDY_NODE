## HDFS

---

### NameNode

> NameNode**重启**的时候
>
> > 先将**edits**中的操作转化为结果滚动到**fsimage**中
> >
> > 启动后读取**fsimage**，将元数据加载到**内存**中，等待**DataNode心跳**
> >
> > > 如果**NameNode**没有收到**DataNode**的心跳，则**NameNode**会认为这一部分**DataNode**丢失且重新备份这些数据、
> > >
> > > 如果接收到所有的**DataNode**的心跳，则**NameNode**就会==校验==**DataNode**上的数据是否准确合法
> > >
> > > > 如果==校验==失败，则试图恢复这个数据块并且中心校验
> > > >
> > > > 如果一切校验成功，**NameNode**才会开始对外提供服务  ----> 安全模式
> > > >
> > > > ==安全模式==
> > > >
> > > > > 在安全模式中，NameNode会进行校验，如果一切校验都成功了，NameNode会自动的退出安全模式
>
> 如果在合理时间内，NameNode没有退出==安全模式==，则这个时候就意味着数据产生丢失或不可恢复
>
> > 这个时候应该强制退出==安全模式==
> >
> > > `hadoop dfsadmin -safemode `
>
> 也正是因为==安全模式==的存在，所以要在Hadoop的伪分布式中，**副本的数量**必须置为1
>
> 在**Hadoop2.X**的完全分布式中，可以最多设置两个**NameNode**
>
> 在HDFS中，这个集群的负载量的多少基本上由**NameNode**的性能决定

### 副本放置策略

> 第一个副本
>
> > 集群**内部上传**，谁上传，第一个副本就放在谁身上
> >
> > 集群外部上传，**NameNode**会从**DataNode**中选取一个相对空闲的节点来存储数据
>
> 第二个副本
>
> > Hadoop1.7之后，第二个副本放在和第一个副本**相同**的机架上
>
> 第三个副本
>
> > Hadoop1.7之后，第三个副本放在和第二个副本**不同**的机架上
>
> 更多副本
>
> > **NameNode**会选取相对空闲的节点来放置

### 机架感知策略

> 所谓的机架指的并不是物理机架而是**逻辑机架**
>
> DataNode会每隔**3s**向NameNode发送心跳
>
> DataNode会存在**四种状态**
>
> > **预服役**
> >
> > > 准备向集群中动态添加的节点
> >
> > **服役**
> >
> > > 集群中正在工作的节点
> >
> > **预退役**
> >
> > > 准备从集群中抽离的节点
> >
> > **退役**
> >
> > > 节点抽离集群

### SecondaryNameNode

> SecondaryNameNode**辅助**NameNode进行**edits**和**fsimage**文件的滚动
>
> 在SecondaryNameNode**存在**的情况下，edits的滚动和写入fsimage的操作是发生在SecondaryNameNode上的

> SecondaryNameNode能起到一定的**备份**作用，但是不作为NameNode**的热备份**

> 在现在的开发环境中，在Hadoop集群中往往会舍弃SecondaryNameNode而是采取==双NameNode==机制来保证NameNode节点的==高可用==
>
> NameNode节点处在**核心节点**的位置，一旦NameNode宕机，则HDFS整个集群会不能服务，所以要考虑NameNode节点的==高可用==

### 回收站机制

> 在HDFS中，回收站**默认**是不开启的
>
> 意味着如果删除文件则**立马生效**
>
> 在core-site.xml中修改fs.trash.interval属性来指定删除时间（min）





### dfs目录

> dfs目录是在NameNode被**格式化**的时候出现的
>
> 存在三个**子目录**
>
> > data、name、namesecondary
>
> **in_use.lock**
>
> > 表示当前节点**已经启动**对应的进程
> >
> > 防止多次启动
>
> 在hdfs中每一次的写操作都会分配一个全局递增的事务id - **txid**
>
> 在HDFS第一次启动的时候，1min之后**edits**会自动进行一次滚动，之后就会按照**fs.checkpoint.period**来进行滚动
>
> edits文件一开始一定是**OP_START_LOG_SRGMENT**结束一定是**OP_END_LOG_SRGMENT**，日志开始和结束都会分配一个事务id

> HDFS上的文件一旦上传完毕，不允许修改
>
> edits文件本身是一个**字节文件**，不能直接查看，需要利用命令转化为指定格式后才能查看
>
> 每一个**fsimage_XXXX**，都会对应一个**fsimage_XXX.md5**
>
> > **MD5**文件是使用md5算法对**fsimage**文件进行**校验**的

### VERSION

> #### clusterID	-	标识DataNode是否归NameNode管理
>
> > 在HDFS中，当**NameNode被格式化**的时候，会自动产生一个**clusterID**
>
> 在集群启动的时候
>
> > NameNode就会将**clusterID**分发给每一个**DataNode**，DataNode只在开始的时候接收**clusterID**
>
> NameNode在收到DataNode的信息之后
>
> > 会先校验**clusterID**是否一致。如果一致，则接收信息；
> >
> > 如果不一致，则认为当前DataNode不归这个NameNode管理。
>
> 所以如果多次格式化NameNode
>
> >  会导致集群中NameNode和DataNode的**clusterID**不一致，此时需要统一**clusterID**才能保证整个集群的正常运行

> #### storageType：节点类型
>
> > NameNode
> >
> > DataNode

> #### blockpoolID：块池编号
>
> > #### 联邦HDFS(Federation HDFS)
> >
> > > 多个节点合作起到一个NameNode的作用，这种设置方式称之为**Federation HDFS**
> >
> > 优势
> >
> > > 提高并发量
> >
> > 劣势
> >
> > > 不能动态增加路径
> > >
> > > 加大了对网络资源的消耗
>
> > ##### 处在一个Federation HDFS的NameNode们，他们的blockpoolID必须一致

> 并发
>
> > 线程数量
>
> 吞吐
>
> > 读写速度
>
> 高并发不一定是高吞吐，高吞吐一般都是高并发

### 流程（读，写，删）

##### 读流程(下载)

> 客户端发起**RPC请求**到NameNode
>
> NameNode在收到请求之后，查询元数据，判断这个请求对应的路径是否存在
>
> 如果文件存在，NameNode会将第一个Block（默认情况下，一个Block对应3个地址）对应的存储地址放入一个队列中返回给客户端
>
> 客户端收到队列之后，从队列中取出这个Block对应的地址，从这些地址中选取一个**较近（中间经过的转发次数越少则认为距离越近）**的地址对应的DataNode来读取数据
>
> 客户端在读取完当前的Block之后，会当前的Block进行一次checksum的验证，保证数据的完整性；如果校验失败，则客户端会给NameNode报告错误信息，同时重新选取地址重新读取；如果校验成功，则客户端会给NameNode发信息要下一个Block的地址
>
> 当客户端将所有的Block读取完成之后，客户端会再给NameNode发送信息通知NameNode关闭文件（实际上就是关流）
>
> ###### HDFS的读写流程之所以是让客户端直接连接DataNode，目的是为了提高吞吐量

##### 写流程

> 客户端发起**RPC请求**到NameNode
>
> NameNode收到请求之后，进行**校验**
>
> > 校验写入路径是否有权限操作
> >
> > 检查指定路径下是否有同名文件
>
> 如果校验成功，NameNode会给客户端返回一个成功信号
>
> 客户端在收到信号之后，会和NameNode要第一个Block的地址
>
> NameNode在收到信号之后，会选取地址放入队列中返回给客户端
>
> 客户端收到地址之后，从中选取一个较近的DataNode来写入第一个副本，在写完之后，这个DataNode会通过pipeline(管道，本质上就是一个NIO中的Channel)将第二个副本写到其他的DataNode上。写完之后，从最后一个节点开始，会依次给前一个节点返回一个ack表示写入成功，直到最后返回给客户端
>
> 客户端在收到ack之后，会向NameNode要下一个Block的地址
>
> #### 直到所有的Block全部都写完，客户端就会给NameNode发送信息要求关闭文件（实际上就是关流），此时文件就不能改动

##### 删流程

> 客户端发起**RPC请求**到NameNode
>
> NameNode在收到请求之后，查询元数据，**校验**当前客户端是否有删除权限
>
> > 如果没有权限则直接报错；
> >
> > 如果有这个权限，则记录edits，然后修改内存中元数据，会给客户端返回成功信号ack。
> >
> > > 注意，此时只是修改了元数据，该文件对应的Block实际上依然存储在DataNode上
>
> NameNode会等待DataNode的心跳，NameNode在收到心跳之后，会检查这个DataNode上的Block信息
>
> 如果发现Block信息和元数据不一致，NameNode就会响应心跳，返回指令，要求DataNode删除对应的Block，当DataNode收到指令之后，才会删除这个文件块，文件才真正从HDFS上移除