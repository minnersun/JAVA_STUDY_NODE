## MapReduce笔记

---

### 合并	- Combine

> **Combine**的目的是为了在保证结果不变的前提下，减轻**ReduceTask**的计算压力，提高效率

> 实际过程中，往往会将Reduce也作为Combine来使用，只需要在Driver添加`job.setCombinerClass()`就可以
>
> **Combiner**的使用，要求不能改变执行结果，就导致并不是所有的场景都适合于使用**Combiner**
>
> **Combiner**是减少计算的数据量，但是不改变结果

### InputFormat	- 输入格式

> 笔记有图解

> **InputFormat**是**MapReduce**中顶级的输入格式类，提供了2个抽象方法
>
> > **getSplits**(获取切片)和**createRecordReader**（针对切片产生输入流，用于读取切片）
> >
> > 针对文件的操作，更多的是实现**InputFormat**的子类**FileInputFormat**，这个子类中重写了**getSplits**方法，只需要自己覆盖**createRecordReader**
>
> 默认使用的**TextInputFormat**
>
> > 从第二个**MapTask**开始，每一个**MapTask**从当前的第二行开始到下一个切片的第一行，这样做的目的是为了保证数据的完整性
>
> 
>
> 多源输入的前提下，允许数据对应的**输入格式**以及**Mapper**不一样，但是**输出格式**要一致

#### cn.tedu.authinput

##### Score.java

```java
package cn.tedu.authinput;

public class Score implements Writable {

	private int math;
	private int english;

	@Override
	public void write(DataOutput out) throws IOException {
		out.writeInt(math);
		out.writeInt(english);
	}

	@Override
	public void readFields(DataInput in) throws IOException {
		this.math = in.readInt();
		this.english = in.readInt();
	}
}
```

##### AuthInputFormat.java

```java
package cn.tedu.authinput;

public class AuthInputFormat extends FileInputFormat<Text, Text> {

	@Override
	public RecordReader<Text, Text> createRecordReader(InputSplit split, TaskAttemptContext context)
			throws IOException, InterruptedException {
		return new AuthReader();
	}

}

class AuthReader extends RecordReader<Text, Text> {

	private LineReader reader;
	private Text blank = new Text(" ");
	private Text key;
	private Text value;

	// 初始化
	@Override
	public void initialize(InputSplit split, TaskAttemptContext context) throws IOException, InterruptedException {

		// 转化为一个文件切片
		FileSplit fileSplit = (FileSplit) split;
		// 获取切片对应的路径
		Path path = fileSplit.getPath();

		// 连接HDFS
		FileSystem fs = FileSystem.get(URI.create(path.toString()), context.getConfiguration());
		// 获取针对切片的输入流
		InputStream in = fs.open(path);
		// 将子节点包装成一个字符流
		// hadoop.util包下的LineReader
		reader = new LineReader(in);
	}

	// MapTask在处理数据的时候，先调用这个方法判断是否有下一个键值对
	// 如果有下一个键值对，才会调用map方法
	// 试图去读取下一个键值对
	// 如果能读取到，那么返回true，表示有
	// 如果读取不到，那么返回false
	@Override
	public boolean nextKeyValue() throws IOException, InterruptedException {
		// 存储键
		key = new Text();
		// 存储值
		value = new Text();
		// 临时值
		Text tmp = new Text();

		// 返回值表示读取到的字符个数
		// 如果没有读取到数据，则返回0
		// 读取第一行，表示键
		if (reader.readLine(tmp) == 0)
			return false;
		key.set(tmp);
		// 读取第二行和第三行表示值
		if (reader.readLine(tmp) == 0)
			return false;
		value.set(tmp);
		value.append(blank.getBytes(), 0, blank.getLength());
		if (reader.readLine(tmp) == 0)
			return false;
		value.append(tmp.getBytes(), 0, tmp.getLength());
		return true;
	}

	// 获取键
	@Override
	public Text getCurrentKey() throws IOException, InterruptedException {
		return key;
	}

	@Override
	public Text getCurrentValue() throws IOException, InterruptedException {
		return value;
	}

	// 获取执行进度
	@Override
	public float getProgress() throws IOException, InterruptedException {
		return 0;
	}

	// 回收
	@Override
	public void close() throws IOException {

		if (reader != null)
			reader.close();
	}
}
```

