## 第十六天课程笔记(Log4j)

----

### Log4j

> 在java语言中最常使用的日志收集框架

> 如果javaee不包含log4j  需要手动导入jar包

##### 在src目录下引入名称为log4j.properties文件

````properties
		log4j.rootLogger = info,stdout,D,E
		
		log4j.appender.stdout = org.apache.log4j.ConsoleAppender
		log4j.appender.stdout.Target = System.out
		log4j.appender.stdout.layout = org.apache.log4j.PatternLayout
		log4j.appender.stdout.layout.ConversionPattern = [%-5p] %d{yyyy-MM-dd HH:mm:ss,SSS} method:%l%m%n
		
		log4j.appender.D = org.apache.log4j.DailyRollingFileAppender
		log4j.appender.D.File = E://logs/log.log
		log4j.appender.D.Append = true
		log4j.appender.D.Threshold = DEBUG
		log4j.appender.D.layout = org.apache.log4j.PatternLayout
		log4j.appender.D.layout.ConversionPattern = %-d{yyyy-MM-dd HH:mm:ss} [ %t:%r ] - [ %p ] %m%n
		
		log4j.appender.E = org.apache.log4j.DailyRollingFileAppender
		log4j.appender.E.File =E://logs/error.log
		log4j.appender.E.Append = true
		log4j.appender.E.Threshold = ERROR
		log4j.appender.E.layout = org.apache.log4j.PatternLayout
		log4j.appender.E.layout.ConversionPattern =%-d{yyyy-MM-dd HH\:mm\:ss} [ %t\:%r ] - [ %p ] %m%n

````

##### 日志的优先级：

> off 最高等级， 用于关闭所有日志记录。
>
> fatal 指出每个严重的错误事件将会导致应用程序的退出。
>
> error 指出虽然发生错误事件， 但仍然不影响系统的继续运行。
>
> warn 表明会出现潜在的错误情形。
>
> info 一般和在粗粒度级别上， 强调应用程序的运行全程。
>
> debug 一般用于细粒度级别上， 对调试应用程序非常有帮助。
>
> all 最低等级， 用于打开所有日志记录

##### 日志的输出目的地Appender

> org. apache. log4j. ConsoleAppender（控制台） 
>
> org. apache. log4j. FileAppender（文件） 
>
> org. apache. log4j. DailyRollingFileAppender（每天产生一个日 志文件） 
>
> org. apache. log4j. RollingFileAppender（文件大小到达指定尺寸的时候产生一个新的文件） 
>
> org. apache. log4j. WriterAppender（将日 志信息以流格式发送到任意指定的地方）

##### 日志输出格式 layout

> org. apache. log4j. HTMLLayout（以HTML表格形式布局）
>
> org. apache. log4j. PatternLayout（可以灵活地指定布局模式）
>
> org. apache. log4j. SimpleLayout（包含日志信息的级别和信息字符串） 
>
> org. apache. log4j. TTCCLayout（包含日志产生的时间、 线程、 类别等等信息）

##### 日志输出格式的正则：

> %p 输出优先级， 即DEBUG， INFO， WARN， ERROR，FATAL
>
> %r 输出自 应用启动到输出该log信息耗费的毫秒数
>
> %c 输出所属的类目 ， 通常就是所在类的全名
>
> %t 输出产生该日 志事件的线程名
>
> %n 输出一个回车换行符， Windows平台为“rn”， Unix平台为“n”
>
> %d 输出日 志时间点的日 期或时间， 默认格式为ISO8601，也可以在其后指定格式， 比如： %d{yyy MMM dd HH: mm: ss, SSS}， 输出类似： 2002年10月 18日 22： 10： 28， 921
>
> %l 输出日志事件的发生位置， 包括类目 名、 发生的线程， 以及在代码中的行数。
>
> > 举例： Testlog4. main(TestLog4. java: 10)
>
> %m  代表日志信息

### Log4j实现

##### 打印日志

````java
			package cn.tedu.log4j;
			
			import org.apache.log4j.Logger;
			
			//log4j日志框架测试
			public class Demo1 {
			        //创建一个logger对象
			        public static Logger  logger = Logger.getLogger(Demo1.class);
			        public static void main(String[] args) {
			                logger.fatal("这是一个致命错误");
			                logger.error("这是一个严重错误");
			                logger.warn("这是一个警告信息");
			                logger.info("这是一个普通信息");
			                logger.debug("这是一个调试信息");
			        }
			
			}

````

