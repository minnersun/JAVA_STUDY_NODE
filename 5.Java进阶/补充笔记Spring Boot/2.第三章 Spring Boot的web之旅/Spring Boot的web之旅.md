## Spring Boot的web之旅

---

### 创建Controller

##### @RestController

> Spring 4.0版本之后的一个注解
>
> > 功能相当于@Controller和@ResponseBody之和

##### @GetMapping("/hello")

> Spring后期推出的一个注解
>
> > 是`@RequestMapping(method = RequestMethod.GET)`的缩写

> 可见代码详情：3-1

###### 案例

###### TestController.java

```java
@RestController
//@RequestMapping("test")
public class TestController {
    //用于测试
    @GetMapping("/hello")
    public void test() {
        System.out.println("hello");
    }
}
```



### 创建WebFlux项目

##### 优势

> SpringMVC是同步阻塞的IO模型，资源浪费相对来说比较严重
>
> WebFlux是异步非阻塞的web框架，效率较高
>
> > WebFlux不再完全依赖于Servlet，而是实现了`Reactive Streams`规范
> >
> > > 如果使用Servlet容器，必须在Servlet3.1版本以上
> >
> > WebFlux默认推荐的是Netty这种异步容器

> 可见代码详情：3-2

###### pom.xml

```xml
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-webflux</artifactId>
        </dependency>
```







### Spring Boot配置文件

##### 自定义配置

###### application.properties

```properties
# 配置端口号
server.port=10004
#自定义配置，使用@Value注解引用
#book.name=王新晖
book.name=\u738b\u65b0\u6656

```

###### TestController.java

```java
@RestController
//@RequestMapping("user")
public class TestController {

    @Value("${book.name}")
    private String bookName;
    //用于测试
    @GetMapping("/hello")
    public void test() {
        System.out.println(bookName);
    }
}
```

##### 注：乱码处理

> 官方处理中文乱码的方式是使用Unicode的方式来展现字符串
>
> > 如`book.name=王新晖`应该配置为
> >
> > `book.name=\u738b\u65b0\u6656`



##### 使用随机数

###### application.properties

```properties
# 随机字符串
book.value=${random.uuid}
# 随机int值
book.intvalue=${random.int}
# 随机long值
book.longvalue=${random.long}
# 随机uuid
book.uuid=${random.uuid}
# 1000以内的随机数
book.randomnum=${random.int(1000)}
# 自定义属性间引用
book.random=${book.value}
```

###### TestController.java

```java
@RestController
//@RequestMapping("user")
public class TestController {

    @Value("${book.value}")
    private String value;
    @Value("${book.intvalue}")
    private String intvalue;
    @Value("${book.uuid}")
    private String uuid;
    @Value("${book.random}")
    private String random;
    
    //用于测试
    @GetMapping("/hello")
    public void test() {
        System.out.println(value+" "+intvalue+" "+uuid+" "+random);
    }
}
```



##### 使用javaBean的形式给属性赋值

> 在Bean类上使用`@ConfigurationProperties(prefix = "book")`注解
>
> 需要在启动类上添加`@EnableConfigurationProperties(BookConfigBean.class)`

###### StartSpringBoot.java

```java
package com.wiscom;

@SpringBootApplication
@MapperScan("com.wiscom.mapper")
// 表示启用@ConfigurationProperties这个配置类
@EnableConfigurationProperties(BookConfigBean.class)
public class StartSpringBoot {
    public static void main(String[] args) {
        SpringApplication.run(StartSpringBoot.class, args);
    }
    //在启动类中可以按照一个配置类来编写所有需要的配置内容
}
```

###### application.properties

```properties
# 随机字符串
book.value=${random.uuid}
# 随机int值
book.intvalue=${random.int}
# 随机long值
book.longvalue=${random.long}
# 随机uuid
book.uuid=${random.uuid}
# 1000以内的随机数
book.randomnum=${random.int(1000)}
# 自定义属性间引用
book.random=${book.value}
```

###### BookConfigBean.java

```java
package com.wiscom.pojo;

import org.springframework.boot.context.properties.ConfigurationProperties;
// @PropertySource:可选，表示从application.properties这一文件中读取数据
@PropertySource(value = "classpath:application.properties")
@ConfigurationProperties(prefix = "book")
public class BookConfigBean {
    private String value;
    private String intvalue;
    private String longvalue;
    private String uuid;
    private String randomnum;
    private String random;
}

```

###### TestController.java

```java
@RestController
//@RequestMapping("user")
public class TestController {
    @Autowired
    private BookConfigBean bookConfigBean;

    //用于测试
    @GetMapping("/hello")
    public void test() {
        System.out.println(bookConfigBean.toString());
    }

```



### 多环境配置（开发、测试、生产）

> 可见代码详情目录：3-4

##### application.properties

```properties
# 决定使用哪个配置文件 - 使用application-dev.properties这个配置文件
spring.profiles.active=dev
```

##### application-dev.properties

```properties
# 开发环境
server.port=8081
```

##### application-test.properties

```properties
# 测试环境
server.port=8082
```

##### application-prod.properties

```properties
# 生产环境
server.port=8083
```



### Spring Boot使用页面模板

> 主要讲解了Spring Boot框架如何使用`Thymeleaf和FreeMarker`模板
>
> `Thymaleaf`为官方推荐使用模板
>
> > Thymaleaf对HTML的校验比较严格
> >
> > > 需要加入nekohtml的依赖来避免

> 案例
>
> > th:text
> >
> > th:value

##### Thymeleaf

> 可见代码详情目录：3-5-1

###### application.xml

```properties
# thymeleaf配置
# thymeleaf缓存是否开启，开发时建议关闭，否则更改页面后不会实时展示效果
spring.thymeleaf.cache=false
# thymeleaf编码格式
spring.thymeleaf.encoding=UTF-8
# thymeleaf对HTML校验很严格，用这个去除thymeleaf严格校验
spring.thymeleaf.mode=LEGACYHTML5
# thymeleaf模板文件前缀
spring.thymeleaf.prefix=classpath:/tempaltes/
# thymeleaf模板文件后缀
spring.thymeleaf.suffix=.html
```

###### pom.xml

```xml
		<!-- 加入thymeleaf依赖 -->
		<dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-thymeleaf</artifactId>
        </dependency>
		<!-- Thymaleaf对HTML的校验比较严格，加入nekohtml包来避"坑" -->
		<!-- 去除html严格校验 -->
		<dependency>
            <groupId>net.sourceforge.nekohtml</groupId>
            <artifactId>nekohtml</artifactId>
            <version>1.9.22</version>
        </dependency>
```



### SpringBoot使用传统的JSP

> SpringBoot使用传统的JSP需要添加`jasper`包
>
> > `jasper`
> >
> > > 把JVM不认识的JSP文件解析成java文件，然后编译成class文件提供使用
>
> > `jstl`
> >
> > > JSP标准标签库

> 见代码详情目录：3-5-3

###### pom.xml

```xml
         <!--jasper包：把JVM不认识的JSP文件解析成java文件，然后编译成class文件提供使用-->
		<!--SpringBoot自带的Tomcat没有jasper这个包，所以需要手动添加-->
		<dependency>
            <groupId>org.apache.tomcat.embed</groupId>
            <artifactId>tomcat-embed-jasper</artifactId>
            <scope>provided</scope>
        </dependency>
		<!-- 添加jsp标准标签库 -->
        <dependency>
            <groupId>javax.servlet</groupId>
            <artifactId>jstl</artifactId>
        </dependency>
```



### 文件的上传和下载

##### 单个文件上传

##### 批量上传

##### 下载方法

