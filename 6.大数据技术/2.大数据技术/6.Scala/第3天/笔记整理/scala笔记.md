##    scala笔记

-----

### Array定长和变长

> 声明**定长数组**，可以指定长度
>
> 声明**变长数组**,可以追加新元素
>
> **apply()**：操作数据，下标从0开始,apply()可以省略
>
> **append()**：追加元素
>
> **take()**：**取出前n个**元素，并返回到一个新数组
>
> **takeRight()**：**取出后n个**元素，并返回到一个新数组
>
> **drop()**： **删除前n个**元素，并返回剩余元素
>
> **dropRight()**：**删除后n个**元素，并返回剩余元素
>
> **head**：返回集合的**头元素**,有别于take(1)
>
> **last**：返回集合的**尾元素**,有别于takeRight()
>
> **mkString()**： 将集合中的元素以**指定的分割符**返回为一个**字符串**
>
> **max**：最大值
>
> **min**：最小值
>
> **sum**：求和
>
> **filter { x => x>3 }**：根据指定的匿名函数规则实现**过滤**
>
> **distinct**：去重
>
> **exists { num => num>6 }**：根据指定的匿名函数**判断元素是否存在**,如果存在返回**true**,反之为**false**
>
> **intersect()**：取**交集**
>
> **union()**：取**并集**
>
> **diff()**：取**差集**
>
> **sortBy { x => x }**：按指定的匿名函数规则**升序排序**
>
> **map { x => x*2 }**：即map方法**改变元素形式**，不改变个数
>
> **foreach { x=> println(x) }**：按照指定的匿名函数**遍历操作数组**

