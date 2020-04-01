## Flume目录

---

### 第一天

#### 概述

> Flume是一套分布式的，可靠的，用于进行**日志的收集，汇集和传输**的系统

##### 版本

> Flume0.X      - Flume-og
>
> Flume1.X - Flume-ng，Flume-ng不兼容Flume-og

##### 基本概念

> ##### Event
>
> > 将手机的每一条日志封装成一个**Event**对象
> >
> > **Event**对象本质上是一个**json串**
> >
> > > 包含**headers**以及**body**
> > >
> > > **收集的日志放在body中**
>
> ##### Agent
>
> > **Flume**中，**Flume**的结构是**Agent**形式来组件的
>
> > **Agent**的组成
> >
> > > Source
> > >
> > > > 从数据源采集数据
> > >
> > > Channel
> > >
> > > > 缓存数据
> > >
> > > Sink
> > >
> > > > 将数据写往目的地
> >
> > 在**Flume**中，一个**Source**可以绑定**多个Channel**，但是一个**Sink**只能绑定一个**Channel**
>
> ##### Source
>
> > AVRO
> >
> > > 可以实现
> > >
> > > > 多级流动
> > > >
> > > > 扇入流动
> > > >
> > > > 扇出流动
> >
> > Spooling Directory
> >
> > > 监听**指定**的目录
> > >
> > > **被收集过的文件再有改动，将不再收集**
> >
> > HTTP
> >
> > > 监听HTTP请求
> > >
> > > 一般监听POST请求，GET请求不稳定，其他请求方式不支持
> >
> > 自定义Source
> >
> > > `public class AuthSource extends AbstractSource implements EventDrivenSource, Configurable `
>
> ##### Channel
>
> > Memory
> >
> > > **内存**通道
> > >
> > > 如果**Memory Channel**满了，后续的数据会被阻塞
> > >
> > > > **Memory Channel**本质上是一个**BlockingDeque**
>
> > File
> >
> > > 将数据存储在磁盘中
>
> > JDBC Channel
> >
> > > 目前只支持DerBy数据库
> >
> > > 测试阶段
>
> > 内存溢出通道
> >
> > > 优先存储到内存中，存储不下，再溢出到文件中
> >
> > > 测试阶段
>
> ##### Sink
>
> > 一个Sink只能绑定一个Channel
>
> > File_roll
> >
> > > 指定文件的存储路径
> > >
> > > > 如果不指定，每30s产生一个文件
>
> > HDFS
> >
> > > 将数据写道**HDFS**上，在连接**HDFS**的时候，需要指定**NameNode**所在地址
>
> > AVRO
> >
> > > 将数据avro序列化后，写到指定的节点上
> > >
> > > 是实现多级流动，扇出流(1到多)，扇入流(多到1)的基础
>
> > Logger
> >
> > > 常用于调试







### 第二天

> 案例见笔记或者课前资料

#### Flume的其他组件

##### Selector

> 基于**扇出流**完成
>
> 决定数据的分发方式

###### replication（复制模式）

> `selector.type`
>
> > ` replicating`
> >
> > > 默认就是这种模式
> > >
> > > 将source复制分发给多个Channel

###### multiplexing（多路复用模式）

> 根据规则进行数据的分发

> `selector.type`
>
> > `multiplexing`
> >
> > > 表示路由模式
>
> `selector.header`
>
> > 指定要检测头部的名称
>
> `selector.mapping.*`
>
> > 匹配头部中名称内容的规则
>
> `selector.default`
>
> > 如果未满足匹配规则，则默认发往指定的通道



##### Interceptor

> 拦截器，用于在运行阶段删除修改Event

###### TimeStamp Interceptor

> 在**头部**插入以**毫秒为单位**的当前处理时间
>
> 头部的名字为**timestamp**，值为当前处理的时间戳

> `type`
>
> > `timestamp`
>
> `preserveExisting`
>
> > `fasle` 如果时间戳以存在则保留

###### Host Interceptor

> 在**头部**插入当前处理Agent的**主机名或ip**
>
> 头部的名字为**host**，或者**IP**

> `type`
>
> > `host`
>
> `preserveExisting`
>
> > false	如果主机名已经存在是否保留
>
> `useIP`
>
> > true
> >
> > > 如果配置为true则用IP
> >
> > fasle
> >
> > > 配置false则用主机名
>
> `hostHeader`
>
> > host
> >
> > > 加入头时使用的名称

###### Static Interceptor

> 允许在头部添加**静态的值**到所有的事件
>
> 目前**不允许一次指定多个头**
>
> 如果需要添加多个静态头，则可以指定多个Static interceptor

> `type`
>
> > static
>
> preserveExisting
>
> > true
>
> key
>
> > 要增加的头部
>
> value
>
> > 要增加的头部

###### Search And Replace Interceptor

> 提供了基于字符串的正则搜索和替换的功能

> `type`
>
> > search_replace
>
> `searchPattern`
>
> > 要搜索和替换的正则表达式
>
> `replaceString`
>
> > 要替换为的字符串
>
> `charset`
>
> > UTF-8        字符集编码，默认utf-8

###### Regex Filtering Interceptor

> 此拦截器通过解析事件体去匹配给定正则表达式来筛选事件
>
> 所提供的正则表达式即可以用来包含或刨除事件

> `type`
>
> > regex_filter
>
> `regex`
>
> > 索要匹配的正则表达式
>
> `excludeEvents`
>
> > false
> >
> > > 包含匹配的事件
> >
> > true
> >
> > > 提出匹配的事件



##### Processor

> 可以通过切换组内Sink来实现**负载均衡**的效果
>
> 后者在一个Sink**故障**之后切换到另一个Sink

###### Defalut Sink Peocess

> 默认的策略
>
> 如果不配置就是用这个策略

> sinks
>
> > 用空格分隔的Sink集合
>
> processor.type
>
> > default

###### Failover Sink Processor

> 维护一个Sink的优先级表
>
> 每一个Sink必须指定一个优先级，且唯一

> `sinks`
>
> > 绑定的sink
>
> `processor.type`
>
> > `failover`
>
> `processor.priority`
>
> > 设置优先级
>
> `processor.maxpenalty`
>
> > 设置故障冷却时间（/毫秒）

###### Load_Balancing Sink Processor

> 实现sink见的**负载均衡**能力
>
> 维护了一个Sink的**索引列表**
>
> 支持**轮询**或**随机**的负载均衡，**默认**为轮询方式
>
> 也可以通过实现`AbstractSinkSelector`接口实现自定义的选择机制

> `processor.sinks`
>
> > 绑定的sink
>
> `processor.type`
>
> > `load_balance`
>
> `processor.selector`
>
> > round_robin（轮询调度） random（随机）