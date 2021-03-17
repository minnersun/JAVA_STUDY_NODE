## redis

------

### redis介绍

> redis的五种基本数据结构
>
> > `String	Hash	List	Set	Zset`

> `NOSQL`，`KEY-VALUE` 内存，可持久化，非关系型数据库	

> redis是基于c语言开发的，性能极高，基于内存处理的单进程单线程技术
>
> 客户端服务端的结构，支持多个客户端访问	万条/秒

> 定义
>
> > `NOSQL`：	非关系型数据库
> >
> > `key-value`：	存储数据的结构
> >
> > 内存：	运行过程完全基于内存
> >
> > > 优点	快
> > >
> > > 缺点	容易丢失（缓存允许丢失一部分，用户登陆关键---key）
> >
> > 可持久：	可以将数据持久化到磁盘文件中（容灾）

#### 缓存雪崩

> 缓存数据大量丢失
>
> 海量请求瞬间进入数据库导致数据库崩溃

> redis
>
> > 出现雪崩只需要恢复数据（持久化）
>
> memoryCache
>
> > 出现雪崩只能停止服务手动，手动恢复数据到内存中

### 非关系型数据库

> 关系型数据库
>
> > 
>
> > 数据存储结构上的关系
> >
> > > 1对1
> > >
> > > 1对多
> > >
> > > 多对多
>
> 非关系型数据库
>
> > `mongoDb`	`redis`
>
> > 无法体现其中的数据的关联
> >
> > > 常用的	`key--value`



### redis在云主机上的安装

> 主机环境	`CentOS6.5`镜像	
>
> > 预安装了一些软件
>
> 云主机文件夹
>
> > `presoftware`:预先安装的各种环境软件
> >
> > > 例如`jdk,ruby,node.js`
> >
> > `resources`
> >
> > > 学习使用各种技术进行安装是的资源包
> > >
> > > > 例如,`redis-3.2.11.tar.gz`是redis的安装包



#### redis解压安装

> 解压
>
> > 创建一个/home/software的文件夹
> >
> > > `mkdir /home/software`
> >
> > 进入文件夹拷贝redis的安装包
> >
> > > `cd /home/software`
> > >
> > > `cp /home/resources/redis-3.2.11.tar.gz  ./`
> >
> > tar 命令解压
> >
> > > `tar -xvf redis-3.2.11.tar.gz`

##### 编译和编译安装

> 在redis的根目录中,有src的文件夹,其中的各种脚本,配置,源码文件都需要进行编译操作

> 编译和编译安装的插件
>
> > 空环境下需要安装:`gcc,tcl*,make,cmake`的插件
> >
> > > `yum -y install gcc tcl* make cmake`
>
> > `make && make install`
> >
> > > make:	利用插件将c语言的文件进行编译
> > >
> > > make install:	相当于jdk的配置，让指令可以在任意位置执行

### redis的脚本启动和运行

> `redis-service [配置文件]`
>
> > 启动一个服务端	
> >
> > > `redis-service `
> >
> > 直接调用脚本以==默认配置==启动一个服务器
> >
> > > `ip：127.0.0.1`
> > >
> > > `端口	63379	等`
> >
> > `&`:	放在后台执行
> >
> > > `redis-service &`
>
> 
>
> 验证是否启动成功
>
> > `ps | grep redis`
>
> 
>
> `redis-cli -p 端口(默认6379) -h ip地址(默认127.0.0.1)`
>
> > 登录到某个redis的服务器
> >
> > > `redis-cli`	
> >
> > 直接书写以默认格式使用
> >
> > > `端口：	6379`
> > >
> > > `ip：	127.0.0.1`



 

### redis基础命令

> 详见下一天笔记，或老师笔记

> `keys`
>
> > 查看`key`值
> >
> > > `keys *`：	查看当前节点所有的key值
>
> `set key value `
>
> > 存储一个`key-value`结构的数据
> >
> > > `set age 22`	如果key值相同则会覆盖原有值
>
> `get key`
>
> > 获取当前key对用的value值
> >
> > > `get age`	获取age对应的value值
>
> `select 整数`
>
> > redis默认存在16个分区（0---15）
> >
> > 默认登陆的是0号分库
>
> `exists key （key）`
>
> > 判断当前节点是否包含key数据
> >
> > 查询单个时 `get` 也可以完成
> >
> > > `get`比较浪费资源
> >
> > > `exists haha kaka age`	查询是否存在haha	kaka	age 的 key
>
> `save`
>
> > 将当前的内存数据保存到磁盘文件
> >
> > > 默认保存在	dump.rbd中
>
> `flushall`
>
> > 将当前redis的所有数据清洗-----》 持久化文件，内存文件
> >
> > > `flushall`
>
> `flushdb`
>
> > 清空一个分库的文件-----》 不删除持久化文件内容
> >
> > > `flushdb`
>
> 

