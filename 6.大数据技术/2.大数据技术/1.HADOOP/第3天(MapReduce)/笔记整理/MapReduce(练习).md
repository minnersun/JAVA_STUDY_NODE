## MapReduce练习

---

### 统计文件中每一个非空字符出现的次数

> 文件`characters.txt - cn.tedu.charcount`

#### cn.tedu.charcount

##### CharCountMapper.java

```java
package cn.tedu.charcount;

public class CharCountMapper extends Mapper<LongWritable, Text, Text, LongWritable> {

	public void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {

		char[] cs = value.toString().toCharArray();
		for (char c : cs) {
			if (c != ' ')
				context.write(new Text(c + ""), new LongWritable(1));
		}
	}

}
```

##### CharCountReducer.java

```java
package cn.tedu.charcount;

public class CharCountReducer extends Reducer<Text, LongWritable, Text, LongWritable> {

	public void reduce(Text key, Iterable<LongWritable> values, Context context)
			throws IOException, InterruptedException {
		long sum = 0;
		for (LongWritable val : values) {
			sum += val.get();
		}
		context.write(key, new LongWritable(sum));
	}

}
```

##### CharCountDriver.java

```java
package cn.tedu.charcount;

public class CharCountDriver {

	public static void main(String[] args) throws Exception {
		Configuration conf = new Configuration();
		Job job = Job.getInstance(conf, "JobName");
		job.setJarByClass(cn.tedu.charcount.CharCountDriver.class);
		job.setMapperClass(CharCountMapper.class);
		job.setReducerClass(CharCountReducer.class);

		job.setOutputKeyClass(Text.class);
		job.setOutputValueClass(LongWritable.class);

		FileInputFormat.setInputPaths(job, new Path("hdfs://192.168.195.132:9000/txt/characters.txt"));
		FileOutputFormat.setOutputPath(job, new Path("hdfs://192.168.195.132:9000/result/charcount"));

		if (!job.waitForCompletion(true))
			return;
	}

}
```









### ip去重

> 文件：ip.txt - cn.tedu.ip

#### cn.tedu.ip

##### IPMapper.java

```java
package cn.tedu.ip;

public class IPMapper extends Mapper<LongWritable, Text, Text, NullWritable> {

	public void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {

		context.write(value, NullWritable.get());

	}

}

```

##### IPReducer.java

```java
package cn.tedu.ip;

public class IPReducer extends Reducer<Text, NullWritable, Text, NullWritable> {

	public void reduce(Text key, Iterable<NullWritable> values, Context context)
			throws IOException, InterruptedException {
		context.write(key, NullWritable.get());
	}

}

```

##### IPDriver.java

```java
package cn.tedu.ip;

public class IPDriver {

	public static void main(String[] args) throws Exception {
		
		Configuration conf = new Configuration();
		Job job = Job.getInstance(conf, "JobName");
		job.setJarByClass(cn.tedu.ip.IPDriver.class);
		job.setMapperClass(IPMapper.class);
		job.setReducerClass(IPReducer.class);

		job.setOutputKeyClass(Text.class);
		job.setOutputValueClass(NullWritable.class);

		FileInputFormat.setInputPaths(job, new Path("hdfs://192.168.195.132:9000/txt/ip.txt"));
		FileOutputFormat.setOutputPath(job, new Path("hdfs://192.168.195.132:9000/result/ip2"));

		if (!job.waitForCompletion(true))
			return;
	}

}
```









### 取最大值

> score2.txt      - cn.tedu.maxscore

#### cn.tedu.maxscore

##### MaxScoreMapper.java

```java
package cn.tedu.maxscore;

public class MaxScoreMapper extends Mapper<LongWritable, Text, Text, IntWritable> {

	public void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {

		String[] arr = value.toString().split(" ");
		context.write(new Text(arr[0]), 
				new IntWritable(Integer.parseInt(arr[1])));
	}

}
```

##### MaxScoreReducer.java

```java
package cn.tedu.maxscore;

public class MaxScoreReducer extends Reducer<Text, IntWritable, Text, IntWritable> {

	public void reduce(Text key, Iterable<IntWritable> values, Context context)
			throws IOException, InterruptedException {
		// 在Reducer的迭代器中，为了节省内存空间
		// 才用了地址复用机制
		IntWritable max = new IntWritable(0);
		// key = Bob
		// values = 684 340 548 312
		// IntWritable val = new IntWritable();
		// val.set(684);
		// val.get() > max.get() -> 684 > 0 -> true
		// max = val; 赋值给的是地址 -> max和val的地址一样
		// val.set(340); 所以max的值也变成了340
		// val.get() > max.get() -> 340 > 340 -> false
		// val.set(548); 此时max的值也变成了548
		// 后续的所有比较实际上自己和自己比较，那么比较结果一定是false
		// 最后输出的结果是最后一个被遍历的值
		for (IntWritable val : values) {
			if (val.get() > max.get())
				// max = val;
				max.set(val.get());
		}
		context.write(key, max);
	}

}
```