```scala
/**
学习scala的collection类型，常用的包括:Array,List,Set,Map,Tuple(元组)
知识点
1.Array分定长和变长两种
2.immutable 定长
3.mutable 变长
**/
import scala.collection.mutable._

object Demo01 {
  println("Welcome to the Scala worksheet")       //> Welcome to the Scala worksheet
  
  //--声明定长数组,一经声明长度不可改变(不能追加新元素)
  val a1=Array(1,2,3,4)                           //> a1  : Array[Int] = Array(1, 2, 3, 4)
  
  //--声明定长数组，并指定长度
  val a2=new Array[Int](3)                        //> a2  : Array[Int] = Array(0, 0, 0)
  
  //--声明变长数组,可以追加新元素
  val a3=scala.collection.mutable.ArrayBuffer(1,2,3,4)
                                                  //> a3  : scala.collection.mutable.ArrayBuffer[Int] = ArrayBuffer(1, 2, 3, 4)
    
  val a4=ArrayBuffer(1,2,3)                       //> a4  : scala.collection.mutable.ArrayBuffer[Int] = ArrayBuffer(1, 2, 3)
    
  //--通过下标操作数组,下标从0开始,apply()可以省略
  a1.apply(0)                                     //> res0: Int = 1
  a1(0)                                           //> res1: Int = 1
  
  //--追加新元素
  a4.append(2,3,4)
  a4                                              //> res2: scala.collection.mutable.ArrayBuffer[Int] = ArrayBuffer(1, 2, 3, 2, 3,
                                                  //|  4)
 	//--取出前n个元素，并返回到一个新数组
  val r1=a1.take(2)                               //> r1  : Array[Int] = Array(1, 2)
  //--取出后n个元素，并返回到一个新数组
  val r2=a1.takeRight(2)                          //> r2  : Array[Int] = Array(3, 4)
  //--删除前n个元素，并返回剩余元素
  val r3=a1.drop(1)                               //> r3  : Array[Int] = Array(2, 3, 4)
  //--删除后n个元素，并返回剩余元素
  val r4=a1.dropRight(1)                          //> r4  : Array[Int] = Array(1, 2, 3)
  //--返回集合的头元素,有别于take(1)
  val r5=a1.head                                  //> r5  : Int = 1
  //--返回集合的尾元素,有别于takeRight(1)
  val r6=a1.last                                  //> r6  : Int = 4
  //--将集合中的元素以指定的分割符返回为一个字符串
  val r7=a1.mkString("|")                         //> r7  : String = 1|2|3|4
  
  //--分别最大值,最小值,求和
  val r8=a1.max                                   //> r8  : Int = 4
  val r9=a1.min                                   //> r9  : Int = 1
  val r10=a1.sum                                  //> r10  : Int = 10
  
  val a5=Array(2,1,6,3,5)                         //> a5  : Array[Int] = Array(2, 1, 6, 3, 5)
  //--计算a5的均值和a5的极差
  val r11=a5.sum/a5.length                        //> r11  : Int = 3
  val r12=a5.max-a5.min                           //> r12  : Int = 5
  
  //--根据指定的匿名函数规则实现过滤
  val r13=a5.filter { x => x>3 }                  //> r13  : Array[Int] = Array(6, 5)
  //--过滤出a5中偶数元素
  val r14=a5.filter{num=>num%2==0&&num>4}         //> r14  : Array[Int] = Array(6)
  
  val a6=Array("tom M 23","rose F 18","jim M 35") //> a6  : Array[String] = Array(tom M 23, rose F 18, jim M 35)
  
  //--过滤a6中男性数据
  val r15=a6.filter { line => line.contains("M") }//> r15  : Array[String] = Array(tom M 23, jim M 35)
  
  val r16=a6.filter { line =>line.split(" ")(1).equals("M")  }
                                                  //> r16  : Array[String] = Array(tom M 23, jim M 35)
  //--过滤a6中成年人数据
  val r17=a6.filter { line => line.split(" ")(2).toInt>=18 }
                                                  //> r17  : Array[String] = Array(tom M 23, rose F 18, jim M 35)
  val a7=Array(1,1,2,2,4,5)                       //> a7  : Array[Int] = Array(1, 1, 2, 2, 4, 5)
  //--去重
  val r18=a7.distinct                             //> r18  : Array[Int] = Array(1, 2, 4, 5)
  
  //--根据指定的匿名函数判断元素是否存在,如果存在返回true,反之为false
  val r19=a7.exists { num => num>6 }              //> r19  : Boolean = false
 	
 	//--判断a6中是否存在>30岁的男性数据
 	//--可以用于检验数据中是否存在异常数据
  val r20=a6.exists{line=>
  	val info=line.split(" ")
  	val age=info(2).toInt
  	val gender=info(1)
  	age>30&&gender.equals("M")
  }                                               //> r20  : Boolean = true
  
  
  val a8=Array(1,2,3)                             //> a8  : Array[Int] = Array(1, 2, 3)
  val a9=Array(3,4,5)                             //> a9  : Array[Int] = Array(3, 4, 5)
  
  //--取交集  可以用交集或差集比较文件之间相同或相异部分
  val r21=a8.intersect(a9)                        //> r21  : Array[Int] = Array(3)
  //--取并集
  val r22=a8.union(a9)                            //> r22  : Array[Int] = Array(1, 2, 3, 3, 4, 5)
  //--取差集,注意有方向
  val r23=a8.diff(a9)                             //> r23  : Array[Int] = Array(1, 2)
  val r24=a9.diff(a8)                             //> r24  : Array[Int] = Array(4, 5)
  
  
  val a10=Array(3,1,4,2,5)                        //> a10  : Array[Int] = Array(3, 1, 4, 2, 5)
  
  //--按指定的匿名函数规则升序排序
  val r25=a10.sortBy { num => num }               //> r25  : Array[Int] = Array(1, 2, 3, 4, 5)
  //--reverse反转 ,降序
  val r26=a10.sortBy { num => num }.reverse       //> r26  : Array[Int] = Array(5, 4, 3, 2, 1)
  //--降序,注意:负号是前缀操作符，需要加空格
  val r27=a10.sortBy { num => -num }              //> r27  : Array[Int] = Array(5, 4, 3, 2, 1)
  
  
  val a11=Array("tom 35","rose 18","jim 25","jary 40")
                                                  //> a11  : Array[String] = Array(tom 35, rose 18, jim 25, jary 40)
  
  //--按a11中的年龄做升序排序，并返回结果
  val r28=a11.sortBy { line => line.split(" ")(1).toInt }
                                                  //> r28  : Array[String] = Array(rose 18, jim 25, tom 35, jary 40)
  
  //--按a11中的姓名做降序排序,前缀操作符 - 只适用于数值型类型。
  
  val r29=a11.sortBy { line => line.split(" ")(0) }.reverse
                                                  //> r29  : Array[String] = Array(tom 35, rose 18, jim 25, jary 40)
  
  
  val a12=Array(1,2,3,4)                          //> a12  : Array[Int] = Array(1, 2, 3, 4)
  //--根据指定的匿名函数，将集合中的元素从一个形式映射到另一个形式,而且元素个数不变
  //--即map方法改变元素形式，不改变个数
  val r30=a12.map { num => num*2 }                //> r30  : Array[Int] = Array(2, 4, 6, 8)
  val r31=a12.map { num => num.toString }         //> r31  : Array[String] = Array(1, 2, 3, 4)
  
  val a13=Array("hello world","hello hadoop","hello spark")
                                                  //> a13  : Array[String] = Array(hello world, hello hadoop, hello spark)
  
  val r32=a13.map { line => line.split(" ") }     //> r32  : Array[Array[String]] = Array(Array(hello, world), Array(hello, hadoo
                                                  //| p), Array(hello, spark))
  //--操作a13,返回的新集合中:Array("hello","hello","hello")
  
  val r33=a13.map { line => line.split(" ")(0) }  //> r33  : Array[String] = Array(hello, hello, hello)
  
  val a14=Array("tom M 25","rose F 18","jary M 30" )
                                                  //> a14  : Array[String] = Array(tom M 25, rose F 18, jary M 30)
  
  //--计算a14中所有人的年龄之和
  //--Array[String:line]->Array[Int:age]->sum
  val r34=a14.map { line => line.split(" ")(2).toInt }.sum
                                                  //> r34  : Int = 73
  //--计算a14中男性年龄的平均值
  val r35=a14.filter { line => line.split(" ")(1).equals("M") }
                                                  //> r35  : Array[String] = Array(tom M 25, jary M 30)
  val r36=r35.map { line => line.split(" ")(2).toInt }.sum/r35.length
                                                  //> r36  : Int = 27
  //--计算a14,返回年龄最大的人的数据,返回:jary M 30
  val r37=a14.sortBy { line => -line.split(" ")(2).toInt }.take(1)
                                                  //> r37  : Array[String] = Array(jary M 30)
  //--按照指定的匿名函数遍历操作数组
  a14.foreach { line => println(line) }           //> tom M 25
                                                  //| rose F 18
                                                  //| jary M 30
}
```



