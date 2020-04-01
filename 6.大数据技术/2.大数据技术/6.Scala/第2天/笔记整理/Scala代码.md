## Scala代码

-------

### 函数式代码

#### demo1	-设置默认值

```scala
package cn.tedu.function

object function {

  def main(args: Array[String]): Unit = {

    //    def test(x: Int, y: Int, f: => Double) {
    //      println(f)
    //      println(x + y)
    //    }
    //
    //    test(2, 3, Math.random)

    // 求a的b次方
    // 定义函数的时候可以给参数一个默认值
    def pow(a: Double, b: Double = 1) = Math.pow(a, b)

    println(pow(5))

    // 求价格对应的折扣之后的价格
    def realPrice(off: Double = 1.0, price: Double) = price * off
    // 参数在赋值的时候是顺次赋值
    // 如果给指定参数赋值，需要指定参数名
    println(realPrice(price = 800))
  }
}
```

#### demo2	-设置全局默认值 

```scala
package cn.tedu.function

object FunctionDemo2 {

  def main(args: Array[String]): Unit = {

    implicit val off = 1.0

    // 卖货
    def realPrice(price: Double)(implicit off: Double) = price * off

    // 进货
    def buy(price: Double, num: Double)(implicit off: Double) = price * num * off

    println(realPrice(25)(0.8))
    println(buy(5, 10)(0.9))
  }
}
```

#### demo3	-递归

```scala
package cn.tedu.function

object FunctionDemo3 {

  def main(args: Array[String]): Unit = {
    // 如果是内嵌函数，不允许重载
    //    def add(x: Int, y: Int) = x + y
    //    def add(x: Int, y: Int, z: Int) = x + y + z

    // 函数的递归
    def sum(n: Int): Int = {
      if (n <= 0) 0
      else n + sum(n - 1)
    }

    println(sum(10))

    // 斐波那契数列
    // 0 1 1 2 3 5 8 13 21...
    def fab(n: Int): Int = {
      if (n <= 1) 0
      else if (n == 2) 1
      else fab(n - 1) + fab(n - 2)
    }

  }

  def add(x: Int, y: Int) = x + y
  def add(x: Int, y: Int, z: Int) = x + y + z
}
```



### object

#### person	-伴生对象

> object	中的属性和方法都是静态的
>
> class	中一般定义私有方法
>
> @BeanProperty	注解的方式实现get，set方法

```scala
package cn.tedu.objectx

import scala.beans.BeanProperty

// 用object定义的结构称之为伴生对象
object PersonDemo {

  def main(args: Array[String]): Unit = {

    var p = new Person
    //    println(p.name)
    //    p.name = "Amy"
    //    p.age = -15
    println(p.getName)

  }

}

class Person {

  // 在Scala中，要求变量在定义的时候必须初始化
  // 在Scala中，不建议属性值给null
  // _不表示值，而是在初始化这个对象的时候系统会给name一个默认值
  // 在Scala中，如果不指定，权限修饰符默认是public
  @BeanProperty var name: String = _
  @BeanProperty var age: Int = _

  def eat = println("吃饭~~~")

}
```



#### demo1	-包对象

```scala
// 声明包的方式和Java中相比会更加的灵活
// 包的声明顺序决定了包的嵌套关系
package cn
package tedu
package objectx

import cn.tedu.objectx.o.a.Cat

object ObjectDemo {

  def main(args: Array[String]): Unit = {

    // 通过包来调用函数
    demo.test

    var c = new Cat

  }

  def test() = println("lalala~~~")

}

// 为了允许在包中声明函数，提供了一类特殊的包 - 包对象
package object demo {
  def test() = println("running~~~")
}

package o {
  package a {
    class Cat {

      def play() = ObjectDemo.test
      
    }
  }
}
```

#### demo2	-类的别名，default修饰

```scala
package cn.tedu.objectx

// _表示通配
// 导入一个包下的多个类
//import java.util.{ List, Set, HashSet }
// 在导包的时候给这个类来起别名
import java.util.{ Date => UtilDate }
import java.sql.{ Date => SqlDate }

object ObjectDemo2 {

  def main(args: Array[String]): Unit = {

  }

}

package a1 {
  // 类中可以定义属性、方法
  // 属性：静态和非静态
  // 方法：构造方法、静态方法和成员方法
  class Driver {
    // 在类中定义的属性和方法默认是public并且是非静态的
    protected var name: String = _
    // 表示这个属性在objectx包中可用
    private[objectx] var age: Int = _

    def dtest() {
      println(name)
      import cn.tedu.objectx.a2._
      var t = new Test
    }

  }

  class YoungDriver extends Driver {

    def test() {
      println(this.name)
    }
  }
}

package a2 {

  class Test {

    def test() {
      var d = new a1.Driver
      //      d.age
    }
  }
}
```

#### demo3	-伴生类(非静态)，伴生对象（静态）

