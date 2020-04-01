## Flume笔记

-------

> Flume需要结合老师笔记（图）与课前资料（配置文件）同时学习

### 概述

> Flume是一套分布式的，可靠的，用于进行**日志的收集，汇集和传输**的系统

##### 版本

> Flume0.X      - Flume-og
>
> Flume1.X - Flume-ng，Flume-ng不兼容Flume-og

### 基本概念

#### Event

> **Flume**会将收集的每一条日志封装成一个**Event**对象
>
> **Event**对象本质上是一个**json串**，包含**headers**以及**body**，**收集的日志放在body中**

#### Agent

> 在**Flume**中，**Flume**的结构是以**Agent**形式来组建的
>
> **Agent**的组成
>
> > Source
> >
> > > 从数据源采集数据
> >
> > Channel
> >
> > > 缓存数据
> >
> > Sink
> >
> > > 将数据写往目的地
>
> 在**Flume**中，一个**Source**可以绑定**多个Channel**，但是一个**Sink**只能绑定一个**Channel**



##### Source

> ###### AVRO
>
> > 接收**AVRO序列化**之后的数据，结合**AVRO Sink**
> >
> > 可以实现
> >
> > > 多级流动
> > >
> > > 扇入流动
> > >
> > > 扇出流动
>
> ###### Spooling Directory
>
> > 监听**指定**的目录
> >
> > 可以实现
> >
> > > 当目录中添加文件的时候
> > >
> > > 这个文件中的内容会被自动收集
> > >
> > > **被收集过的文件再有改动，将不再收集**
>
> ###### HTTP
>
> > 监听HTTP请求
> >
> > > 一般监听POST请求，GET请求不稳定，其他请求方式不支持
>
> ###### 自定义Source
>
> > 需要实现**EventDrivenSource**或者**PollableSource**
> >
> > > **EventDrivenSource**
> > >
> > > > 事件驱动Source	**- 被动型Source**
> > > >
> > > > 不会提供线程获取数据，需要**自定义线程去获取数据**
> >
> > > **PollableSource**
> > >
> > > > 拉取Source 	**- 主动型Source**
> > > >
> > > > 提供了线程获取数据
> >
> > ##### 需要实现**Configurable**接口获取指定的属性值
>
> NetCat
>
> > 用来监听指定的端口，并接收监听到的数据
> >
> > 接收的数据是字符串形式

###### 自定义Source代码实现

> AuthSource

```java
package cn.tedu.source;

public class AuthSource extends AbstractSource implements EventDrivenSource, Configurable {

	private String path;
	private ExecutorService es;

	// 这个方法是用于获取格式文件中的属性值
	@Override
	public void configure(Context context) {
		path = context.getString("filePath");
	}

	@Override
	public synchronized void start() {

		es = Executors.newFixedThreadPool(5);

		// 获取Channel
		ChannelProcessor cp = super.getChannelProcessor();

		es.submit(new ReadLogThread(path, cp));

	}

	@Override
	public synchronized void stop() {
		es.shutdown();
	}
}

class ReadLogThread implements Runnable {

	private BufferedReader reader;
	private ChannelProcessor cp;

	public ReadLogThread(String path, ChannelProcessor cp) {
		File file = new File(path);
		if (file.isDirectory())
			throw new IllegalArgumentException(path);
		if (!path.endsWith("log"))
			throw new IllegalArgumentException(path);
		try {
			reader = new BufferedReader(new FileReader(file));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		this.cp = cp;
	}

	@Override
	public void run() {

		while (true) {
			try {
				String line1 = reader.readLine();
				if (line1 == null)
					return;
				String line2 = reader.readLine();
				if (line2 == null)
					return;
				// [2019-10-09 07:25:24]
				// a.avi get 10.3.53.3
				// Flume中将每一条日志封装成一个Event对象
				Map<String, String> headers = new HashMap<>();
				String[] arr = line1.split(" ");
				headers.put("date", arr[0].substring(1));
				headers.put("time", arr[1].substring(0, arr[1].length() - 1));
				// 创建一个Event对象
				Event e = EventBuilder.withBody(line2.getBytes(), headers);
				cp.processEvent(e);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
```



##### Channel

> ###### Memory
>
> > **内存**通道
> >
> > > 将数据存储在内存中，存储速度相对较**快**，但是**不可靠**
> > >
> > > > 在使用的时候需要指定容量
> > >
> > > **Memory Channel**如果满了，后续的数据会被阻塞
> > >
> > > > **Memory Channel**本质上是一个**BlockingDeque**

> ###### File
>
> > **文件**通道
> >
> > > 将数据存储在**磁盘**中
> > >
> > > 读写速度相对较**慢**，但是**可靠**

> JDBC Channel
>
> > 时间会被持久化到可靠的数据中
> >
> > 目前只支持嵌入式Derby数据库
>
> > 目前仅用于测试，不能用于生产环境

> 内存溢出通道
>
> > 优先把Event存到内存中，如果存不下，再溢出到文件中
>
> > 目前处于测试阶段，还未能用于生产环境



##### Sink

> 一个Sink只能绑定一个Channel

> ###### File_roll
>
> > 将数据**写入到文件**中。在使用的时候需要指定文件的存储路径
> >
> > > 如果不指定，默认情况：
> > >
> > > > 每隔30s产生一个新文件
>
> ###### HDFS
>
> > 将数据**写到HDFS**上，在连接**HDFS**的时候，需要指定**NameNode**所在地址
>
> ###### AVRO
>
> > 将数据源用avro序列化之后，写到指定的节点上
> >
> > 是实现多级流动，扇出流（1到多），扇入流（多到1）的基础
>
> Logger
>
> > 记录指定级别（INFO，DEBUG，ERROR）的日志
> >
> > > 通常用于调式



