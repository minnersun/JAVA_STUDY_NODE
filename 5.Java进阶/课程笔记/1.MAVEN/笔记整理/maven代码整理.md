## maven代码整理

-----

### pom.xml

```xml
<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
  xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/maven-v4_0_0.xsd">
  <modelVersion>4.0.0</modelVersion>
  <groupId>cn.tedu</groupId>
  <artifactId>SSM-DEMO01</artifactId>
  <packaging>war</packaging>
  <version>0.0.1-SNAPSHOT</version>
  <name>SSM-DEMO01 Maven Webapp</name>
  <url>http://maven.apache.org</url>
  <dependencies>
    <dependency>
      <groupId>junit</groupId>
      <artifactId>junit</artifactId>
      <version>3.8.1</version>
      <scope>test</scope>
    </dependency>
    <dependency>
  		<groupId>org.springframework</groupId>
  		<artifactId>spring-context</artifactId>
  		<version>4.3.7.RELEASE</version>
  	</dependency>
  	<!-- datasource -->
  	<dependency>
  		<groupId>com.alibaba</groupId>
		<artifactId>druid</artifactId>
		<version>1.0.14</version>
	</dependency>
	<!-- spring mysql -->
	<dependency>
		<groupId>org.springframework</groupId>
		<artifactId>spring-jdbc</artifactId>
		<version>4.3.7.RELEASE</version>
	</dependency>
	<dependency>
		<groupId>mysql</groupId>
		<artifactId>mysql-connector-java</artifactId>
		<version>5.0.8</version>
	</dependency>
	<!-- mybatis -->
	<dependency>
		<groupId>org.mybatis</groupId>
		<artifactId>mybatis</artifactId>
		<version>3.4.5</version>
	</dependency>
	<!-- spring mybatis  -->
	<dependency>
		<groupId>org.mybatis</groupId>
		<artifactId>mybatis-spring</artifactId>
		<version>1.3.1</version>
	</dependency>
  </dependencies>
  <build>
    <finalName>SSM-DEMO01</finalName>
  </build>
</project>

```

### src/main/resources/	

##### spring.xml

```xml
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
	xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" 
	xmlns:aop="http://www.springframework.org/schema/aop"
	xmlns:tx="http://www.springframework.org/schema/tx"
	xmlns:p="http://www.springframework.org/schema/p"
	xmlns:util="http://www.springframework.org/schema/util" 
	xmlns:context="http://www.springframework.org/schema/context"
	xmlns:mvc="http://www.springframework.org/schema/mvc"
	xsi:schemaLocation="
        http://www.springframework.org/schema/beans
        http://www.springframework.org/schema/beans/spring-beans.xsd
        http://www.springframework.org/schema/aop 
        http://www.springframework.org/schema/aop/spring-aop.xsd
        http://www.springframework.org/schema/tx 
        http://www.springframework.org/schema/tx/spring-tx.xsd
        http://www.springframework.org/schema/util 
        http://www.springframework.org/schema/util/spring-util.xsd
        http://www.springframework.org/schema/context
        http://www.springframework.org/schema/context/spring-context.xsd
        http://www.springframework.org/schema/mvc
        http://www.springframework.org/schema/mvc/spring-mvc.xsd">
	<!-- 配置内容 -->
	<context:component-scan base-package="cn.tedu"/>
	<!-- 持久层数据源 -->
	<bean id="dataSource" class="com.alibaba.druid.pool.DruidDataSource">
		<!-- 4个属性 -->
		<property name="driverClassName" value="com.mysql.jdbc.Driver"/>
		<property name="url" value="jdbc:mysql:///easymall"/>
		<property name="username" value="root"/>
		<property name="password" value="root"/>
	</bean>
	<!-- sqlSession -->
	<bean id="sqlSession" class="org.mybatis.spring.SqlSessionFactoryBean">
		<!-- 数据源注入 -->
		<property name="dataSource" ref="dataSource"/>
		<!-- 扫描mybatis的独立配置文件 关闭二级缓存,驼峰命名 -->
		<property name="configLocation" value="classpath:mybatis/mybatis-config.xml"/>
		<!-- 映射文件扫描 -->
		<property name="mapperLocations" value="classpath:mapper/*.xml"/>
		<!-- 别名包 -->
		<property name="typeAliasesPackage" value="cn.tedu.domain"></property>
	</bean>
	<bean class="org.mybatis.spring.mapper.MapperScannerConfigurer">
		<!-- 扫描的接口包 cn.tedu.mapper -->
		<property name="basePackage" value="cn.tedu.mapper"/>
	</bean>
 </beans>
```

##### mybatis-config.xml

```xml
<?xml version="1.0" encoding="UTF-8" ?>
<!DOCTYPE configuration
PUBLIC "-//mybatis.org//DTD Config 3.0//EN"
"http://mybatis.org/dtd/mybatis-3-config.dtd">
<configuration>
	<!--添加缓存  引入第三方二级缓存 -->
</configuration>
```

##### UserMapper.xml

```xml
<?xml version="1.0" encoding="UTF-8" ?>
<!DOCTYPE mapper PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN"
"http://mybatis.org/dtd/mybatis-3-mapper.dtd">
<mapper namespace="cn.tedu.mapper.UserMapper">
	<select id="queryOne" parameterType="int"
	resultType="User">
		select * from user where id=#{id}
	</select>
    
	<!-- <insert id="saveUser" parameterType="User">
		insert into user (字段) vlaues(#{属性})
	</insert> -->
</mapper> 
```



### src/main/java

##### cn.tedu.controller

###### UserController

```java
package cn.tedu.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import cn.tedu.domain.User;
import cn.tedu.service.UserService;
@Controller
public class UserController {
	@Autowired
	private UserService userSerivce;
	//根据一个user的id值查询user对象
	public User queryOne(Integer id){
		return userSerivce.queryOne(id);
	}
}

```

##### cn.tedu.service

###### UserService

```java
package cn.tedu.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.tedu.domain.User;
import cn.tedu.mapper.UserMapper;
@Service
public class UserService {
	@Autowired
	private UserMapper userMapper;
	public User queryOne(Integer id) {
		return userMapper.queryOne(id);
	}
}

```

##### cn.tedu.domain

###### User

```java
public class User {

	private Integer id;//null
	private String username;
	private String password;
	private String nickname;
	private String email;
}
```

##### cn.tedu.mapper

###### UserMapper

```java
package cn.tedu.mapper;

import cn.tedu.domain.User;

public interface UserMapper {

	User queryOne(Integer id);
}
```



### src/test/java

##### cn.tedu.test

###### SpringTest

```java
package cn.tedu.test;

import org.junit.Test;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import cn.tedu.controller.HelloController;
import cn.tedu.controller.UserController;
import cn.tedu.dao.HelloDao;
import cn.tedu.domain.User;
import cn.tedu.service.HelloService;

public class SpringTest {
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
	
}
```