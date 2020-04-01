## SSM整合

-------

### 导包，过程略

### web.xml文件配置

> 配置前端控制器
>
> 配置监听器：自动加载bean容器
>
> 全站乱码过滤器
>
> 主页

```xml
<?xml version="1.0" encoding="UTF-8"?>
<web-app version="2.5" 
	xmlns="http://java.sun.com/xml/ns/javaee" 
	xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" 
	xsi:schemaLocation="http://java.sun.com/xml/ns/javaee 
	http://java.sun.com/xml/ns/javaee/web-app_2_5.xsd">
  
  <!-- 配置前端控制器  -->
  	<!-- SpringMVC前端控制器 -->
    <!-- 负责转发请求，接受用户的请求，申请处理后，将响应返回给客户 -->
	<servlet>
		<servlet-name>springmvc</servlet-name>
		<servlet-class>org.springframework.web.servlet.DispatcherServlet</servlet-class>
		<init-param>
			<param-name>contextConfigLocation</param-name>
			<param-value>classpath:/applicationContext-mvc.xml</param-value>
		</init-param>
	</servlet>
	<servlet-mapping>
		<servlet-name>springmvc</servlet-name>
		<url-pattern>*.action</url-pattern>
	</servlet-mapping>

	<!-- 配置监听器 -->
    <!-- 自动加载bean容器 --> 
	<listener>
		<listener-class>org.springframework.web.context.ContextLoaderListener</listener-class>
	</listener>
	<context-param>
		  <param-name>contextConfigLocation</param-name>
		  <param-value>classpath:applicationContext.xml</param-value>
  	</context-param>

	<!-- SpringMVC全站乱码解决过滤器 -->
	<filter>
		<filter-name>encodingFilter</filter-name>
		<filter-class>org.springframework.web.filter.CharacterEncodingFilter</filter-class>
		<init-param>
			<param-name>encoding</param-name>
			<param-value>utf-8</param-value>
		</init-param>
	</filter>
	<filter-mapping>
		<filter-name>encodingFilter</filter-name>
		<url-pattern>/*</url-pattern>
	</filter-mapping>
  
  
  <!-- 主页  -->
  <welcome-file-list>
    <welcome-file>index.jsp</welcome-file>
  </welcome-file-list>
</web-app>

```

### applicationContext-mvc.xml文件配置

> 开启包扫描
>
> 开启注解配置mvc
>
> 配置视图解析器

```xml
<?xml version="1.0" encoding="UTF-8"?>
<beans 
xmlns="http://www.springframework.org/schema/beans"
xmlns:context="http://www.springframework.org/schema/context"
xmlns:aop="http://www.springframework.org/schema/aop" 
xmlns:mvc="http://www.springframework.org/schema/mvc"
xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" 
xsi:schemaLocation=
"http://www.springframework.org/schema/beans 
http://www.springframework.org/schema/beans/spring-beans-3.2.xsd
http://www.springframework.org/schema/context
http://www.springframework.org/schema/context/spring-context-3.2.xsd
http://www.springframework.org/schema/aop
http://www.springframework.org/schema/aop/spring-aop-3.2.xsd
http://www.springframework.org/schema/mvc
http://www.springframework.org/schema/mvc/spring-mvc-3.2.xsd
"
>
<!-- 开启包扫描 -->
<context:component-scan base-package="cn.tedu.web"></context:component-scan>

<!-- 注解配置MVC -->
<mvc:annotation-driven></mvc:annotation-driven>


	<!-- 配置视图解析器 -->
	<bean class="org.springframework.web.servlet.view.InternalResourceViewResolver">
		<property name="prefix" value="/WEB-INF/"></property>
		<property name="suffix" value=".jsp"></property>
	</bean>

</beans>
```

### applicationContext.xml文件配置

> 开启包扫描
>
> 开启注解配置
>
> 配置数据源
>
> 整合Mybatis
>
> 配置 MyBatis MapperBean扫描器
>
> 配置事务管理器

