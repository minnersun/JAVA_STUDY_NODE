## easymall

------

### easymall项目结构

> parent
>
> > 维护管理easymall整个项目的资源版本统一
> >
> > 依赖版本,和资源
> >
> > 构建的各种插件
>
> common公用资源
>
> > 多个子工程 公用的代码,提取到一个公用工程被所有子工程依赖使用(工具类,模板类)
> >
> > 继承parent

### easymall的项目开发

> product商品系统
>
> > - 商品分页查询
> >
> > - 商品的单个查询
> >
> > - 商品的新增
> >
> > - 商品的更新
>
> 图片上传
>
> > 作为单独的微服务功能提供使用



## 开发系统准备工作

### parent

> 维护easymall中有资源版本统一
>
> > 包括 持久层,redis
> > elasticsearch,rabbitmq等内容

#### pom.xml

> - 修改packaging类型为pom
> - 继承springboot-parent
> - build插件(spring-boot-maven-plugin,source      略)
>
> - 子工程中公用的依赖,放到dependencies(略)
>
> - 扩充parent 的dependencyManagement
>
> > 子工程常用依赖
> >
> > > starter-web
> > >
> > > jdbc
> > >
> > > mybatis(1.3.0)

```xml
		<parent>
		  	<groupId>org.springframework.boot</groupId>
		  	<artifactId>spring-boot-starter-parent</artifactId>
		  	<version>1.5.9.RELEASE</version>
		</parent>

		<dependencyManagement>
				<dependencies>
					<dependency>
						<groupId>org.springframework.cloud</groupId>
						<artifactId>spring-cloud-dependencies</artifactId>
						<version>Edgware.RELEASE</version>
						<type>pom</type>
						<scope>import</scope>
					</dependency>
				</dependencies>
		</dependencyManagement>

		<dependency>
					<groupId>org.mybatis.spring.boot</groupId>
					<artifactId>mybatis-spring-boot-starter</artifactId>
					<version>1.3.0</version>
		</dependency>

		<!-- druid连接数据源 -->
		<dependency>
				<groupId>com.alibaba</groupId>
				<artifactId>druid</artifactId>
				<version>1.0.14</version>
		</dependency>

```





### common-resources

> 提取到一个公用工程，所有子工程可以直接继承使用.

#### pom.xml

> + 继承父工程
> + common具备代码,需要starter-web支持(exclusions去除不需要的依赖资源略)

```xml
		<parent>
		  	<groupId>cn.tedu</groupId>
		  	<artifactId>easymall-parent</artifactId>
		  	<version>0.0.1-SNAPSHOT</version>
		</parent>

```

#### 拷贝课前资料的公用代码

> 课前资料-->springcloud-->easymall-->common-pojo工程
>
> 将src/main粘贴到当前自定义搭建的common中的src下



### javaBean规范

> entity
>
> > 所有属性严格对应一个表的字段结构
>
> domain
>
> > 具备一个业务领域的数据含义
> >
> > 一个表关联另一个表的类型
>
> vo
>
> > 视图对象，给js使用的数据
> >
> > 后台------》 前端js
>
> dto
>
> > 页面----》后台的javabean数据对象
>
> pojo
>
> > 以上所有的数据类型都叫pojo
> >
> > 是满足所有javabean规范类的统称







## 开发商品系统和功能

> 需要导入的工具类`easymall-common-resources`	提供了bean对象，和相关工具

> 唯一的一个维护商品相关功能子系统;数据层面的体现就是只有商品系统具备操作t_product表格的能力

### 接口文件

#### **1.1.1.** 首页商品全部分页查询接口文件

| js请求地址 | http://www.easymall.com/products/pageManage?page=1&rows=1    |
| ---------- | ------------------------------------------------------------ |
| 后台接收   | /product/manage/pageManage?page=1&rows=1                     |
| 请求方式   | Get                                                          |
| 请求参数   | Get提交参数 Integer page,Integer rows                        |
| 返回数据   | 根据查询结果封装2个数据到EasyUIResult对象中:Integer total:查询的总条数;List<Product> rows:查询分页的数据结果; |
| 备注       | 商品总数 SELECT COUNT(*) FROM t_product分页数据(第一页的5条数据) SELECT * FROM t_product LIMIT 0,5(第二页的5条数据) select * from t_product LIMIT 5,5(第三页的5条数据) select * from t_product LIMIT 10,5 |



#### **1.1.2.** 根据id查询单个商品

| js请求地址 | http://www.easymall.com/products/item/{productId} |
| ---------- | ------------------------------------------------- |
| 后台接收   | /product/manage/item/{productId}                  |
| 请求方式   | Get                                               |
| 请求参数   | 路径中携带的参数 String productid                 |
| 返回数据   | 返回查询的Product对象json字符串,在ajax中解析使用  |



