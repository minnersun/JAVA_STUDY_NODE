## scala课程目录

-------

### 第1天

#### 关键字

> package, import, class, **object**, **trait**, extends, **with**, **type**, for，                                               
>
> private, protected, abstract, **sealed**, final，**implicit**,**lazy**,**override**，
>
> try, catch, finally, throw ，if, else, **match**, **case**, do, while, return, **yield**，
>
> **def**, **val**, **var** ，this, super，new，true, false, null

##### import

> 可以出现在任意的位置

##### package

> 定义包，可以定义子包
>
> 包名要求尽量简短

##### object

> object中定义的属性和方法都是静态的

##### trait

> 相当于java中的接口，允许定义实体类

##### with

> 一个类已经继承一个类，需要用with，混入特质
>
> 也可以给对象混入特质
>
> 如果混入多个特质，要求方法名不同

##### implicit

> 定义一个隐藏函数

##### override

> 重写父类方法

##### try，catch，case，finally

> 处理异常时，可以结合使用

##### yield

> 将计算结果返回到一个集合中

##### def

> 定义函数

##### val

> 定义常量

##### var

> 定义变量



#### 入门

##### demo1	-入门案例

```scala
package cn.tedu.scala

object Demo {

  // public static void main(String[] args){}
  // 在Scala中，所有的方法和类只要不指定默认就是public的，所以导致在Scala中没有public关键字
  // 使用def来定义函数
  // Scala中不只是static关键字，在object中定义函数默认都是static的
  // Scala中不支持void关键字，如果一个函数返回值为空，则返回值定义为Unit类型
  // Scala中注重参数而不注重类型，所以在定义参数的时候是参数名在前，类型在后
  def main(args: Array[String]): Unit = {

    // Scala中不推荐使用;
    // 默认一行就是一条语句，除非这一行有多条语句，那么语句之间才会使用;间隔
    println("hello world!!!")
    println("hi scala")

  }

}

// class中定义的函数全部都是非静态的
class Test {

  def test(): Unit = {}

}
```

##### demo2	-变量名

```scala
package cn.tedu.scala

object VarNameDemo {

  def main(args: Array[String]): Unit = {

    // 标识符的命名规则
    // 1. 可以使用字母、数字、_以及$
    // 2. _不能结尾
    // 3. 数字不能开头
    var a2_$: String = "abc"
    // 4. 允许使用其他符号作为标识符
    // 如果使用了符号作为标识符，那么整个标识符只能使用符号
    var +-*/ = 5
    // 5. 用于``作为标识符，``可以使用任何字符
    var `private` = "abc"
    // 注意：标识符不建议全部使用_
    //    var __ = "abc"
    //    var str = __

    var name = "Amy"
    var age = 15
    println("name=" + name + ", age=" + age)
    // 支持占位符写法
    println(s"name=${name}, age=${age}")

  }

}
```

##### demo3	-声明变量

```scala
package cn.tedu.scala

object VariableDemo {

  def main(args: Array[String]): Unit = {

    // 在Scala中，使用的是var关键字来定义变量
    // 变量定义：
    // 显式声明 --- var 变量名:类型 = 值
    var str: String = "abc"
    var i: Int = 54
    // 隐式声明 --- var 变量名 = 变量值
    // Scala在底层会自动推导变量的类型
    // Scala也是一门强类型语言，变量在定义好之后类型不能发生改变
    var d = 3.25
    d = 2.54

    // Scala中变量在定义的时候必须初始化
    //    var c:Char
    //    c = 'a'

    // 通过val来定义常量
    val c = 'a'
    //    c = 'd'
  }

  def test():Nothing = {
    throw new Exception();
  }
  
}
```

##### demo4	-类型转化

