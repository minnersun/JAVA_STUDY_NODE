## mycat

-------

### 故障转移

> 原理
>
> > 客户端发送sql语句
> >
> > mycat获取拦截sql，计算分片读写分离
> >
> > 获取后端连接对象发送
> >
> > 超时，调用心跳语句 `select user();`
> >
> > 多次尝试心跳，没有返回结果
> >
> > 根据dataHost配置的switchType的值，执行故障转移



#### 配置文件的修改

###### schema.xml

> `switchType`
>
> > `-1` ：不进行转移
> >
> > `1`：自动实现故障转移

> 在非分片表格的基础上
>
> 添加一个writeHost标签指向第二个云主机数据库
>
> > `<writeHost  host="hostM2"  url="10.9.104.184:3306"  user="root"  password="root"/> `

```xml
<dataHost name="localhost2" maxCon="1000" minCon="10" balance="0"
		writeType="0" dbType="mysql" dbDriver="native" switchType="1"  slaveThreshold="100">
    
		<heartbeat>select user()</heartbeat>
		<writeHost host="hostM1" url="10.9.100.26:3306" user="root" 
                   password="root"/>	<!-- ==index=0 -->
		<writeHost host="hostM2" url="10.9.104.184:3306" user="root" 
                   password="root"/> 	<!-- ==index=1 -->
    
</dataHost>
```



### 读写分离逻辑

> 主从复制（双机热备）总结
>
> > 无论什么时候搭建主从，一定确定双方的历史数据一致

##### 读的控制

> dataHost标签中的 `balance`属性
>
> > `0`：不开启读写分离
> >
> > `1`：开启读写分离
> >
> > > 所有的读操作，都在除了第一个writeHost以外的所有Host标签中进行，当并发量极高时，第一个host也会参与一部分分担
> >
> > `2`：随机在所有writeHost和readHost读取
> >
> > `3`：在所有的readHost中读取数据
> >
> > > 如果没有`readHost`将会只从第一个``writeHost`中读取
> >
> > > 前提`writeHost`不能`=1`	

###### schema.xml

> 逻辑表profuct作为一个分片表
>
> 使用的分片规则是`auto-sharding-long`
>
> > id：`[0-500w)`	切分到dn1
> >
> > id：`[500W-1000W)`	切分到dn2
> >
> > id：`[1000W+]` 报错
>
> 也可以在`rule.xml`自定义分片规则

```xml
<schema name="mstest" checkSQLschema="true" sqlMaxLimit="100">
    	<table name="product" primaryKey="ID" dataNode="dn1,dn2" rule="auto-sharding-long"/>
</schema>
```

###### rule.xml

```xml
	<tableRule name="auto-sharding-long">
		<rule>
			<columns>id</columns>
			<algorithm>rang-long</algorithm>
		</rule>
	</tableRule>

	<function name="rang-long"
		class="org.opencloudb.route.function.AutoPartitionByLong">
		<property name="mapFile">autopartition-long.txt</property>
	</function>
```

###### autopartition-long.txt

```
# range start-end ,data node index
# K=1000,M=10000.
0-30M=0
30M-60M=1                                          
```



##### hash一致性

> 使用datenode的名称做计算
>
> 使用每个计算方法指定的字段值

###### rule.xml

```xml
	<function name="murmur"
		class="org.opencloudb.route.function.PartitionByMurmurHash">
		<property name="seed">0</property><!-- 默认是0 -->
		<property name="count">2</property><!-- 要分片的数据库节点数量，必须指定，否则没法分片 -->
		<property name="virtualBucketTimes">160</property><!-- 一个实际的数据库节点被映射为这么多虚拟节点，默认是160倍，也就是虚拟节点数是物理节点数的160倍 -->
		<!-- <property name="weightMapFile">weightMapFile</property> 节点的权重，没有指定权重的节点默认是1。以properties文件的格式填写，以从0开始到count-1的整数值也就是节点索引为key，以节点权重值为值。所有权重值必须是正整数，否则以1代替 -->
		<!-- <property name="bucketMapPath">/etc/mycat/bucketMapPath</property> 
		用于测试时观察各物理节点与虚拟节点的分布情况，如果指定了这个属性，会把虚拟节点的murmur hash值与物理节点的映射按行输出到这个文件，没有默认值，如果不指定，就不会输出任何东西 -->
	</function>
```



### 全局表和ER分片表

##### 全局表

> 什么是全局表
>
> > 企业中常见的表格有很多种
> >
> > > 一种是业务表格
> > >
> > > > 比如：`cart，product，user`
> > >
> > > 还有一种叫工具字典
> > >
> > > > 长期和业务表格做关联操作
> > >
> > > > 比如：`HTTP的状态码`
> >
> > 字典工具表的特点
> >
> > > 数据量不大
> > >
> > > 数据不发生巨大变化
>
> mycat不支持跨分片操作
>
> > 认为所有跨分片的操作会降低数据库中间键的计算效率



### 使用全局配置

> 为了解决工具字典作为分片表格 与 业务分片表格的底层不跨分片，可以使用全局表配置
>
> 多个分片中的真实库有对应的表格，并且在配置的逻辑表table中指定多个分片，但不需要指定分片计算规则（默认同步）

#### ER分片表

> mycat底层不支持夸分片操作,如果需求中有多个相关的分片表格(t_order,t_order_item)进行关联操作时,需要引入ER分片的配置逻辑--mycat首创的.mycat无法支持大量数据的多对多关系

###### schema.xml

> `joinKey`:字表关联主表的字段名称
>
> `parentKey`：字表管理字段在主表中的名字

```xml
<schema name="mstest" checkSQLschema="true" sqlMaxLimit="100">	
	<table name="t_order" primaryKey="order_id" dataNode="dn1,dn2" rule="easymall-hash">
        
		<childTable name="t_order_item" primaryKey="ID" joinKey="order_id" parentKey="order_id"/>
	
    </table>
</schema>
```

###### rule.xml

> `murmur`：Hash一致性算法

```xml
	<tableRule name="easymall-hash">
		<rule>
			<columns>order_id</columns>
			<algorithm>murmur</algorithm>
		</rule>
	</tableRule>

```

