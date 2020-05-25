## Spring Boot 配置文件整合

-----

### application.properties

```properties
# 配置端口号：（第三章）
server.port=10004

# 自定义属性
# 如果需要引用，在属性上添加@Value("${属性名}")：（第三章）
# 以中文的方式进行配置会出现乱码，官方的解决方案是使用Unicode方式
#book.name=Spring Boot实战
book.name=\u0053\u0070\u0072\u0069\u006e\u0067\u0020\u0042\u006f\u006f\u0074\u5b9e\u6218
#book.author=王新晖
book.author=\u738b\u65b0\u6656
# 使用随机数
# 可创建实体类以@ConfigurationProperties(prefix = "book")的形式赋值：（第三章）
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

# mysql
# 数据库驱动
spring.datasource.driverClassName=com.mysql.cj.jdbc.Driver
# 数据库地址
spring.datasource.url=jdbc:mysql://101.200.42.195:3306/easydb?useUnicode=true&characterEncoding=utf8&useSSL=false
# 数据库用户名
spring.datasource.username=root
# 数据库密码
spring.datasource.password=root


# SQL Server
# 数据库地址
spring.datasource.url=jdbc:sqlserver://192.168.16.218:1433;databaseName=dev_btrpawn
# 数据库用户名
spring.datasource.username=sa
# 数据库密码
spring.datasource.password=sa
# 数据库驱动
spring.datasource.driver-class-name=com.microsoft.sqlserver.jdbc.SQLServer

# Oracle
# 数据库驱动
spring.datasource.driver-class-name=oracle.jdbc.OracleDriver
# 数据库地址
spring.datasource.url=jdbc:oracle:thin:@172.17.112.249:1521:orcl
# 数据库用户名
spring.datasource.username=sde
# 数据库密码
spring.datasource.password=sde

# MongoDB
# 无密码
#spring.data.mongodb.uri=mongodb://localhost:27017/test
# 有密码
spring.data.mongodb.uri=mongodb://root(username):root(password)@localhost(ip地址):27017(端口号)/test(collections/数据库)

# Neo4j数据库
spring.data.neo4j.uri=http://localhost:7474
spring.data.neo4j.username=root
spring.data.neo4j.password=root

# Redis数据库配置
# Redis数据库索引，默认为0
spring.redis.database=0
# Redis服务器地址
spring.redis.host=localhost
# Redis服务器连接端口
spring.redis.port=6379
# Redis服务器连接密码
spring.redis.password=
# 连接池最大连接数，使用负值则表示没有任何限制
spring.redis.pool.max-active=8
# 连接池最大阻塞等待时间，使用负值则表示没有任何限制
spring.redis.pool.max-wait=-1
# 连接池中得最大空闲连接
spring.redis.pool.max-idle=8
# 连接池中得最小空闲连接
spring.redis.pool.min-idle=0
# 连接超时时间(毫秒)
spring.redis.timeout=0

# MyBatis配置
# 检查Mybatis配置文件是否存在
mybatis.check-config-location=true
# 配置文件位置：一般用于配置别名等信息
mybatis.config-location=classpath://mybatis/mybatis-config.xml
# mapper xml文件地址
mybatis.mapper-locations=classpath*:/mapper/*.xml
# 日志级别
logging.level.com.springboot.dao.UserMapper=debug

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

### pom.xml

```xml
        <!-- mysql需要添加的依赖 -->
		<dependency>
            <groupId>mysql</groupId>
            <artifactId>mysql-connector-java</artifactId>
            <scope>runtime</scope>
        </dependency>

		<!-- SQL Service需要添加的依赖 -->
		<dependency>
            <groupId>com.microsoft.sqlserver</groupId>
            <artifactId>mssql-jdbc</artifactId>
            <scope>runtime</scope>
        </dependency>

		<!-- Orcale数据库需要添加的依赖 -->
        <dependency>
            <groupId>com.oracle.ojdbc</groupId>
            <artifactId>ojdbc8</artifactId>
            <scope>runtime</scope>
        </dependency>
        <!-- Orcale数据库需要添加的依赖 -->
        <dependency>
            <groupId>com.oracle.ojdbc</groupId>
            <artifactId>ojdbc8</artifactId>
            <scope>runtime</scope>
        </dependency>

		<!-- MongoDB数据库依赖 -->
		<dependency>
			<groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-data-mongodb</artifactId>
		</dependency>		

		<!-- neo4j数据库依赖 -->
		<dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-data-neo4j</artifactId>
        </dependency>

		<!-- redis数据库依赖 -->
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-data-redis</artifactId>
        </dependency>

		<!-- Mybatis依赖 -->
		<dependency>
            <groupId>org.mybatis.spring.boot</groupId>
            <artifactId>mybatis-spring-boot-starter</artifactId>
            <version>1.3.0</version>
        </dependency>

		<!-- lombok包 -->
        <dependency>
            <groupId>org.projectlombok</groupId>
            <artifactId>lombok</artifactId>
            <version>1.18.4</version>
            <scope>provided</scope>
        </dependency>
	
		<!-- WebFlux依赖 -->
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-webflux</artifactId>
        </dependency>

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

		<!-- pom引入打包本地jar插件 -->
		<!-- 解决Spring Boot项目打成jar包的时候无法引入本地的jar -->
		<plugin>
			<groupId>org.springframework.boot</groupId>
             <artifactId>spring-boot-maven-plugin</artifactId>
             <configuration>
            	<includeSystemScope>true</includeSystemScope>
             </configuration>
		</plugin>


```