```scala
package cn.tedu.objectx

object ObjectDemo3 {

  def main(args: Array[String]): Unit = {

    import cn.tedu.objectx.o3.Pet

    var p = new Pet
  }
}

package o3 {

  // 在一个类中可以声明属性和方法
  // 属性：静态和非静态
  // 方法：构造、静态和成员
  // 在class中只能写非静态的属性和方法
  // 在Scala中，所有的静态都必须定义到object中
  // class和object在编译完成之后会编译到一个类中
  // 利用这种方法来完成静态的定义
  // object Pet称之为class Pet的伴生对象
  // class Pet称之为object Pet的伴生类
  // 将非静态方法和属性定义在伴生类中
  // 将静态的方法和属性定义在伴生对象中
  class Pet {

    private var name: String = _

    def getName = this.name
    def setName(name: String) = this.name = name
  }
  // object中定义的都是静态方法
  object Pet {
    def sleep = println("sleeping~~~")
  }
}
```



#### demo4	-构造方法的重载

```scala
package cn.tedu.objectx

object ObjectDemo4 {

  def main(args: Array[String]): Unit = {

    import o4.Student

    var s: Student = new Student("g01c01", "Sam")
    //    println(s.id)
    println(s)

  }

}

package o4 {

  // 定义在类名后边的构造方法称之为主构造器
  class Student() {

    private var id: String = _
    private var name: String = _
    private var age: Int = _
    private var gender: String = _
    private var addr: String = _

    // 在一个类中，除了属性和函数之外的代码默认是放在主构造器中
    if (this.name == null)
      this.name = ""
    this.age = 10

    // 重载 - 辅助构造器
    // 在辅助构造器中需要显式的调用主构造器
    def this(id: String) = {
      this()
      this.id = id
    }
    // 提供一个含参构造，对id和name赋值
    def this(id: String, name: String) = {
      this()
      this.id = id
      this.name = name
    }

  }

  // 如果在主构造器定义参数，那么此时主构造器中的参数默认就是Driver中的私有属性
  // 在Scala中，函数的参数列中的参数默认是val定义的
  class Driver(id: String, name: String, age: Int) {

    //    private val id:String = _
    if (age <= 18) throw new IllegalArgumentException

    def getId = id

    def test() = {
      var c: Cat = new Cat("瞄~")
    }

  }

  // 表示将主构造器私有，只对外提供辅助构造器
  // 养一只猫，起名，允许改名
  class Cat private () {

    private var name: String = _

    def this(name: String) {
      this()
      this.name = name
    }

    def setName(name: String) = this.name = name

  }

  // 定义类表示矩形
  class Rectangle(a: Double, b: Double) {}

}
```



#### demo5	-类的向上造型与继承

```scala
package cn.tedu.objectx

object ObjectDemo5 {

  def main(args: Array[String]): Unit = {

    import o5._
    // 类型的自动推导
    var p: Pet = new Dog
    // 强制转换为Dog类型
    var d = p.asInstanceOf[Dog]

  }

}

package o5 {

  class Pet {
    val age: Int = 8
    def eat() = println("吃东西中~~~")
  }

  //  class Dog (name:String)extends Pet {}
  class Dog extends Pet {

    // 父子类中不能出现同名的变量
    // 父子类中可以出现同名常量，但是子类中的同名常量必须用override
    override val age: Int = 5

    // 在Scala中，如果一个方法是被重写的方法，那么这个方法需要显式的声明为override
    override def eat() = println("这只狗在吃XXXX~~~")

  }
}
```



#### singleton	-单例模式

```scala
package cn.tedu.objectx

object Singleton {

  def main(args: Array[String]): Unit = {

    var a: A = A.getInstance
    var b: B = B.getInstance 
  }
}
// 单例
// 在Scala中，如果需要定义单例利用伴生对象来定义
// 懒汉式
class A private () {}
object A {
  private var a: A = _
  def getInstance = {
    if (a == null)
      a = new A
    a
  }
}
// 饿汉式
class B private () {}
object B {
  private var b: B = new B
  def getInstance = b
}
```





### abstract，trait

#### demo1	-abstract

```scala
package cn.tedu.objectx

object AbstractDemo {

  def main(args: Array[String]): Unit = {

    import abst._

    var p = new Pet {}
  }
}

package abst {

  abstract class Pet {}

  abstract class Animal {

    def sleep = println("sleeping~~~")

    //    def eat(): Unit
    def eat
  }

  class Dog extends Animal {

    override def eat = println("eating~~~")
  }
}
```

#### demo2	-trait	-extends，with

