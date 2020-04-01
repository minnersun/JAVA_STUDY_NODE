## spark编程练习

----

### 中位数

#### cn.teud.median

##### Driver.scala

```scala
package cn.teud.median

import org.apache.spark.SparkConf
import org.apache.spark.SparkContext

/**
 * 找出median.txt中，数据集的中位数。
 * 只需要考虑数据集个数为奇数个情况
 */
object Driver {
  
  def main(args: Array[String]): Unit = {
    val conf=new SparkConf().setMaster("local").setAppName("median")
    
    val sc=new SparkContext(conf)
    
    val data=sc.textFile("c://data/median.txt")
    
    //--3 1 7 5 9 -> 1 3 5 7 9 ->中位数的位置=(n+1)/2 =3 ->take(3).last
    //--RDD[String:line]->RDD[Int:num]
    val r1=data.flatMap { line => line.split(" ") }
               .map { num => num.toInt }
               
    val r2=r1.sortBy{num=>num}   
    
    val medianPos=(r2.count+1)/2
    
    val median=r2.take(medianPos.toInt).last
    
    println(median)
               
  }
}
```

### 倒排索引

#### cn.tedu.inverted

##### Driver.scala

```scala
package cn.tedu.inverted

import org.apache.spark.SparkConf
import org.apache.spark.SparkContext

object Driver {
  
  def main(args: Array[String]): Unit = {
    
    val conf=new SparkConf().setMaster("local").setAppName("inverted")
    
    val sc=new SparkContext(conf)
    
    //--将指定目录下所有文件数据读取到一个RDD中
    //--RDD[(filePath,fileText)]
    //--课后作业:完成倒排索引表
    //--(scala,doc3)  (hadoop,doc1,doc2)
    //--提示:文档名从filePath获取
    //--单词从fileText中获取，先按\r\n 把每行数据切出来，在按空格将每行数据的单词切分
    val data=sc.wholeTextFiles("c://data/inverted/*")
    
    //--RDD[(filePath,fileText)]->RDD[(fileName,fileText)]
    val r1=data.map{case(filePath,fileText)=>
      val fileName=filePath.split("/").last.dropRight(4)
      (fileName,fileText)
    }
    
   //--RDD[(fileName,fileText)]->RDD[(word,fileName)]
   val r2=r1.flatMap{case(fileName,fileText)=>
       fileText.split("\r\n").flatMap{ line => line.split(" ") }
               .map {word =>(word,fileName) } 
   }
   
   val r3=r2.groupByKey.map { case(word,buffer) =>
       (word,buffer.toList.distinct.mkString(",")) }
   
   r3.foreach{println}

  }
}
```

### 二次排序

#### cn.tedu.ssort

##### Ssort.scala

```scala
package cn.tedu.ssort
/**
 * 用于封装文件中两列数据，并实现二次排序的比较规则
 */
class Ssort(val col1:String, val col2:Int) extends Ordered[Ssort] with Serializable{
  
  /*
   * compare用于定义排序比较规则
   */
  def compare(that: Ssort): Int = {
    //--先按第一列做升序排序
    val result=this.col1.compareTo(that.col1)
    
    if(result==0){
      //--再按第二列做降序排序
      that.col2.compareTo(this.col2)
      
    }else{
      result
    }
  }
}
```

##### Driver.scala

```scala
package cn.tedu.ssort

import org.apache.spark.SparkConf
import org.apache.spark.SparkContext

object Driver {
  
  def main(args: Array[String]): Unit = {
    
    val conf=new SparkConf().setMaster("local").setAppName("ssort")
    
    val sc=new SparkContext(conf)
    
    val data=sc.textFile("c://data/ssort.txt")
    
    //--RDD[String:line]->RDD[(Ssort(col1,col2),line)]->sortByKey,
    //--会按Ssort对象中的compare排序
    val r1=data.map { line =>
      val info=line.split(" ") 
      val col1=info(0)
      val col2=info(1).toInt
      (new Ssort(col1,col2),line)
    }
    
    //--true表示按Ssort对象中Compare的规则排序
    //--false表示按Compare中的规则反排序
    val r2=r1.sortByKey(true).map{x=>x._2}
    
    r2.foreach{println}
  }
}
```



### 思考题解答

####  找出40亿个数字中，int没有的数，占用内存不能超过2G

```java
package cn.tedu.ssort;

import java.util.BitSet;

import org.junit.Test;

public class TestDemo {
	
	@Test
	public void testBitSet(){
		//--创建比特数组
		//--1001 1001
		//--创建一个43亿个大小比特数组，内存占用512MB
		//--如何找出没有出现过的数字
	    //--文件中:0 3 4 7   给定0~10范围，再次遍历BitSet,bs.get(8)=false
		BitSet bs=new BitSet(Integer.MAX_VALUE);
		bs.set(0);
		bs.set(3);
		bs.set(4);
		bs.set(7);
		for(int i=0;i<bs.length();i++){
			System.out.println(bs.get(i));
		}
	}
}

```