```scala
package cn.tedu.scala

object TypeChangeDemo {

  def main(args: Array[String]): Unit = {

    // 隐式转换
    // 方式一：小类型转化为大类型
    var i = 5
    var d: Double = i
    // 方式二：自定义隐式转换规则
    // 如果要自定义隐式转换的规则，需要将规则以函数的形式来体现
    // implicit是隐藏的意思
    // 定义一个隐藏函数，这个函数在使用的时候不需要调用，会自动转化为指定规则
    implicit def test(dou: Double): Int = {
      return dou.toInt
    }
    // Scala在运行的时候发现不符合自动转化的规则，就会去当前作用域内去检查是否有自定义的转化规则
    // 如果有自定义的转化规则，会自动调用这个转化规则
    // 下面的语句等价于：var id:Int = test(2.56)
    var id: Int = 2.56

    // 显式转换
    // 方式一：调用toXXX来转换
    var i2: Int = 5.23.toInt
    var i3: Int = "132".toInt
    println(i3)
    // 方式二：向上造型的转换
    var o: Object = "abc"
    var str: String = o.asInstanceOf[String]
    println(str)
	}
}
```

##### demo5	-运算规则

```scala
package cn.tedu.scala

object OperatorDemo {

  def main(args: Array[String]): Unit = {

    // 1. Scala中不支持++ --
    // Scala是一门完全面向对象的语言
    // 所以所有的运算符本质上是一个函数
    var i = 5
    var j = 7
    var sum1 = i + j
    // 等价于
    var sum2 = i.+(j)

    println(i.&(j))

    // 如果使用简略形式，那么这个时候考虑运算符的优先级
    var x = 2 + 3 * 5
    println(x)
    // 如果使用完全写法，那么就按照方法的调用顺序来执行
    var y = 2.+(3).*(5)
    println(y)
    var z = 2.+(3) * 5
    println(z)

    var a = 3
    var b = 5
    // a=b是一个赋值运算，赋值运算是没有计算结果
    // 所以c的值就是没有值，就需要将c的类型定义为Unit类型
    var c: Unit = a = b
    println(c)
  }
}
```

##### demo6	-if	案例

```scala
package cn.tedu.scala

import scala.io.StdIn

object IfDemo {

  def main(args: Array[String]): Unit = {

    // if(boolean) else if...

    // 输入一个数字，判断这个数字是否是一个奇数
    // Standard Input
    var i = StdIn.readInt()

    // if-else执行完成之后没有结果
    // result的类型是Unit
    var result: Unit = if (i % 2 == 1) {
      println("是一个奇数")
    } else {
      println("不是一个奇数")
    }

    // 输入分数，打印登记
    var level: String = if (i > 90) "A"
    else if (i > 80) "B"
    else if (i > 60) "C"
    else "D"

    // 在返回结果的类型不一致的前提下，变量的类型是？
    var r2: Any = if (i > 5)
      "yes" // String
    else
      println("不大于5") // Unit

    // 如果这个变量的类型声明为Unit，无论后边写什么都会被忽略
    //    var x: Unit = "abc"

    // 返回String的前提得是满足if条件
    // 如果不满足if条件，r3此时就没有结果
    var r3: Any = if (i > 10) "yes"

    println(r2)

  }
}
```

##### demo7	-For	案例

###### demo1

```scala
package cn.tedu.scala

object ForDemo {

  def main(args: Array[String]): Unit = {

    // 循环结构只要不指定，返回值默认就是Unit类型
    // while
    // 打印5个hello
    //    var i = 0;
    //    var result: Unit = while (i < 5) {
    //      println("hello")
    //      i += 1
    //    }

    // Scala中的for循环结构产生了变化
    // for(to)
    // for(until)
    // 打印0-5
    // 通过调用to()方法表示截止数据
    // 调用方法的时候只有1个参数，写法进行简化
    // [0,5]
    //    for (i <- 0.to(5)) {
    for (i <- 0 to 5) {
      println(i)
    }

    // [0,5)
    //    for (i <- 0.until(5)) {
    for (i <- 0 until 5) {
      println(i)
    }

    // 打印1,3,5,7,9
    for (i <- 1.until(10, 2))
      println(i)
    println("**************************")
    // 打印5->0
    //    for (i <- 0.to(5).reverse)
    for (i <- 0 to 5 reverse)
      println(i)

    // 打印100以内5的倍数但是不是7的倍数
    //      for(i <- 1 to 100){
    //        if(i % 5 ==0 & i % 7 != 0)
    //          println(i)
    //      }
    // 守护嵌套
    for (i <- 1 to 100 if (i % 5 == 0 && i % 7 != 0)) 
      println(i) 
  }
}
```