```xml
<?xml version="1.0" encoding="UTF-8"?>
<beans 
xmlns="http://www.springframework.org/schema/beans"
xmlns:context="http://www.springframework.org/schema/context"
xmlns:tx="http://www.springframework.org/schema/tx"
xmlns:aop="http://www.springframework.org/schema/aop" 
xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" 
xsi:schemaLocation=
"http://www.springframework.org/schema/beans 
http://www.springframework.org/schema/beans/spring-beans-3.2.xsd
http://www.springframework.org/schema/context
http://www.springframework.org/schema/context/spring-context-3.2.xsd
http://www.springframework.org/schema/aop
http://www.springframework.org/schema/aop/spring-aop-3.2.xsd
http://www.springframework.org/schema/tx
http://www.springframework.org/schema/tx/spring-tx-3.2.xsd
"
>

	<!-- 开启包扫描实现IOC -->
	<context:component-scan base-package="cn.tedu.service"></context:component-scan>
	<context:component-scan base-package="cn.tedu.web"></context:component-scan>
	<context:component-scan base-package="cn.tedu.mapper"></context:component-scan>
	
	<!-- 开启注解注入实现DI -->
	<context:annotation-config></context:annotation-config>
	
		<!-- 配置数据源 -->
	<bean id="dataSource" class="com.mchange.v2.c3p0.ComboPooledDataSource">
		<property name="driverClass" value="com.mysql.jdbc.Driver"></property>
		<property name="jdbcUrl" value="jdbc:mysql:///ssmdb"></property>
		<property name="user" value="root"></property>
		<property name="password" value="root"></property>
	</bean>
	
	<!-- 整合MyBatis -->
	<bean id="sqlSessionFactory" class="org.mybatis.spring.SqlSessionFactoryBean">
		<property name="dataSource" ref="dataSource"></property>
        <!-- sqlMapConfig.xml的路径 -->
		<property name="configLocation" value="classpath:/sqlMapConfig.xml"></property>
        <!-- 映射路径 -->
		<property name="mapperLocations" value="classpath:/cn/tedu/mapper/*.xml"></property>
	</bean>
	
	<!-- MyBatis MapperBean扫描器，负责为MapperBean生成实现类 -->
	<bean class="org.mybatis.spring.mapper.MapperScannerConfigurer">
		<property name="basePackage" value="cn.tedu.mapper"></property>
	</bean>
	
	
	<!-- 配置事务管理器 -->
	<bean id="transactionManager" class="org.springframework.jdbc.datasource.DataSourceTransactionManager">
		<property name="dataSource" ref="dataSource"></property>
	</bean>
	<!-- 注解开启事务 -->
	<tx:annotation-driven/>

</beans>
```

### sqlMapConfig.xml

> 整合都在application中配置
>
> > 配置数据源
> >
> > 配置映射文件

```xml
<?xml version="1.0" encoding="UTF-8" ?>
<!DOCTYPE configuration
	PUBLIC "-//mybatis.org//DTD Config 3.0//EN"
	"http://mybatis.org/dtd/mybatis-3-config.dtd">
<configuration>
	
</configuration>

```

### 开发控制器

##### cn.tedu.web.FirstController

```java
package cn.tedu.web;

@Controller
public class FirstController {
	@Autowired
	private UserService userService;
	
	@RequestMapping("/first.action")
	public void first(){
		userService.query();
	}
}

```

### 开发service

##### cn.tedu.service.UserService

```java
package cn.tedu.service;

public interface UserService {
	
	public void query();
}
```

##### cn.tedu.service.UserServiceImpl

```java
package cn.tedu.service;

import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.tedu.mapper.UserMapper;


@Service
public class UserServiceImpl implements UserService{
	@Autowired
	private UserMapper mapper;
	
	@Transactional
	public void query() {
		// 真正去实际访问数据库的内容
//		List<User> list = mapper.queryAll();
//		System.out.println(list);
		
		HashMap<String,Object> map = new HashMap<String,Object>();
		map.put("id", 10);
		map.put("name", "赵敏");
		map.put("addr", "蒙古");
		
		mapper.insertOne(map);
		
		int i = 1 / 0;
	}

}

```



### 创建bean

##### cn.tedu.domain.User

```java
public class User {
	private int id;
	private String name;
	private String addr;
}
```



### 创建映射文件

##### UserMapper.xml

```xml
<?xml version="1.0" encoding="UTF-8" ?>
<!DOCTYPE mapper
	PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN"
	"http://mybatis.org/dtd/mybatis-3-mapper.dtd">
<mapper namespace="cn.tedu.mapper.UserMapper">
	<select id="queryAll" resultType="cn.tedu.domain.User">
		select * from user;
	</select>
	<select id="insertOne">
		insert into user values(#{id},#{name},#{addr});
	</select>
</mapper>

```



### 创建bean的Mapper接口

##### cn.tedu.mapper.UserMapper

```java
package cn.tedu.mapper;

@Repository
public interface UserMapper {
	public List<User> queryAll();
	
	public void insertOne(HashMap<String,Object> map);
}

```