##### MaxScoreDriver.java

```java
package cn.tedu.maxscore;

public class MaxScoreDriver {

	public static void main(String[] args) throws Exception {
		Configuration conf = new Configuration();
		Job job = Job.getInstance(conf, "JobName");
		job.setJarByClass(cn.tedu.maxscore.MaxScoreDriver.class);
		job.setMapperClass(MaxScoreMapper.class);
		job.setReducerClass(MaxScoreReducer.class);

		job.setOutputKeyClass(Text.class);
		job.setOutputValueClass(IntWritable.class);

		FileInputFormat.setInputPaths(job, 
				new Path("hdfs://192.168.195.132:9000/txt/score2.txt"));
		FileOutputFormat.setOutputPath(job, 
				new Path("hdfs://192.168.195.132:9000/result/maxscore2"));

		if (!job.waitForCompletion(true))
			return;
	}
}
```









### 求总分

> score2 	- cn.tedu.totalscore

#### cn.tedu.totalscore

##### TotalScoreMapper.java

```java
package cn.tedu.totalscore;

public class TotalScoreMapper extends Mapper<LongWritable, Text, Text, IntWritable> {

	public void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {

		String[] arr = value.toString().split(" ");
		context.write(new Text(arr[0]), new IntWritable(Integer.parseInt(arr[1])));

	}
}
```

##### TotalScoreReducer.java

```java
package cn.tedu.totalscore;

public class TotalScoreReducer 
	extends Reducer<Text, IntWritable, Text, IntWritable> {

	public void reduce(Text key, Iterable<IntWritable> values, Context context) throws IOException, InterruptedException {
		int sum = 0;
		for (IntWritable val : values) {
			sum += val.get();
		}
		context.write(key, new IntWritable(sum));
	}

}
```

##### TotalScoreDriver.java

```java
package cn.tedu.totalscore;

public class TotalScoreDriver {

	public static void main(String[] args) throws Exception {
		Configuration conf = new Configuration();
		Job job = Job.getInstance(conf, "JobName");
		job.setJarByClass(cn.tedu.totalscore.TotalScoreDriver.class);
		job.setMapperClass(TotalScoreMapper.class);
		job.setReducerClass(TotalScoreReducer.class);

		job.setOutputKeyClass(Text.class);
		job.setOutputValueClass(IntWritable.class);

		FileInputFormat.setInputPaths(job, new Path("hdfs://192.168.195.132:9000/txt/score2/"));
		FileOutputFormat.setOutputPath(job, new Path("hdfs://192.168.195.132:9000/result/totalscore"));

		if (!job.waitForCompletion(true))
			return;
	}
}
```









## 序列化练习  -Writable

------------------



### 统计每一个人的总流量

> flow.txt	- cn.tedu.serialflow

#### cn.tedu.serialflow

##### Flow.java

```java
package cn.tedu.serialflow;

public class Flow implements Writable {

	private String phone;
	private String city;
	private String name;
	private int flow;

	// 反序列化
	// 按照写出的顺序来依次读取
	@Override
	public void readFields(DataInput in) throws IOException {
		this.phone = in.readUTF();
		this.city = in.readUTF();
		this.name = in.readUTF();
		this.flow = in.readInt();
	}

	// 序列化
	// 只需要将需要被序列化的属性来依次写出即可
	@Override
	public void write(DataOutput out) throws IOException {
		out.writeUTF(phone);
		out.writeUTF(city);
		out.writeUTF(name);
		out.writeInt(flow);
	}

}

```

##### SerialFlowMapper.java

```java
package cn.tedu.serialflow;

public class SerialFlowMapper extends Mapper<LongWritable, Text, Text, Flow> {

	public void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {

		String[] arr = value.toString().split(" ");
		Flow f = new Flow();
		f.setPhone(arr[0]);
		f.setCity(arr[1]);
		f.setName(arr[2]);
		f.setFlow(Integer.parseInt(arr[3]));
		context.write(new Text(f.getName()), f);

	}
}
```

##### SerialFlowReducer.java

```java
package cn.tedu.serialflow;

// 统计每个人花费的总流量
// 打印每个人出现过的城市
public class SerialFlowReducer 
	extends Reducer<Text, Flow, Text, IntWritable> {

	public void reduce(Text _key, Iterable<Flow> values, Context context) throws IOException, InterruptedException {
		int sum = 0;
		for (Flow val : values) {
			sum += val.getFlow();
		}
		context.write(_key, new IntWritable(sum));
	}

}

```

##### SerialFlowDriver.java