###### demo2

```scala
package cn.tedu.scala

object ForDemo2 {

  def main(args: Array[String]): Unit = {

    // 打印*组成的三角
    /*
     * *
     * **
     * ***
     * ****
     * *****
     * ******
     */
    // 在Scala中，字符串可以进行乘法运算的
    //    for (i <- 1 to 6) {
    //      println("*" * i)
    //    }
    // 打印3个+
    //    println("+" * 3)

    /*
     * ******
     * ******
     * ******
     * ******
     * ******
     */
    //    for (i <- 1 to 5) {
    //      println("*" * 6)
    //    }

    // x:1->5,y:1->6
    // 打印x和y的组合情况
    //    for (x <- 1 to 5)
    //      for (y <- 1 to 6)
    //        println(s"x=${x}, y=${y}")

    // 嵌套循环，两次条件是用;隔开的
    //    for (x <- 1 to 5; y <- 1 to 6)
    //      println(s"x=${x}, y=${y}")
    // 等价于
    //    for {
    //      x <- 1 to 5
    //      y <- 1 to 6
    //    } println(s"x=${x}, y=${y}")

    // 打印九九乘法表
    for {
      i <- 1 to 9
      j <- 1 to i
    } {
      print(s"${i}*${j}=" + i * j + "\t")
      if (i == j)
        println()
    }

    // 构建集合，集合中的元素是2,4,6,8,10
    // for循环的指定计算结果返回放入一个集合中
    //    var result = for (i <- 1 to 5) yield i * 2
    //    println(result)

    // 定义数组
    // 方式一：指定初始大小
    var arr = new Array[Int](5)
    // 方式二：指定初始值
    //    var arr: Array[Int] = Array(2, 4, 6, 8, 10)
    //    var arr: Array[Int] = Array[Int](2, 4, 6, 8, 10)

    // 遍历这个数组
    // 方式一：下标遍历
    //    for (i <- 0 until arr.length)
    //      println(arr(i))
    // for(int i:arr)
    // 方式二：增强for循环
    for (i <- arr) {
      println(i)
    }
  }
}
```

###### demo3

```scala
package cn.tedu.scala

//_等价于Java中的*
import scala.util.control.Breaks._

object ForDemo3 {

  def main(args: Array[String]): Unit = {

    //    for (i <- 1 to 10 if i % 3 != 0)
    //      println(i)

    // 提供了Breaks类来达到类似于break的效果
    breakable {
      for (i <- 1 to 10) {
        if (i % 3 == 0) {
          // 调用了break方法，方法没有参数
          // 如果一个方法在调用的时候没有参数，那么可以省略()
          //   break()
          break
        }
        println(i)
      }
    }
    println("循环执行完成~~~")

  }

}
```

#### 函数式编程

##### demo1	-定义函数

```scala
package cn.tedu.scala

object FunctionDemo1 {

  def main(args: Array[String]): Unit = {

    println(add(3, 5))

    // 如果调用无参函数，()可以省略
    // 如果定义函数的时候省略了()，那么调用函数的时候也得省略
    var d = random

  }

  // 定义一个函数求两个整数的和
  def add(x: Int, y: Int): Int = {
    //    return x + y
    // 不写return，则函数的最后一行默认作为返回结果
    x + y
  }

  // 获取两个整数中较大的一个
  def max(x: Int, y: Int) = {
    if (x > y) x else y
  }

  // 打印m*n的由*组成的矩形
  // 返回值类型是Unit，则返回值类型和=可以省略
  def printStar(m: Int, n: Int) {

    if (m <= 0 && n <= 0)
      throw new Exception

    for (i <- 1 to m)
      println("*" * n)

  }

  // 传入一个数字，开平方
  // 如果方法体只有一句，则{}可以省略
  def sqrt(d: Double) = Math.sqrt(d)

  // 获取一个随机数
  // 如果没有参数，则()可以省略
  def random = Math.random

  // 求多个整数的和
  // 利用*定义可变参数
  def sum(arr: Int*) = {
    var r = 0
    for (i <- arr)
      r += i
    r
  }
}

```

##### demo2	-匿名函数

