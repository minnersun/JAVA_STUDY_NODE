## Spring Boot概述

-------

### Spring Boot创建方式

> IDEA
>
> > Spring Boot在idea中以`Spring  Initializr`的方式创建
>
> Eclipse
>
> > 在eclipse中以Maven的形式创建

### Spring Boot嵌入式Web容器

> 在Spring Boot2.x中
>
> > spring-boot-starter-web
> >
> > > 依赖为Tomcat
> > >
> > > >  springboot中tomcat默认使用的是NIO
>
> > spring-boot-starter-webflux
> >
> > > 依赖为Netty

##### Netty与Tomcat的区别

> Tomcat是基于Http协议的，他的实质是一个基于http协议的web容器
>
> 但是Netty能通过编程自定义各种协议，因为netty能够通过codec自己来编码/解码字节流，完成类似redis访问的功能

###### Netty优势

> 并发高
> 传输快
> 封装好
> Netty为什么并发高
> Netty是一款基于NIO（Nonblocking I/O，非阻塞IO）开发的网络通信框架，对比于BIO（Blocking I/O，阻塞IO），他的并发性能得到了很大提高。



### 微服务的劣势

##### 部署

> 部署需要花费更长的时间，当一个服务出现问题之后，对运维人员是一种挑战

##### 服务间的接口问题

> 服务之间出现了相互调用，当一个接口发生变化时，所有调用这一接口的服务都需要调整才能正常使用

##### 高可用

> 出现多个服务调用同一个接口/服务时，这个服务的可用性需要我们更加注意

##### 分布式事务

> 微服务间可能使用不同的数据库，搜索服务用Elasticsearch、基础服务用MySQL、评论用MongoDB,
>
> 对==不同数据库间的一致性==将是我们面临的重大挑战

##### 网络复杂性

> 系统之间需要考虑网络延迟带来的影响，要保证服务间的正常运转

##### 测试的复杂性

> 服务间的接口调用，服务间的测试需要一套整体的测试方案，自动化测试显得必不可少