```java
package cn.tedu.serialflow;

public class SerialFlowDriver {

	public static void main(String[] args) throws Exception {
		Configuration conf = new Configuration();
		Job job = Job.getInstance(conf, "JobName");
		job.setJarByClass(cn.tedu.serialflow.SerialFlowDriver.class);
		job.setMapperClass(SerialFlowMapper.class);
		job.setReducerClass(SerialFlowReducer.class);

		job.setMapOutputKeyClass(Text.class);
		job.setMapOutputValueClass(Flow.class);
		job.setOutputKeyClass(Text.class);
		job.setOutputValueClass(IntWritable.class);

		FileInputFormat.setInputPaths(job, new Path("hdfs://192.168.195.132:9000/txt/flow.txt"));
		FileOutputFormat.setOutputPath(job, new Path("hdfs://192.168.195.132:9000/result/serialflow"));

		if (!job.waitForCompletion(true))
			return;
	}
}

```











### 统计每一个人的总分

> score.txt - cn.tedu.serialscore

#### cn.tedu.serialscore

##### Score.java

```java
package cn.tedu.serialscore;

public class Score implements Writable {

	private String name;
	private int chinese;
	private int math;
	private int english;

	@Override
	public void readFields(DataInput in) throws IOException {
		this.name = in.readUTF();
		this.chinese = in.readInt();
		this.math = in.readInt();
		this.english = in.readInt();
	}

	@Override
	public void write(DataOutput out) throws IOException {
		out.writeUTF(name);
		out.writeInt(chinese);
		out.writeInt(math);
		out.writeInt(english);
	}

}
```

##### SerialScoreMapper.java

```java
package cn.tedu.serialscore;

public class SerialScoreMapper extends Mapper<LongWritable, Text, Text, Score> {

	public void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {

		String[] arr = value.toString().split(" ");
		Score s = new Score();
		s.setName(arr[0]);
		s.setChinese(Integer.parseInt(arr[1]));
		s.setMath(Integer.parseInt(arr[2]));
		s.setEnglish(Integer.parseInt(arr[3]));

		context.write(new Text(s.getName()), s);

	}
}
```

##### SerialScoreReducer.java

```java
package cn.tedu.serialscore;

public class SerialScoreReducer extends Reducer<Text, Score, Text, IntWritable> {

	public void reduce(Text key, Iterable<Score> values, Context context) throws IOException, InterruptedException {
		int total = 0;
		for (Score val : values) {
			total = val.getChinese() + val.getMath() + val.getEnglish();
		}
		context.write(key, new IntWritable(total));
	}
}
```

##### SerialScoreDriver.java

```java
package cn.tedu.serialscore;

public class SerialScoreDriver {

	public static void main(String[] args) throws Exception {
		Configuration conf = new Configuration();
		Job job = Job.getInstance(conf, "JobName");
		job.setJarByClass(cn.tedu.serialscore.SerialScoreDriver.class);
		job.setMapperClass(SerialScoreMapper.class);
		job.setReducerClass(SerialScoreReducer.class);

		job.setMapOutputKeyClass(Text.class);
		job.setMapOutputValueClass(Score.class);
		job.setOutputKeyClass(Text.class);
		job.setOutputValueClass(IntWritable.class);

		FileInputFormat.setInputPaths(job, new Path("hdfs://192.168.195.132:9000/txt/score.txt"));
		FileOutputFormat.setOutputPath(job, new Path("hdfs://192.168.195.132:9000/result/serialscore"));

		if (!job.waitForCompletion(true))
			return;
	}

}

```









## 分区练习 --- Partitioner

---------

### 按城市统计每一个人花费的总流量

> flow.txt- cn.tedu.partflow

#### cn.tedu.partflow

##### Flow.java

```java
package cn.tedu.partflow;

public class Flow implements Writable {

	private String phone;
	private String city;
	private String name;
	private int flow;

	@Override
	public void readFields(DataInput in) throws IOException {
		this.phone = in.readUTF();
		this.city = in.readUTF();
		this.name = in.readUTF();
		this.flow = in.readInt();
	}

	@Override
	public void write(DataOutput out) throws IOException {
		out.writeUTF(phone);
		out.writeUTF(city);
		out.writeUTF(name);
		out.writeInt(flow);
	}
}
```

##### PartFlowMapper.java

```java
package cn.tedu.partflow;

public class PartFlowMapper 
	extends Mapper<LongWritable, Text, Text, Flow> {

	public void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
		String[] arr = value.toString().split(" ");
		Flow f = new Flow();
		f.setPhone(arr[0]);
		f.setCity(arr[1]);
		f.setName(arr[2]);
		f.setFlow(Integer.parseInt(arr[3]));
		context.write(new Text(f.getName()), f);
	}

}

```

##### CityPartitioner.java

````java
package cn.tedu.partflow;