### List的声明和调用

> 声明**定长**List，并赋值
>
> 声明**变长**List,并赋值
>
> **apply()**：通过下标操作List，apply()可以省略  
>
> **toArray**：List转Array
>
> **toList**：Array转List
>
> **.::(5)**：基于一个List,在头部追加新元素并返回新List

```scala
/**
学习List的声明和调用
总结Array和List 通用而且重要的方法:
1.take
2.takeRight
3.drop
4.dropRight
5.mkString(分隔符)
6.head
7.last
8.intersect(交集)
9.union
10.diff(差集)
11.max
12.min
13.sum
14.filter(过滤)
15.map(映射)
16.sortBy(排序)
17.exists(判断元素是否存在)
18.reverse(反转)
19.distinct
20.foreach
21.mapValues(只有Map类型有,针对Map的Value做映射)
22.flatMap
23.reduce
24.groupBy
以上,重点掌握:①map  ②filter  ③sortBy ④mapValues ⑤flatMap ⑥reduce ⑦groupBy
**/
object Demo02 {
  println("Welcome to the Scala worksheet")       //> Welcome to the Scala worksheet
  
  //--声明定长List,并赋值
  val l1=List(1,2,3,4)                            //> l1  : List[Int] = List(1, 2, 3, 4)
  
  //--声明变长List,并赋值
  val l2=scala.collection.mutable.ListBuffer(1,2,3)
                                                  //> l2  : scala.collection.mutable.ListBuffer[Int] = ListBuffer(1, 2, 3)
  
  //--通过下标操作List
  l1.apply(0)                                     //> res0: Int = 1
  l1(0)                                           //> res1: Int = 1
  
  //--List转Array
  val a1=l1.toArray                               //> a1  : Array[Int] = Array(1, 2, 3, 4)
  val l3=a1.toList                                //> l3  : List[Int] = List(1, 2, 3, 4)
  
  //--基于一个List,追加新元素并返回新List
  val r1=l1.::(5)                                 //> r1  : List[Int] = List(5, 1, 2, 3, 4)
}
```

### Set 和Map

> 声明**定长Set**
>
> 声明**变长Set**
>
> 声明**定长map**
>
> 声明**变长Map**,可以追加kv对
>
> **apply()**：通过key获取value，apply可以省略
>
> **get("tom").getOrElse(0)**：通过key获取value，如果key不存在，返回getOrElse()中的值
>
> **keys**：返回key的迭代器
>
> **values**：返回value的迭代器
>
> **filter{case(key,value)=>value>20}**：过滤数据
>
> **map{case(key,value)=>(key,value+10)}**：在原来的基础上 **value+10**
>
> **mapValues { value => value+10 }**：key值不变，**只改变value值**

