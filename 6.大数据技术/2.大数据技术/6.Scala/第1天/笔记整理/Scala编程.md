## Scala编程	-案例

-----

### 入门案例

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

### 变量名

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

### 声明变量

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

### 类型转化

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

### 运算规则

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

### if	案例

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

### For	案例

#### demo1

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

#### demo2

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

#### demo3

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

### 函数式编程

#### demo1

````scala
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
````

#### demo2

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

#### demo3

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

#### demo4

````scala
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
````

#### demo5

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