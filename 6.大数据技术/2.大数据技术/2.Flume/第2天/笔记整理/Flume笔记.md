## Flume笔记

------

### Flume的其他组件

#### Selector

> 决定数据的分发方式
>
> 基于扇出流动完成的
>
> replicating（复制模式）和multiplexing（路由模式）
>
> 在默认情况下，使用的是复制模式
>
> 路由模式需要指定分发规则
>
> > 分发规则是根据headers中指定字段的值来进行分发
>
> Selector是Source的字组件，是配置在Source上的

##### 复制模式(replication)

> 即把source复制，然后分发给多个Channel

> `selector.type`
>
> > `replication`
> >
> > > 表示复制模式
> > >
> > > 默认就是这种模式
> >
> > > 在`source`接收到数据之后，复制分发给每一个`avro sink`
>
> `selector.option`
>
> > 标志通道为可选

```
a1.sources = r1
a1.channels = c1 c2 c3
a1.source.r1.selector.type = replicating(这个是默认的)
a1.source.r1.channels = c1 c2 c3
a1.source.r1.selector.optional = c3
```



##### multiplexing多路复用模式

> 在这种模式下，用户可以指定转发的规则。selector根据规则进行数据的分发

> `selector.type`
>
> > `multiplexing`
> >
> > > 表示路由模式
>
> `selector.header`
>
> > 指定要检测的头部的名称
>
> `selector.mapping.*`
>
> > 匹配头部中名称内容的规则
>
> `selector.default`
>
> > 如果未满足匹配规则，则默认发往指定的通道

##### 案例

> 01机利用http source接收数据，根据路由规则，发往02，03机

######  01机配置示例

```
	01机配置示例
	#配置Agent a1 的组件
	a1.sources=r1
	a1.sinks=s1 s2
	a1.channels=c1 c2
	
	#描述/配置a1的source1
	a1.sources.r1.type=http
	a1.sources.r1.port=8888
	a1.sources.r1.selector.type=multiplexing
	a1.sources.r1.selector.header=state
	a1.sources.r1.selector.mapping.cn=c1
	a1.sources.r1.selector.mapping.us=c2
	a1.sources.r1.selector.default=c2
	
	#描述sink
	a1.sinks.s1.type=avro
	a1.sinks.s1.hostname=192.168.234.212
	a1.sinks.s1.port=9999
	
	a1.sinks.s2.type=avro
	a1.sinks.s2.hostname=192.168.234.213
	a1.sinks.s2.port=9999
	#描述内存channel
	a1.channels.c1.type=memory
	a1.channels.c1.capacity=1000
	a1.channels.c1.transactionCapacity=100
	
	a1.channels.c2.type=memory
	a1.channels.c2.capacity=1000
	a1.channels.c2.transactionCapacity=100
	
	#为channel 绑定 source和sink
	a1.sources.r1.channels=c1 c2
	a1.sinks.s1.channel=c1
	a1.sinks.s2.channel=c

```

###### 02,03配置示例

```
	02,03配置示例：
	#配置Agent a1 的组件
	a1.sources=r1
	a1.sinks=s1
	a1.channels=c1
	
	#描述/配置a1的source1
	a1.sources.r1.type=avro
	a1.sources.r1.bind=0.0.0.0
	a1.sources.r1.port=9999
	
	#描述sink
	a1.sinks.s1.type=logger
	
	#描述内存channel
	a1.channels.c1.type=memory
	a1.channels.c1.capacity=1000
	a1.channels.c1.transactionCapacity=100
	
	#为channel 绑定 source和sink
	a1.sources.r1.channels=c1
	a1.sinks.s1.channel=c1

```







#### Interceptor

> 拦截器是Flume中唯一可以对数据进行修改的机制
>
> 拦截器可以形成**责任链模式**
>
> Interceptor是Source的子组件，是配置在Source上
>
> 时间拦截器

##### TimeStamp Interceptor

> 这个拦截器在事件头中插入以毫秒为单位的当前处理时间
>
> 头部的名字为timestamp，值为当前处理的时间戳
>
> 如果在之前已经有这个时间戳，则保留原有的时间戳

