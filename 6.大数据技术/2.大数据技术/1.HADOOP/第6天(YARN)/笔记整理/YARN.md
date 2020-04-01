## YARN

----

HADOOP完全分布式搭建

> 见笔记



###概述

> **YARN**(Yet Another Resource Negotiator)
>
> > 主要负责资源的**协调/管理**和**任务的调度**

### YARN产生的原因

> 内因
>
> > **JobTracker**同时监控节点，任务数量是比较多的，导致整个集群的协调压力全部落到了**JobTracker**上
> >
> > 当**集群节点超过4000**个的时候，JobTracker会出现性能下降，甚至于崩溃的问题
>
> 外果
>
> > Hadoop发展过程中，产生了越来越多的**计算框架**，而很多计算框架都是基于HDFS来实现的
> >
> > **计算框架**之间的**计算模式**不一样，计算框架的设计必然会涉及到任务的划分和监控，以及资源的使用
> >
> > 为了解决不同框架之间任务冲突以及资源占用问题，需要设计一套统一的框架来**解决任务分配和资源分配问题**

### 主要包含的三类进程

> ResourceManager
>
> > 用于进行资源管理
>
> NodeManager
>
> > 完全取代了TaskTracker，用于执行任务
>
> ApplicationMaster
>
> > 只有在任务需要执行的时候才会出现，负责进行任务的调度和监控

> **ResourceManager**管理**Application**，**ApplicationMaster**管理具体任务



### Job执行流程

> 图见笔记

> **ResourceManage**接受任务，等待**NodeManager**心跳
>
> **ResourceManager**收到**NodeManager**心跳之后，进行响应，要求这个**NdoeManager**开启一个进程**ApplicationMaster**
>
> **NodeManager**在开启完**ApplicationMaster**之后会再次心跳
>
> **ResourceManager**在收到心跳之后会将**job**任务分配给这个**ApplicationMaster**
>
> **ResourceManager**在收到请求之后会进行资源的划分，将这个请求所需要的资源封装成一个**Container**对象（包含给这个任务所分配的内存大小以及CPU的核数，**默认一个任务分配1G内存+1CPU核**）
>
> **ApplicationMaster**收到**Container**之后，会将资源进行二次划分，划分给每一个**MapTask**和**ReduceTask**
>
> **ApplicationMaster**将**MapTask**和**ReduceTask**分配给**NodeManager**，并且监控这些任务的执行
>
> > 如果执行成功
> >
> > > 给**ApplicationMaster**一个成功的信号，并且释放资源
> >
> > 如果执行失败
> >
> > > 给**ApplicationMaster**一个失败的信号，并且释放资源
> > >
> > > 然后重新启动这个**MapTask**或者**ReduceTask**



