## Spark

-------

### spark介绍

> spark是一种**快速，轻量，通用**的大数据**计算框架**

> 目前市场上的常见计算框架
>
> > MapReduce
> >
> > > 离线批处理
> >
> > Spark
> >
> > > 离线批处理+实时处理
> >
> > Strom
> >
> > > 实时处理
> >
> > Flink
> >
> > > 实时处理

#### MapReduce的缺点

> MRJob
>
> > MapTask
> >
> > ReduceTask
>
> > **shuffle**过程中会产生大量的**磁盘I/O**和**排序**的过程

#### Spark优化

> Spark在设计时，吸取了MapReduce的一些经验
>
> > 底层通过优化，**尽量避免产生Shuffle过程**，减少不必要的排序过程
> >
> > 支持**中间结果集**的**持久化**和**复用**
> >
> > > 从而**避免**重新计算带来的**计算代价**

#### Spark模块构成

> Apache Spark
>
> > core 核心模块
>
> Spark SQL
>
> > 类比hive
> >
> > 通过sql操作spark
> >
> > 交互式查询模块
>
> Spark Streaming
>
> > 实时计算模块
>
> MLib(machine learning)和GraphX(graph)
>
> > 机器学习（算法建模）模块



#### Spark可以处理大数据的场景

> 离线批处理
>
> 交互式查询
>
> 实时计算
>
> 算法建模

> Spark可以一栈式处理大数据的所有场景

### Spark环境搭建

> Local 
>
> > 本地单机模式
>
> Standalone
>
> > Spark集群模式
>
> On Yarn
>
> > 资源管理统一交给Yarn来管理



### RDD 	-弹性分布式数据集

> 可以将RDD看作是一种集合类型**(类比于Array或List)**
>
> > RDD是一种**特殊的集合类型**
>
> > **有分区机制(可以并行处理数据集)**
> >
> > > 极大提高处理效率
> > >
> > > 分区越多，并行度越高，处理越快
> >
> > **有容错机制**
> >
> > > 数据丢失可以恢复

#### 创建RDD的三种途径

##### 1.直接创建

> `sc.makeRDD(普通集合,分区数)`

##### 2.将一个普通的集合类型(Array或List)转变为RDD

> `sc.parallelize(a1,2)`
>
> > sc：SparkContext 
> >
> > > 通过此对象创建RDD，以及提交Job
> >
> > a1:
> >
> > > 普通集合
> >
> > 2
> >
> > > 指定的分区数量

##### 3.可以通过读取外部存储系统的文件，将文件数据转化为RDD

> `sc.textFile("hdfs://hadoop01:9000/1.txt"，2)`
>
> > 将Hadoop中的文件转化为RDD类型
>
> `sc.textFile("file://home/1.txt"，2)`
>
> > 将本地的数据类型转化为RDD类型



#### 查看RDD

> 查看RDD分区数
>
> > `xxx.partitions.size`
>
> 查看RDD每个分区数据
>
> > `xxx.glom.collection`



#### RDD的操作

##### Transformation

> 变换操作，是**懒操作(懒执行)**
>
> > 即方法调用之后，并不是马上进行处理和计算
> >
> > 每执行一次懒方法，就会创建一个新的RDD

##### Action

> **执行操作**
>
> > 调用Action方法后，才会触发懒方法的执行
> >
> > **典型**的方法:
> >
> > > **collect**



### RDD的操作

> `map()`
>
> > 将函数应用到rdd的每一个元素，返回值是**新的RDD**
>
> > 案例
> >
> > > `val rdd = sc.makeRDD(List(1,3,5,7,9))`
> > >
> > > `rdd.map(_*10)`
>
> `flatMap()`
>
> > 扁平化Map，对每一元素转化，然后再**扁平化处理**
>
> > 案例
> >
> > > `val rdd = sc.makeRDD(List("hello world","hello count","world spark"),2)`
> > >
> > > `rdd.flatMap(_.split{" "})`
> > >
> > > > `//Array[String] = Array(hello, world, hello, count, world, spark)`
>
> > map与flatMap的不同
> >
> > > map
> > >
> > > > 对RDD每个元素转换
> > >
> > > flatMap
> > >
> > > > 对RDD每个元素转换, 然后再扁平化（即去除集合）
>
> `filter()`
>
> > 用来从rdd中过滤掉不符合条件的数据
>
> `mapPartitions()`
>
> > 该函数和map函数类似，只不过映射函数的参数由RDD中的每一个元素变成了RDD中**每一个分区的迭代器**
>
> > 案例
> >
> > > `val rdd3 = rdd1.mapPartitions{ x => {`
> > >
> > > `val result = List[Int]()`
> > >
> > > `var i = 0`
> > >
> > > `while(x.hasNext){`
> > >
> > > `i += x.next()`
> > >
> > > `}`
> > >
> > > `result.::(i).iterator`
> > >
> > > `}}`
> > >
> > > `rdd3.collect`
>
> `mapPartitionsWithIndex()`
>
> > 函数作用同mapPartitions，不过提供了两个参数，**第一个参数为分区的索引**
>
> > 案例
> >
> > > var rdd1 = sc.makeRDD(1 to 5,2)
> > >
> > > 
> > >
> > > `var rdd2 = rdd1.mapPartitionsWithIndex{`
> > >
> > > `(index,iter) => {`
> > >
> > > `var result = List[String]()`
> > >
> > > `var i = 0`
> > >
> > > `while(iter.hasNext){`
> > >
> > > `i += iter.next()`
> > >
> > > `}`
> > >
> > > `result.::(index + "|" + i).iterator`
> > >
> > > `}`
> > >
> > > `}`