> type
>
> > timestamp
>
> preserveExisting
>
> > fasle 如果时间戳以存在则保留

###### 配置示例

```
#配置Agent a1 的组件
a1.sources=r1
a1.sinks=s1 s2
a1.channels=c1 c2

#描述/配置a1的source1
a1.sources.r1.type=http
a1.sources.r1.port=8888
a1.sources.r1.interceptors=i1
a1.sources.r1.interceptors.i1.type=timestamp

#描述sink
a1.sinks.s1.type=avro
a1.sinks.s1.hostname=192.168.234.212
a1.sinks.s1.port=9999

a1.sinks.s2.type=avro
a1.sinks.s2.hostname=192.168.234.213
a1.sinks.s2.port=9999
#描述内存channel
a1.channels.c1.type=memory
a1.channels.c1.capacity=1000
a1.channels.c1.transactionCapacity=100

a1.channels.c2.type=memory
a1.channels.c2.capacity=1000
a1.channels.c2.transactionCapacity=100

#为channel 绑定 source和sink
a1.sources.r1.channels=c1 c2
a1.sinks.s1.channel=c1
a1.sinks.s2.channel=c2
```



##### Host Interceptor

> 这个拦截器插入当前处理Agent的主机名或ip
>
> 头的名字为host或配置的名称
>
> 值是主机名或ip地址，基于配置

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

###### 配置示例

```
#配置Agent a1 的组件
a1.sources=r1
a1.sinks=s1 s2
a1.channels=c1 c2

#描述/配置a1的source1
a1.sources.r1.type=http
a1.sources.r1.port=8888
a1.sources.r1.interceptors=i1
a1.sources.r1.interceptors.i1.type=host

#描述sink
a1.sinks.s1.type=avro
a1.sinks.s1.hostname=192.168.234.212
a1.sinks.s1.port=9999

a1.sinks.s2.type=avro
a1.sinks.s2.hostname=192.168.234.213
a1.sinks.s2.port=9999
#描述内存channel
a1.channels.c1.type=memory
a1.channels.c1.capacity=1000
a1.channels.c1.transactionCapacity=100

a1.channels.c2.type=memory
a1.channels.c2.capacity=1000
a1.channels.c2.transactionCapacity=100

#为channel 绑定 source和sink
a1.sources.r1.channels=c1 c2
a1.sinks.s1.channel=c1
a1.sinks.s2.channel=c2

```



##### Static Interceptor

> 此拦截器允许用户增加静态头信息使用静态的值到所有事件
>
> 目前的实现中不允许一次指定多个头
>
> 如果需要增加多个静态头可以指定多个Static interceptors

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

```
#配置Agent a1 的组件
a1.sources=r1
a1.sinks=s1 s2
a1.channels=c1 c2

#描述/配置a1的source1
a1.sources.r1.type=http
a1.sources.r1.port=8888
a1.sources.r1.interceptors=i1
a1.sources.r1.interceptors.i1.type=static
a1.sources.r1.interceptors.i1.key=addr
a1.sources.r1.interceptors.i1.value=beijing

#描述sink
a1.sinks.s1.type=avro
a1.sinks.s1.hostname=192.168.234.212
a1.sinks.s1.port=9999

a1.sinks.s2.type=avro
a1.sinks.s2.hostname=192.168.234.213
a1.sinks.s2.port=9999
#描述内存channel
a1.channels.c1.type=memory
a1.channels.c1.capacity=1000
a1.channels.c1.transactionCapacity=100

a1.channels.c2.type=memory
a1.channels.c2.capacity=1000
a1.channels.c2.transactionCapacity=100

#为channel 绑定 source和sink
a1.sources.r1.channels=c1 c2
a1.sinks.s1.channel=c1
a1.sinks.s2.channel=c2
```



##### Search And Replace Interceptor

> 这个拦截器提供了简单的基于字符串的正则搜索和替换功能

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



##### Regex Filtering Interceptor

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