```scala
package cn.tedu.objectx

object Interfacex {

  def main(args: Array[String]): Unit = {

    import in._

    //    var a = new Animal{}

    abstract class Parrot {
    }

    // 可以给对象混入特质
    var p1 = new Parrot with Animal {
      override def eat = println("eating")
    }

    trait Bird {}
    // 抽象类混入特质之后可以声明对象，前提是抽象类中没有抽象方法
    var p2 = new Parrot with Bird

  }

}

package in {

  // Scala中没有接口，不支持interface关键字
  // 如果在Scala中，需要一个结构来进行约束，那么使用的trait - 特质
  // trait类似于Java中的接口，但是不一样
  // 特质中允许定义变量和实体函数
  trait Animal {

    var name: String = _

    def eat

    def sleep = println("sleeping~~~")

    //    def test = name = "~~~"

  }

  // 如果这个类没有显式声明父类，那么需要用extends继承特质
  class Dog extends Animal {

    override def eat = println("eating~~~")

  }

  class Cat {}

  trait Pet {}

  // 如果一个类已经显式的声明父类，那么需要用with混入特质
  // 如果需要混入多个特质，那么需要挨个with
  class 布偶 extends Cat with Animal with Pet {
    override def eat = println("eating~~~")
  }
}
```



#### demo3	-tracit	方法要不一样

```scala
package cn.tedu.objectx

object Interfacex2 {

  def main(args: Array[String]): Unit = {

    import in2._

    class Test {}
    //  如果混入了多个特质，那么特质之间的方法要求不一样
    var t = new Test with A with C with B
    t.test
  }

}

package in2 {

  trait A {
    def test = println("A")
  }
  trait B extends A {
    override def test = println("B")
  }
  trait C extends B {
    override def test = println("C")
  }

}
```



### API

#### demo1	-Exception

```scala
package cn.tedu.api

import java.io.IOException

object ExceptionDemo {

  // 在Scala中不关注异常，所以也可以不用抛出异常
  @throws[IOException]
  def test(i: Int, j: Int) = {
    if (j == 0)
      throw new IOException
    i / j.toDouble
  }

  def main(args: Array[String]): Unit = {

    try {
      var r = test(3, 0)
    } catch {
      // 如果需要捕获不同的异常，只需要添加case即可
      // 小异常要放在大异常的前边
      case e: NullPointerException     => e.printStackTrace()
      case e: IllegalArgumentException => println(e.getMessage)
      case e: Exception                => e.printStackTrace()
    }
  }

}
```

#### demo2	-Collection

```scala
package cn.tedu.api

import java.io.IOException

object ExceptionDemo {

  // 在Scala中不关注异常，所以也可以不用抛出异常
  @throws[IOException]
  def test(i: Int, j: Int) = {
    if (j == 0)
      throw new IOException
    i / j.toDouble
  }

  def main(args: Array[String]): Unit = {

    try {
      var r = test(3, 0)
    } catch {
      // 如果需要捕获不同的异常，只需要添加case即可
      // 小异常要放在大异常的前边
      case e: NullPointerException     => e.printStackTrace()
      case e: IllegalArgumentException => println(e.getMessage)
      case e: Exception                => e.printStackTrace()
    }
  }

}
```

#### demo3	-Collection

```java
package cn.tedu.api

object CollectionDemo2 {

  def main(args: Array[String]): Unit = {

    // 创建数组
    //    var arr1: Array[Int] = new Array[Int](5)
    // 如果使用这种方法创建数组，底层使用的是apply方法
    //    var arr2: Array[Int] = Array[Int](5, 2, 3)

    // 创建List
    //    var list1: List[String] = List[String]("a", "b", "c")
    //    var list2: List[String] = new List[String](5)
    // 表示创建了一个list
    //    var list2: List[String] = "a" :: "b" :: "c" :: Nil
    //    println(list2)

    var list: List[Int] = 1 :: 2 :: 3 :: 4 :: 5 :: Nil
    // 遍历list
    // 方式一：通过下标遍历
    //    for(i <- 0 until list.size)
    //      println(list(i))
    // 方式二：增强for循环
    //    for (i <- list)
    //      println(i)
    // 方式三：foreach
    //    list.foreach { (x: Int) => println(x) }
    //  list.foreach { x => println(x) }
    //    list.foreach { println(_) }
    list.foreach { println }

  }
}
```

#### demo4	-list

```scala
package cn.tedu.api

object ListDemo {

  def main(args: Array[String]): Unit = {

    //    var list = List[Int](3, 5, 2, 7, 1, 8, 9)
    // 将集合中的元素一一取出按照指定的规则进行映射
    //    var result = list.map { x => x * x }
    // 获取集合中大于5的元素
    //    var result = list.filter { x => x > 5 }

    // 在列表头部添加一个元素，返回一个新的list
    //    var result = list.+:(5)
    //    var result = (5) +: list
    // 翻转集合
    //    var result = list.reverse

    // 将结合中的元素拷贝到一个数组中
    //    var arr = new Array[Int](list.size)
    //    list.copyToArray(arr)

    //    var list = List[String]("hello", "hadoop", "hello", "zookeeper", "hello", "scala")
    //    var result = list.groupBy { x => x }
    //    result.foreach(println)

    var list = List[Int](3, 5, 2, 7, 1, 8, 9)
    // 将list中的所有的元素进行求和
    //    var result = list.reduce((x, y) => x + y)
    var result = list.reduce(_ + _)
    println(result)
  }
}
```