```scala
package cn.tedu.scala

object FunctionDemo2 {

  def main(args: Array[String]): Unit = {

    // 函数中也可以定义函数
    def sum(x: Int, y: Int) = x + y
    // 求a的b次方
    def pow(a: Double, b: Double) = Math.pow(a, b)

    println(sum(4, 0))

    // 匿名函数
    // 将一个函数赋值给一个变量
    var f = (x: Int, y: Int) => { x + y }
    var m = f
    // 调用上述的匿名函数
    // 打印f的值，此时f的值是一个函数
    println(f)
    // 调用f值中对应的匿名函数
    println(f(3,6)) 
  }
}

```

##### demo3	-闭包

```scala
package cn.tedu.scala

object FunctionDemo3 {

  def main(args: Array[String]): Unit = {

    // 定义函数，接收三个整数，对其中的两个较大的数进行求和
    def sum(x: Int, y: Int, z: Int) = {
      var result = x + y + z
      // 定义了一个函数求三个整数中的最小值
      def min(): Int = {
        if (x < y) {
          if (x < z) x else z
        } else {
          if (y < z) y else z
        }
      }
      println(result - min)
      // 将函数整体作为一个返回值返回
      min _
    }
    // 此时看起来f是个变量，实际上f的值是一个函数
    var f = sum(3, 4, 6)
    // 通过函数的嵌套延长了参数的生命周期的方式称之为闭包
    // 获取三个整数中最小的数
    println(f())
    println(sum(3, 4, 6)())
  }
}

```

##### demo4	-柯里化

```scala
package cn.tedu.scala

object FunctionDemo4 {

  def main(args: Array[String]): Unit = {

    def test(x: Double) = {
      def f(y: Double) = x * y
      f _
    }

    // 柯里化 - 基于闭包的结构来进行了简化
    def test2(x: Double)(y: Double) = x * y

    def t1(x: Double) = {
      def t2(y: Double) = {
        def t3(z: Double) = x * y * z
        t3 _
      }
      t2 _
    }

    def test3(x: Double)(y: Double)(z: Double) = x * y * z

    println(test(4.25)(2.36))
    println(test2(4.25)(2.58))
    println(test3(4.25)(2.58)(2.5))

  }
}
```

##### demo5	-高阶函数

```scala
package cn.tedu.scala

object FunctionDemo5 {

  def main(args: Array[String]): Unit = {

    // 高阶函数 - 参数或者返回值是函数的函数

    // 定义函数接收两个整数，打印这两个整数的运算结果
    // 第三个参数是一个函数，至于是什么函数不知道，需要在调用的时候指定
    def printResult(x: Int, y: Int, f: (Int, Int) => Int) = println(f(x, y))

    // 将一个函数作为参数传递到了另一个函数中
    //    def sum(x: Int, y: Int) = x + y
    //    printResult(3, 5, sum)
    // 在匿名函数的函数体中，x和y各用了1次
    //    printResult(3, 5, (x, y) => x + y)
    printResult(3, 5, _ + _)

    def max(x: Int, y: Int) = if (x > y) x else y
    printResult(5, 1, max)
    // 匿名函数
    printResult(4, 7, (x: Int, y: Int) => if (x < y) x else y)
    printResult(4, 7, (x, y) => if (x < y) x else y)

  }
}
```





### 第2天

#### 函数式编程

##### demo1	-参数设置默认值

```scala
package cn.tedu.function

object function {

  def main(args: Array[String]): Unit = {
	// f 是匿名函数
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

##### demo2	-设置全局默认值

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

##### demo3	-递归

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



#### object

##### person	-伴生对象

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

##### demo1	-包对象

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

##### demo2	-类的别名，default修饰

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

##### demo3	-伴生类(非静态)，伴生对象（静态）

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



##### demo4	-构造方法的重载

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



##### demo5	-类的向上造型与继承

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



#### abstract，trait

##### demo1	-abstract

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

##### demo2	-trait	-extends，with

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



##### demo3	-tracit	特质之间方法要不一样

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



#### API

##### demo1	-Exception

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

##### demo2	-Collection

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

##### demo3	-Collection

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

##### demo4	-list

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