```scala
/**
学习Set 和Map
**/
object Demo03 {
  println("Welcome to the Scala worksheet")       //> Welcome to the Scala worksheet
  
  //--声明定长Set
  val s1=Set(1,1,2,2,3)                           //> s1  : scala.collection.immutable.Set[Int] = Set(1, 2, 3)
  
  //--声明变长Set
  val s2=scala.collection.mutable.Set(1,2,3,3)    //> s2  : scala.collection.mutable.Set[Int] = Set(1, 2, 3)
  
  //--声明定长map
  val m1=Map("tom"->23,"rose"->18,"jim"->25)      //> m1  : scala.collection.immutable.Map[String,Int] = Map(tom -> 23, rose -> 18
                                                  //| , jim -> 25)
  //--声明变长Map,可以追加kv对
  val m2=scala.collection.mutable.Map("tom"->23,"rose"->18,"jim"->25)
                                                  //> m2  : scala.collection.mutable.Map[String,Int] = Map(jim -> 25, rose -> 18, 
                                                  //| tom -> 23)
  
  m2+=("jary"->30)                                //> res0: Demo03.m2.type = Map(jary -> 30, jim -> 25, rose -> 18, tom -> 23)
  
  //--通过key获取value
  m1.apply("tom")                                 //> res1: Int = 23
  m1("tom")                                       //> res2: Int = 23
  //--get通过key获取value,返回的类型是Option
  //--Option有两个子类，分别是Some和None
  //--如果通过get指定的key存在，则返回Some(value)
  //--如果key不存在,则返回None
  //--可以避免报错
  //--可以通过getOrElse取值,并指定返回为None的默认值
  m1.get("tom").getOrElse(0)                      //> res3: Int = 23
  m1.get("wefewoi").getOrElse(0)                  //> res4: Int = 0
  
  //--返回key的迭代器
  m1.keys                                         //> res5: Iterable[String] = Set(tom, rose, jim)
  //--返回value的迭代器
  m1.values                                       //> res6: Iterable[Int] = MapLike(23, 18, 25)
  
 	//--过滤年龄>20岁的数据
  val r1=m1.filter{case(key,value)=>value>20}     //> r1  : scala.collection.immutable.Map[String,Int] = Map(tom -> 23, jim -> 25)
                                                  //| 

	val r11=m1.filter{x=>x._2>20}             //> r11  : scala.collection.immutable.Map[String,Int] = Map(tom -> 23, jim -> 25
                                                  //| )
  //--操作m1,实现映射 :姓名不变,年龄加10岁
  val r2=m1.map{case(key,value)=>(key,value+10)}  //> r2  : scala.collection.immutable.Map[String,Int] = Map(tom -> 33, rose -> 28
                                                  //| , jim -> 35)
  
  //--如果在映射Map时，key不变，只改变value，可以使用mapValues 方法
  val r3=m1.mapValues { value => value+10 }       //> r3  :， scala.collection.immutable.Map[String,Int] = Map(tom -> 33, rose -> 28
                                                  //| , jim -> 35)
  
}
```

> **flatMap{ line => line.split(" ") } **：扁平化map方法，即改变元素形式，也改变了元素个数
>
> > 一般用于Spark读取文件后，把每行数据切分出单独的数据
>
> **reduce{(a,b)=>a*b}**：规约方法，底层是一种迭代方法
>
> **groupBy{x=>x._1} **：分组
>
> 统计**单词频次**,比如返回的结果(hello,3)(world,2)