##### AuthMapper.java

```java
package cn.tedu.authinput;

public class AuthMapper extends Mapper<Text, Text, Text, Score> {

	// key = tom
	// value = math 90 english 98
	public void map(Text key, Text value, Context context) throws IOException, InterruptedException {

		String[] arr = value.toString().split(" ");
		Score s = new Score();
		s.setMath(Integer.parseInt(arr[1]));
		s.setEnglish(Integer.parseInt(arr[3]));

		context.write(key, s);
	}
}
```

##### AuthReducer.java

````java
package cn.tedu.authinput;

public class AuthReducer extends Reducer<Text, Score, Text, IntWritable> {

	public void reduce(Text key, Iterable<Score> values, Context context) throws IOException, InterruptedException {
		int sum = 0;
		for (Score val : values) {
			sum = val.getMath() + val.getEnglish();
		}
		context.write(key, new IntWritable(sum));
	}
}
````

##### AuthDriver.java

```java
package cn.tedu.authinput;

public class AuthDriver {

	public static void main(String[] args) throws Exception {
		Configuration conf = new Configuration();
		// 指定键和值之间的间隔符
		conf.set("mapreduce.output.textoutputformat.separator", "|");
		Job job = Job.getInstance(conf, "JobName");
		job.setJarByClass(cn.tedu.authinput.AuthDriver.class);
		job.setMapperClass(AuthMapper.class);
		job.setReducerClass(AuthReducer.class);

		job.setMapOutputKeyClass(Text.class);
		job.setMapOutputValueClass(Score.class);

		job.setOutputKeyClass(Text.class);
		job.setOutputValueClass(IntWritable.class);

		// 设置输入格式类
		job.setInputFormatClass(AuthInputFormat.class);

		// 多源输入
		// 在多源输入的前提下，输入的文件格式可以不一样
		// MultipleInputs.addInputPath(job, new Path("/a/a.log"),
		// AuthInputFormat.class);
		// MultipleInputs.addInputPath(job, new Path("/b/b.log"),
		// TextInputFormat.class);
		// MultipleInputs.addInputPath(job, path, inputFormatClass,
		// mapperClass);
		FileInputFormat.setInputPaths(job, new Path("hdfs://192.168.195.132:9000/txt/score3.txt"));
		FileOutputFormat.setOutputPath(job, new Path("hdfs://192.168.195.132:9000/result/authinput2"));

		if (!job.waitForCompletion(true))
			return;
	}
}
```



### 数据倾斜

> 数据本身具有倾斜的特性，所以在处理过程中会产生数据倾斜

**Map**端的数据倾斜产生的条件

>  输入了多个文件，这些文件不可切，并且文件大小不均匀

> **Map**端产生数据倾斜，无法解决

**Reduce**端产生数据倾斜

> **Reduce**端，实际开发中，绝大多数的数据倾斜都发生在**Reduce**端

> **Reduce**端数据倾斜的处理方法
>
> > 分阶段聚合
>
> > 连接join

#### cn.tedu.join

##### Order.java

```java
package cn.tedu.join;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

import org.apache.hadoop.io.Writable;

public class Order implements Writable {

	private String orderid = "";
	private String date = "";
	private int proid;
	private int num;
	private String name = "";
	private double price;

	@Override
	public void write(DataOutput out) throws IOException {

		out.writeUTF(orderid);
		out.writeUTF(date);
		out.writeInt(proid);
		out.writeInt(num);
		out.writeUTF(name);
		out.writeDouble(price);
	}

	@Override
	public void readFields(DataInput in) throws IOException {
		this.orderid = in.readUTF();
		this.date = in.readUTF();
		this.proid = in.readInt();
		this.num = in.readInt();
		this.name = in.readUTF();
		this.price = in.readDouble();
	}
}
```

##### JoinMapper.java