public class CityPartitioner extends Partitioner<Text, Flow> {

	// 分区默认从0开始，依次向上递增
	@Override
	public int getPartition(Text key, Flow value, int numReduceTasks) {

		String city = value.getCity();
		if (city.equals("bj"))
			return 0;
		else if (city.equals("sh"))
			return 1;
		else
			return 2;
	}
}
````

##### PartFlowReducer.java

```java
package cn.tedu.partflow;

public class PartFlowReducer extends Reducer<Text, Flow, Text, IntWritable> {

	public void reduce(Text key, Iterable<Flow> values, Context context) throws IOException, InterruptedException {
		int sum = 0;
		for (Flow val : values) {
			sum += val.getFlow();
		}
		context.write(key, new IntWritable(sum));
	}

}

```

##### PartFlowDriver.java

```java
package cn.tedu.partflow;

public class PartFlowDriver {

	public static void main(String[] args) throws Exception {
		Configuration conf = new Configuration();
		Job job = Job.getInstance(conf, "JobName");
		job.setJarByClass(cn.tedu.partflow.PartFlowDriver.class);
		job.setMapperClass(PartFlowMapper.class);
		job.setReducerClass(PartFlowReducer.class);
		
		// 设置分区类
		job.setPartitionerClass(CityPartitioner.class);
		// 如果不设置，那么默认只有ReduceTask来处理
		// 设置ReduceTask的数量
		job.setNumReduceTasks(3);

		job.setMapOutputKeyClass(Text.class);
		job.setMapOutputValueClass(Flow.class);
		job.setOutputKeyClass(Text.class);
		job.setOutputValueClass(IntWritable.class);

		FileInputFormat.setInputPaths(job, new Path("hdfs://192.168.195.132:9000/txt/flow.txt"));
		FileOutputFormat.setOutputPath(job, new Path("hdfs://192.168.195.132:9000/result/partflow"));

		if (!job.waitForCompletion(true))
			return;
	}
}
```











### 按月份统计每一个人的总分

> score1	- cn.tedu.partscore

#### cn.tedu.partscore

##### Score.java

```java
package cn.tedu.partscore;

public class Score implements Writable {

	private int month;
	private String name;
	private int score;

	@Override
	public void readFields(DataInput in) throws IOException {
		this.month = in.readInt();
		this.name = in.readUTF();
		this.score = in.readInt();
	}

	@Override
	public void write(DataOutput out) throws IOException {
		out.writeInt(month);
		out.writeUTF(name);
		out.writeInt(score);
	}
}
```

##### PartScoreMapper.java

```java
package cn.tedu.partscore;

public class PartScoreMapper extends Mapper<LongWritable, Text, Text, Score> {

	public void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {

		String[] arr = value.toString().split(" ");
		Score s = new Score();
		s.setMonth(Integer.parseInt(arr[0]));
		s.setName(arr[1]);
		s.setScore(Integer.parseInt(arr[2]));

		context.write(new Text(s.getName()), s);
	}
}

```

##### MonthPartitioner.java

```java
package cn.tedu.partscore;

public class MonthPartitioner extends Partitioner<Text, Score> {

	@Override
	public int getPartition(Text key, Score value, int numReduceTasks) {
		int month = value.getMonth();
		// 1 - 0
		// 2 - 1
		// 3 - 2
		return month - 1;
	}
}
```

##### PartScoreReducer.java

```java
package cn.tedu.partscore;

public class PartScoreReducer extends Reducer<Text, Score, Text, IntWritable> {

	public void reduce(Text key, Iterable<Score> values, Context context) throws IOException, InterruptedException {
		int sum = 0;
		for (Score val : values) {
			sum += val.getScore();
		}
		context.write(key, new IntWritable(sum));
	}
}
```

##### PartScoreDriver.java

```java
package cn.tedu.partscore;

public class PartScoreDriver {

	public static void main(String[] args) throws Exception {
		Configuration conf = new Configuration();
		Job job = Job.getInstance(conf, "JobName");
		job.setJarByClass(cn.tedu.partscore.PartScoreDriver.class);
		job.setMapperClass(PartScoreMapper.class);
		job.setReducerClass(PartScoreReducer.class);

		job.setPartitionerClass(MonthPartitioner.class);
		job.setNumReduceTasks(3);

		job.setMapOutputKeyClass(Text.class);
		job.setMapOutputValueClass(Score.class);
		job.setOutputKeyClass(Text.class);
		job.setOutputValueClass(IntWritable.class);

		FileInputFormat.setInputPaths(job, new Path("hdfs://192.168.195.132:9000/txt/score1/"));
		FileOutputFormat.setOutputPath(job, new Path("hdfs://192.168.195.132:9000/result/partscore"));

		if (!job.waitForCompletion(true))
			return;
	}
}
```