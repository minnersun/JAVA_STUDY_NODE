## Scala代码整理

---

### 1.计算第二列的均值

#### average.txt

```tex
1 16
2 74
3 51
4 35
5 44
6 95
7 5
8 29
10 60
11 13
12 99
13 7
14 26
```

#### cn.tedu.average

##### Driver.scala

```scala
package cn.tedu.average

import org.apache.spark.SparkConf
import org.apache.spark.SparkContext

object Driver {
  
  def main(args: Array[String]): Unit = {
    
    val conf=new SparkConf().setMaster("local").setAppName("average")
    
    val sc=new SparkContext(conf)
    
    val data=sc.textFile("c://data/average.txt",2)
    
    //--计算第二列的均值。结果打印到控制台
    //--RDD[String:line]->RDD[Int:第二列数字]->sum/count
    val r1=data.map { line => line.split(" ")(1).toInt }
    val average=r1.sum/r1.count
    
    println(average)
  }
}
```



### 2.1返回男性身高最大数据

### 2.2返回女性身高最大的前两行数据

#### MaxMin.txt

```tex
1 M 174
2 F 165
3 M 172
4 M 180
5 F 160
6 F 162
7 M 172
8 M 191
9 F 175
10 F 167
```

#### cn.tedu.maxmin

##### Driver.scala

```scala
package cn.tedu.maxmin

import org.apache.spark.SparkConf
import org.apache.spark.SparkContext

/**
 * 返回男性(M)身高最大值数据
 * 返回 :191
 */
object Driver {
  
  def main(args: Array[String]): Unit = {
    val conf=new SparkConf().setMaster("local").setAppName("maxmin")
    
    val sc=new SparkContext(conf)
    
    val data=sc.textFile("c://data/MaxMin.txt", 2)
    
    //--RDD[String:line]->RDD[String:男性的line]->RDD[Int:男性身高].max
    val r1=data.filter { line => line.split(" ")(1).equals("M") }
    
    val r2=r1.map { line => line.split(" ")(2).toInt }.max
    
    println(r2)
  }
}
```

##### Driver02.scala

```scala
package cn.tedu.maxmin

import org.apache.spark.SparkConf
import org.apache.spark.SparkContext

/**
 * 处理MaxMin.txt,返回女性身高最大的前两行数据
 */
object Driver02 {
  
  def main(args: Array[String]): Unit = {
    val conf=new SparkConf().setMaster("local").setAppName("maxmin")
    
    val sc=new SparkContext(conf)
    
    val data=sc.textFile("c://data/MaxMin.txt", 2)
    
    val r1=data.filter { line => line.split(" ")(1).equals("F") }
               .sortBy{line=> -line.split(" ")(2).toInt}
               .take(2)
               
    r1.foreach{println}          
  }
}
```



### 3.返回单词频次出现最高的前3项单词

#### topk.txt

```tex
hello world bye world
hello hadoop bye hadoop
hello world java web
hadoop scala java hive
hadoop hive redis hbase
hello hbase java redis
```

#### cn.tedu.topk

##### Driver.scala

```scala
package cn.tedu.topk

import org.apache.spark.SparkConf
import org.apache.spark.SparkContext

/**
 * 处理topk.txt,返回单词频次出现最高的前3项单词，
 * 返回的结果形式:
 * (hello,10)
 * (hive,8)
 * (hadoop,6)
 */
object Driver {
  
  def main(args: Array[String]): Unit = {
    val conf=new SparkConf().setMaster("local").setAppName("topk")
    
    val sc=new SparkContext(conf)
    
    val data=sc.textFile("c://data/topk.txt", 2)
    
    val wordcount=data.flatMap {_.split(" ")}.map {(_,1)}.reduceByKey{_+_}
    
    val top3=wordcount.sortBy{case(word,count)=> -count}.take(3)
    
    top3.foreach{println}
    
  }
}
```



### 4.统计每个单词出现的次数

#### word.txt

```tex
hello scala
hello spark
hello world
```

#### cn.tedu.wordcount

##### Driver.scala

```scala
package cn.tedu.wordcount

import org.apache.spark.SparkConf
import org.apache.spark.SparkContext

object Driver {
  
  def main(args: Array[String]): Unit = {
    
    //--创建Spark的环境参数对象,设置运行模式和jobid
    //--调试或测试一般用local
    val conf=new SparkConf().setMaster("spark://hadoop01:7077").setAppName("wordcount")
    
    //--创建Spark的上下文对象,通过此对象创建RDD以及提交job任务
    val sc=new SparkContext(conf)
    
    val data=sc.textFile("hdfs://hadoop01:9000/3.txt", 2)
    
    val result=data.flatMap { _.split(" ") }.map { (_,1) }.reduceByKey{_+_}
    
    result.saveAsTextFile("hdfs://hadoop01:9000/result02")
  }
}
```