```scala
object Demo05 {
  println("Welcome to the Scala worksheet")       //> Welcome to the Scala worksheet
  
  val l1=List("hello world","hello hadoop","hello hadoop")
                                                  //> l1  : List[String] = List(hello world, hello hadoop, hello hadoop)
  
  val r1=l1.map { line => line.split(" ") }       //> r1  : List[Array[String]] = List(Array(hello, world), Array(hello, hadoop), 
                                                  //| Array(hello, hadoop))
  //--扁平化map方法，即改变元素形式，也改变了元素个数
  //--一般用于Spark读取文件后，把每行数据切分出单独的数据
  val r2=l1.flatMap{ line => line.split(" ") }    //> r2  : List[String] = List(hello, world, hello, hadoop, hello, hadoop)
  
  val l2=List(1,2,3,4)                            //> l2  : List[Int] = List(1, 2, 3, 4)
  
  //--reduce方法规约方法,底层是一种迭代方法
  //--① a=1 b=2  a+b=3
  //--② a=3 b=3  a+b=6
  //--③ a=6 b=4  a+b=10
   val r3=l2.reduce{(a,b)=>a+b}                   //> r3  : Int = 10
   
   //--计算l2元素的阶乘结果
   val r4=l2.reduce{_*_}                          //> r4  : Int = 24
    
   val l3=List(5,8,1,3,10)                        //> l3  : List[Int] = List(5, 8, 1, 3, 10)
   
   //--要求用reduce方法返回l3中最大的数字,不能使用max方法
   //--① a=5  b=8  result=8
   //--② a=8  b=1  result=8
   //--③ a=8  b=3  result=8
   val r5=l3.reduce{(a,b)=>if(a>b) a else b}      //> r5  : Int = 10
   
   val l4=List("hello","world","hello","hello","world")
                                                  //> l4  : List[String] = List(hello, world, hello, hello, world)
   
   //--按指定的匿名函数规则分组
   val r6=l4.groupBy { word => word }             //> r6  : scala.collection.immutable.Map[String,List[String]] = Map(world -> Lis
                                                  //| t(world, world), hello -> List(hello, hello, hello))
   
   val r7=l4.map { word =>(word,1) }              //> r7  : List[(String, Int)] = List((hello,1), (world,1), (hello,1), (hello,1),
                                                  //|  (world,1))
   
   //--要求操作r7,List[(String,Int)]->List[String:word]
   val r8=r7.map{x=>x._1}                         //> r8  : List[String] = List(hello, world, hello, hello, world)
   
   //--要求操作r7,按单词分组
   val r9=r7.groupBy{x=>x._1}                     //> r9  : scala.collection.immutable.Map[String,List[(String, Int)]] = Map(world
                                                  //|  -> List((world,1), (world,1)), hello -> List((hello,1), (hello,1), (hello,1
                                                  //| )))
   val l5=List(("tom",25),("rose",18),("jim",30),("jary",20))
                                                  //> l5  : List[(String, Int)] = List((tom,25), (rose,18), (jim,30), (jary,20))
                                                  //| 
   
   //--要求计算l5中所有人的年龄之和
   //--List[(String, Int)]->List[Int].sum
   val r10=l5.map{x=>x._2}.sum                    //> r10  : Int = 93
   
   //--返回l5中年龄最大的前两个人的数据
   val r11=l5.sortBy{x=> -x._2}.take(2)           //> r11  : List[(String, Int)] = List((jim,30), (tom,25))
   
   
   
   val l6=List("hello world","hello hadoop","hello world")
                                                  //> l6  : List[String] = List(hello world, hello hadoop, hello world)
   
   //--统计l6中单词频次,比如返回的结果(hello,3)(world,2)...
   
   val r12=l6.flatMap { line => line.split(" ") }
   					 .groupBy { word => word }
             .mapValues { list => list.length }
             .toArray                             //> r12  : Array[(String, Int)] = Array((hadoop,1), (world,2), (hello,3))
   
   
   val r13=l6.flatMap { line => line.split(" ") }
   					 .groupBy { word => word }
             .map{case(key,value)=>(key,value.length)}
             .toArray                             //> r13  : Array[(String, Int)] = Array((hadoop,1), (world,2), (hello,3))
             
             
             
   val i1=Item("huawei",2999)                     //> i1  : Item = Item(huawei,2999.0)
   val i2=Item                                    //> i2  : Item.type = Item

}
```



### Tuple(元组)

> **声明一个元组**
>
> **t1._1 **：取出元组中第一个元素

```scala
/**
学习Tuple(元组)类型，这种类型是最灵活，最常用的类型
**/
object Demo04 {
  println("Welcome to the Scala worksheet")       //> Welcome to the Scala worksheet
 	
 	//--声明一个包含4个元素的元组,用()声明的数据就是一个元组
 	val t1=(1,2,3,4)                          //> t1  : (Int, Int, Int, Int) = (1,2,3,4)
 	
 	val t2=(1,"hello",Array(1,2,3),List(4,5,6))
                                                  //> t2  : (Int, String, Array[Int], List[Int]) = (1,hello,Array(1, 2, 3),List(4,
                                                  //|  5, 6))
  //--取出元组中第一个元素
  t1._1                                           //> res0: Int = 1
  
  //--取出t2中的hello
  t2._2                                           //> res1: String = hello
  
  val t3=(1,(2,3,4),((5,6),(7,8),Array(9,10)))    //> t3  : (Int, (Int, Int, Int), ((Int, Int), (Int, Int), Array[Int])) = (1,(2,3
                                                  //| ,4),((5,6),(7,8),Array(9, 10)))
  
  //--取出数字2
  t3._2._1                                        //> res2: Int = 2
  
  //--取出数字5
  t3._3._1._1                                     //> res3: Int = 5
  
  //--取出数字10
  t3._3._3(1)                                     //> res4: Int = 10
}
```