#### **1.1.3.** 新增商品

| js请求地址 | http://www.easymall.com/products/save                        |
| ---------- | ------------------------------------------------------------ |
| 后台接收   | /product/manage/save                                         |
| 请求方式   | Post                                                         |
| 请求参数   | Product product对象接参 缺少id                               |
| 返回数据   | 返回SysResult对象的json,其结构:Integer status; 200表示成功,其他表示失败String msg;成功返回 “ok”,失败返回其他信息Object data;根据需求携带其他数据 |
| 备注       | 返回的SysResult对象是一个标准的和ajax对话的后台vo对象,它可以将当前的操作结果通过status传递给ajax,可以将操作中出现任何后台的消息,例如错误信息,以msg传递给前台,也可以将很多其他数据封装到data传递给前台使用 |

#### **1.1.4.** 商品的更新

| js请求地址 | http://www.easymall.com/products/update                      |
| ---------- | ------------------------------------------------------------ |
| 后台接收   | /product/manage/update                                       |
| 请求方式   | Post                                                         |
| 请求参数   | Product product                                              |
| 返回数据   | 返回SysResult对象的json,其结构:Integer status; 200表示成功,其他表示失败String msg;成功返回 “ok”,失败返回其他信息Object data;根据需求携带其他数据 |







### easymall-product-service

#### pom.xml

> - 继承easymall-parent
> - 依赖common-resources
> - starter-web(依赖资源从common中传递过来)
> - 持久层相关依赖(jdbc,mysql,mybatis,druid)
> - 微服务角色(服务提供者eureka-client)

```xml
		<!-- 继承easymall-parent -->
		<parent>
		  	<groupId>cn.tedu</groupId>
		  	<artifactId>easymall-parent</artifactId>
		  	<version>0.0.1-SNAPSHOT</version>
		</parent>
		<!-- 依赖common-resources -->
		<dependency>
		 	<groupId>cn.tedu</groupId>
		 	<artifactId>easymall-common-resources</artifactId>
		 	<version>0.0.1-SNAPSHOT</version>
	  	</dependency>
		<!-- jdbc -->
			<dependency>
		  		<groupId>org.springframework.boot</groupId>
		  		<artifactId>spring-boot-starter-jdbc</artifactId>
		  	</dependency>
		<!-- mysql -->
		  	<dependency>
		  		<groupId>mysql</groupId>
		  		<artifactId>mysql-connector-java</artifactId>
		  	</dependency>
		<!-- mybatis -->
		  	<dependency>
					<groupId>org.mybatis.spring.boot</groupId>
					<artifactId>mybatis-spring-boot-starter</artifactId>
			</dependency>
		<!-- druid -->
			<dependency>
		  		<groupId>com.alibaba</groupId>
				<artifactId>druid</artifactId>
			</dependency>
		<!-- eureka-client -->
		<dependency>
		    	<groupId>org.springframework.cloud</groupId>
		    	<artifactId>spring-cloud-starter-eureka</artifactId>
		    </dependency>

```



#### src/main/resourses

##### application.properties

```properties
server.port=10001

spring.datasource.driverClassName=com.mysql.jdbc.Driver
spring.datasource.url=jdbc:mysql:///easydb
spring.datasource.username=root
spring.datasource.password=root
#druid datasource
spring.datasource.type=com.alibaba.druid.pool.DruidDataSource
#mybatis
mybatis.mapperLocations=classpath:mapper/*.xml
mybatis.typeAliasesPackage=com.jt.common.pojo
mybatis.configuration.mapUnderscoreToCamelCase=true
mybatis.configuration.cacheEnabled=false
#eureka client
spring.application.name=productservice
eureka.client.serviceUrl.defaultZone=http://localhost:8888/eureka

```

##### mapper

###### ProductMapper.xml

````xml
<?xml version="1.0" encoding="UTF-8" ?>
<!DOCTYPE mapper PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN"
"http://mybatis.org/dtd/mybatis-3-mapper.dtd">
<mapper namespace="cn.tedu.product.mapper.ProductMapper">
	<!-- total; -->
	<select id="queryTotal" resultType="int">
		select count(product_id) from t_product;
	</select>
	<!-- productList  -->
	<select id="queryPage" resultType="Product">
		select * from t_product limit #{start},#{rows};
	</select>
	<!-- 根据id查询单个商品,1 row -->
	<select id="queryById" parameterType="String" 
	resultType="Product">
		select * from t_product where
		product_id=#{productId};
	</select>
	<insert id="saveProduct" parameterType="Product">
		insert into t_product (
		product_id,
		product_price,
		product_name,
		product_category,
		product_description,
		product_num,
		product_imgurl
		) values (
		#{productId},
		#{productPrice},
		#{productName},
		#{productCategory},
		#{productDescription},
		#{productNum},
		#{productImgurl}
		)
	</insert>
	<update id="updateProduct" parameterType="Product">
		update t_product set 
		product_name=            #{productName},
		product_price=           #{productPrice},
		product_num=             #{productNum},
		product_description=     #{productDescription},
		product_category=        #{productCategory},
		product_imgurl=          #{productImgurl}
		where product_id=        #{productId};
	</update>