```java
package cn.tedu.join;

public class JoinMapper extends Mapper<LongWritable, Text, Text, Order> {

	private Map<Integer, Order> map = new HashMap<>();

	// 在这个方法中，将小文件提前解析
	// 然后如果map处理过程中需要数据
	// 则直接从解析出来的内容中找
	@Override
	protected void setup(Mapper<LongWritable, Text, Text, Order>.Context context)
			throws IOException, InterruptedException {

		// 从缓存中将小文件取出来
		URI file = context.getCacheFiles()[0];
		// 连接HDFS
		FileSystem fs = FileSystem.get(file, context.getConfiguration());
		// 获取针对这个文件的输入流
		InputStream in = fs.open(new Path(file.getPath()));
		// 将字节流包装成一个字符流
		BufferedReader reader = new BufferedReader(new InputStreamReader(in));
		// 进行按行读取
		String line = null;
		while ((line = reader.readLine()) != null) {
			// 1 chuizi 3999
			String[] arr = line.split(" ");
			Order o = new Order();
			o.setProid(Integer.parseInt(arr[0]));
			o.setName(arr[1]);
			o.setPrice(Double.parseDouble(arr[2]));

			map.put(o.getProid(), o);
		}
		reader.close();
	}

	// 处理大文件
	public void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {

		String[] arr = value.toString().split(" ");
		Order o = new Order();
		// 1001 20170710 4 2
		o.setOrderid(arr[0]);
		o.setName(arr[1]);
		o.setProid(Integer.parseInt(arr[2]));
		o.setNum(Integer.parseInt(arr[3]));
		o.setName(map.get(o.getProid()).getName());
		o.setPrice(map.get(o.getProid()).getPrice());
		
		context.write(new Text(o.getOrderid()), o);
	}
}
```

##### JoinReducer.java

```java
package cn.tedu.join;

public class JoinReducer extends Reducer<Text, Order, Text, DoubleWritable> {

	public void reduce(Text key, Iterable<Order> values, Context context) throws IOException, InterruptedException {
		double total = 0;
		for (Order val : values) {
			total = val.getNum() * val.getPrice();
		}
		context.write(key, new DoubleWritable(total));
	}
}
```

##### JoinDriver.java

```java
package cn.tedu.join;

public class JoinDriver {
    
	public static void main(String[] args) throws Exception {
		Configuration conf = new Configuration();
		Job job = Job.getInstance(conf, "JobName");
		job.setJarByClass(cn.tedu.join.JoinDriver.class);
		job.setMapperClass(JoinMapper.class);
		job.setReducerClass(JoinReducer.class);

		job.setMapOutputKeyClass(Text.class);
		job.setMapOutputValueClass(Order.class);
		
		job.setOutputKeyClass(Text.class);
		job.setOutputValueClass(DoubleWritable.class);

		// 需要先缓存小文件
		URI[] files = new URI[1];
		files[0] = URI.create("hdfs://192.168.195.132:9000/txt/union/product.txt");
		job.setCacheFiles(files);

		FileInputFormat.setInputPaths(job, new Path("hdfs://192.168.195.132:9000/txt/union/order.txt"));
		FileOutputFormat.setOutputPath(job, new Path("hdfs://192.168.195.132:9000/result/join"));

		if (!job.waitForCompletion(true))
			return;
	}
}
```







### 小文件的危害

> 存储 - HDFS：大量小文件会产生大量的元数据，会加剧内存的消耗，甚至于会导致内存被占满从而减低NameNode的处理效率
>
> 计算 - MapReduce：每一个小文件会对应一个切片，一个切片对应一个MapTask，所以意味着如果有大量的小文件则会产生大量的MapTask，每一个MapTask处理的数据量不大，但是MapTask数量多了可能会导致资源消耗加剧，甚至于会导致服务器的崩溃

#### 小文件的处理手段最常用的方式有2种

> 合并和打包

> Hadoop提供了一种原生的处理方式
>
> > HAR(Hadoop Archive)
> >
> > > 官方的说法是将多个小文件打成一个har包，但是实际上是将多个小文件合并成一个大文件



### 推测执行机制

> 推测执行机制实际上是Hadoop中针对"慢任务"的一种优化手段。如果出现了慢任务，则Hadoop将这个任务复制一份放到其他节点上执行，两个节点一起执行，谁先执行完成，则执行结果为最后结果，另一个没有完成的任务就会被kill掉
>
> 因为实际生产场景中，数据倾斜导致的慢任务更多，而此时推测执行机制并不能提高效率反而会导致集群资源的浪费，所以绝大部分情况下会关闭推测执行机制


