## easymall-day01

--------

### 什么是MAVEN

> 基于项目对象模型（POM）
>
> 是一个项目管理工具，可以对项目的打包部署安装报告等做统一的管理
>
> 用一小段描述性息来管理项目的构建
>
> > 描述信息（三个坐标）
> >
> > > groupId
> > >
> > > artifactId
> > >
> > > versionId



### Maven的windows安装

> 详见笔记
>
> > `maven3.6	------ > 最低为jdk1.8`
> >
> > `ali_repo`
>
> 配置环境变量
>
> > 原理与java相同



### MAVEN的概念

#### 库（repository）

###### 中央库

> maven社区维护，给全世界开发人员准备的库
>

###### 远程库（私服）

> 公司内部开发项目的交流
>
> 可以分模块处理

###### 本地库

> 为了后续的方便实用，将下载的资源放到本地的一个文件夹中



#### 资源定位

###### 定位标签

> `<groupId>`	域名倒写，一个公司的具体某一个项目
>
> `<artifactId>`	当前项目的一个模块
>
> `<version>`	表示一个项目的具体版本

```xml
  <dependencies>	
	<dependency>
		<groupId>org.springframework</groupId>
		<artifactId>spring-beans</artifactId>
		<version>4.3.7.RELEASE</version>
	</dependency>
  </dependencies>
```

###### 资源的结构

> `spring-beans-4.3.7.RELEASE.jar`	资源
>
> `spring-beans-4.3.7.RELEASE.pom`	maven资源的核心配置
>
> `spring-beans-4.3.7.RELEASE-javadoc.jar`	文档
>
> `spring-beans-4.3.7.RELEASE-source.jar`	源码
>
> `spring-beans-4.3.7.RELEASE.jar.sha1`	hash散列计算是否丢包	



### MAVEN的项目插件

> 在maven项目中可以在==pom==文件里制定当前项目可以使用的各种插件

###### 源码包插件

> 作为项目运行时加载的插件资源,可以在pom文件(每个项目的pom文件)中的build标签里完成

> `execution`:不需要的依赖包

```xml
	<build>
		<plugins>
			<plugin>
				<groupId>org.apache.maven.plugins</groupId>
				<artifactId>maven-source-plugin</artifactId>
				<version>3.0.1</version>
				<configuration>
					<attach>true</attach>
				</configuration>
				<executions>
					<execution>
						<phase>compile</phase>
						<goals>
							<goal>jar</goal>
						</goals>
					</execution>
				</executions>
			</plugin>
		</plugins>
	 </build>
```

###### main方法的主清单配置插件

> 在项目中存在main方法,作为jar包的入口类,cpu需要加载运行,maven软件需要在打包时配置main方法所在的类路径,否则jar包无法正常运行

```xml
	<plugin>
		<groupId>org.apache.maven.plugins</groupId>
		<artifactId>maven-jar-plugin</artifactId>
		<configuration>
			<archive>
				<manifest>
					<addClasspath>true</addClasspath>
					<mainClass>cn.tedu.App</mainClass> <!-- 此处为主入口-->
				</manifest>
			</archive>
		</configuration>
	</plugin>

```

###### tomcat的maven插件

> 这个插件的核心tomcat代码,就是tomcat7版本,没有tomcat8,没有tomcat9

```xml
		<plugin>
			<groupId>org.apache.tomcat.maven</groupId>
			<artifactId>tomcat7-maven-plugin</artifactId>	
             <version>2.2</version>
			<configuration>
				<!-- port -->
		    	<port>80</port>
		   		<!-- context path -->
		   		<path>/</path>
		   		<!-- utf-8 -->
		   		<uriEncoding>utf-8</uriEncoding>
		   		<useBodyEncodingForURI>utf-8</useBodyEncodingForURI>
			</configuration>
		</plugin>

```




###  eclipse整合maven

#### 在maven中配置本地库ali_repo

..../maven3.6/confsettings.xml/settings.xml

> `\`:需要转义
>
> `/`:不需要转义

```xml
	<localRepository>E:/software/ali_repo</localRepository>
```



#### maven依赖 

> 需要知道依赖的名称
>
> 资源由三个标签来定义坐标
>
> > `<groupId>`:域名倒写，一个公司的具体某一个项目
> >
> > `<artifactId>`：某一个模块的名称
> >
> > `<version>`:资源的版本
>
> 依赖的传递性
>
> > maven会将所有依赖的jar包导入
> >
> > 不需要的jar包可以使用`exclusion`屏蔽



#### 依赖的使用范围(scope)

> 一般只使用前两种

###### complie

> 从编译开始,打包,运行,安装,部署等等(默认值)

###### test

> 测试时候使用,打包,运行,安装,部署不在使用

###### runtime

> 除了编译时不使用,其他的都跟随

###### provide

> 编译和测试时有效,但是运行,打包,部署,安装不存在

###### system

> 与本地jar包有关，可以利用system范围指定一个本地路径的jar包

###### pom.xml

```xml
<dependency>
    	<groupId>cn.tedu</groupId>
    	<artifactId>test</artifactId>
    	<version>1.0</version>
    	<scope>system</scope>
    	<systemPath>C:\\maven-test01-1.0-SNAPSHOT.jar</systemPath>
</dependency>
```



#### Maven的生命周期

> 可以通过run as ---> maven build 生成原生命令

> 创建	`mvn archetype:generate`
>
> 清空	`mvn clean`
>
> 编译	`mvn compile`
>
> 测试	`mvn test`
>
> 打包	`mvn package`
>
> 安装	`mvn install`
>
> 发布	`mvn deploy`



#### 创建出来的maven项目

> `src/main/java`:编写工程代码文件夹
>
> `src/main/resources`:配置文件存放位置,spring.xml/application.xml,properties
>
> `src/test/java`:测试代码
>
> `src/test/resources`:测试代码使用的配置文件(如果没有,默认使用src/main/resources内容)

```java
@Test
	public void springCon(){
		//通过加载配置文件,运行获取上下文对象
		ClassPathXmlApplicationContext context=
				new ClassPathXmlApplicationContext
				("classpath:spring/spring.xml");
		//maven工程加载的配置文件和配置各种内容,需要通过classload加载
		//普通java工程bin/WEB-INF/classes下就是加载路径
		//指向target/classes,classpath
		//java的类加载器路径起始地址,classpath==target/classes
		HelloController bean1 = context.getBean(HelloController.class);
		HelloService bean2 = context.getBean(HelloService.class);
		HelloDao bean3 = context.getBean(HelloDao.class);
		String name="wanglaoshi";
		System.out.println(bean1.sayHi(name));
		System.out.println(bean2.sayHi(name));
		System.out.println(bean3.sayHi(name));
	}
	//持久层的测试案例
	@Test
	public void userQuery(){
		ClassPathXmlApplicationContext context=
				new ClassPathXmlApplicationContext
				("classpath:spring/spring.xml");
		//获取userController
		UserController uc=context.getBean(UserController.class);
		//查询user
		User user=uc.queryOne(3);
		System.out.println(user);
	}
```