</mapper> 
````





#### src/main/java

##### cn.tedu

###### StarterProductService.java

```java
package cn.tedu;

@SpringBootApplication
@EnableEurekaClient
@MapperScan("cn.tedu.product.mapper")
public class StarterProductService {
	public static void main(String[] args) {
		SpringApplication.run(StarterProductService.class, args);
	}
	//在启动类中可以按照一个配置类来编写所有需要的配置内容
}

```

##### cn.tedu.product.controller

###### ProductController.java

```java
package cn.tedu.product.controller;

@RestController 
@RequestMapping("/product/manage")
public class ProductController {
	@Autowired
	private ProductService productService;
    
    
	//商品的分页查询
	@RequestMapping("pageManage")
	public EasyUIResult productPageQuery(Integer page,
			Integer rows){
		//控制层直接调用业务层方法获取返回结果
		EasyUIResult result=productService.
				pageQuery(page,rows);
		return result;
	}
    
    
	//商品的单个查询
	@RequestMapping("{haha}/{productId}")
	//在路径中如果某个阶段的字符串想要接收成为参数变量的值
	//需要在springmvc使用{变量名称}
	//url:/product/manage/1/2 haha=1 productId=2
	public Product queryById(@PathVariable String haha
			,@PathVariable String productId){
		//haha,productId 在请求url中以 ?key=value
		//post 请求体 key=value
		System.out.println("haha:"+haha);
		System.out.println("productId:"+productId);
		Product product=productService.queryById(productId);
		return product;
	}
    
    
	//商品的新增
	@RequestMapping("save")
	public SysResult saveProduct(Product product){
		//成功与失败
		try{
			productService.saveProduct(product);//缺少了id
			//表示成功 SysResult (200,"",null)
			return SysResult.ok();//{"status":200,"msg":"ok","data":null}
		}catch(Exception e){
			//e.message
			e.printStackTrace();
			//201,e.message,null
			return SysResult.build(201, e.getMessage(), null);
			//js data.msg data.status ,data.data
		}
	}
    
    
	//商品的更改
	@RequestMapping("update")
	public SysResult updateProduct(Product product){
		//包含了所有的商品字段属性值
		try{
			productService.updateProduct(product);
			return SysResult.ok();
		}catch(Exception e){
			e.printStackTrace();
			return SysResult.build(201, e.getMessage(), null);
		}
	}
}
```

##### cn.tedu.product.service

###### ProductService.java

```java
package cn.tedu.product.service;

@Service
public class ProductService {
	@Autowired
	private ProductMapper productMapper;
	public EasyUIResult pageQuery(Integer page, Integer rows) {
		//1,准备好一个返回的对象
		EasyUIResult result=new EasyUIResult();
		//2,封装total,查询商品总数
		int total=productMapper.queryTotal();
		//select count(product_id)
		result.setTotal(total);//js 计算分页数量
		// total%rows==0? total/rows:(total/rows+1)
		//3,封装另一个数据rows EasyUIResuolt属性
		int start=(page-1)*rows;
		List<Product> pList=productMapper.queryPage
				(start,rows);
		result.setRows(pList);
		return result;
	}
    
    
	public Product queryById(String productId) {
		//TODO 添加缓存逻辑 数据未必直接来自于数据库
		return productMapper.queryById(productId);
	}
    
    
	public void saveProduct(Product product) {
		//product 缺少productId补齐数据
		String productId=UUID.randomUUID().toString();
		product.setProductId(productId);
		//TODO 引入缓存的逻辑
		productMapper.saveProduct(product);
		//insert into t_product (表格字段) values
		//(#{各种参数属性值})
	}
    
    
	public void updateProduct(Product product) {
		// TODO 缓存锁解决高并发的资源争抢
		productMapper.updateProduct(product);
	}

}
```

##### cn.tedu.product.mapper

###### ProductMapper.java

```java
package cn.tedu.product.mapper;

public interface ProductMapper {

	int queryTotal();

	List<Product> queryPage(@Param("start")int start, @Param("rows")Integer rows);

	Product queryById(String productId);

	void saveProduct(Product product);

	void updateProduct(Product product);

}

```