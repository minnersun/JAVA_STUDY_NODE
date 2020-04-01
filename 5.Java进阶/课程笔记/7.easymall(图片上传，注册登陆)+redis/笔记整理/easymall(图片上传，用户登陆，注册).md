## easymall+redisAM

----

### 图片上传系统（nginx维护静态文件）

> 图片从浏览器传递给后台服务提供者,将其解析判断,生成存储路径
>
> 存放到nginx的访问目录中.提供的url地址要与存储路径对应
>
> `http://image.jt.com/upload/****.jpg`
>
> `C:\img\upload\****.jpg`

### easymall-img-service

### 接口文件

| 后台接收地址 | /pic/upload                                                  |
| ------------ | ------------------------------------------------------------ |
| 请求方式     | Post                                                         |
| 请求参数     | MultipartFile pic 包含了图片数据的二进制对象,所有图片相关内容就都在这个对象中包含 |
| 返回数据     | PicUploadResult对象的json,结构是:   String url 生成的访问图片路径   Integer error 上传成功和失败的标志   0表示无错误 1表示有错误 |
| 备注         | 获取图片数据,在逻辑代码中实现各种判断校验之后,生成2个路径   一个是存盘路径,一个是返回的访问地址url |



#### pom.xml

> - 继承parent
> - 依赖common(VO视图对象PicUploadResult)
>
> > version与依赖版本统一，使用以来类自带的变量

```xml
		<dependency>
		 	<groupId>cn.tedu</groupId>
		 	<artifactId>easymall-common-resources</artifactId>
		 	<!-- project.version是maven提供的一个隐藏的properties -->
		 	<!-- 使用当前工程的version数据 common中默认隐藏版本变量-->
		 	<version>${project.version}</version>
		</dependency>

```



#### src/main/resourses

##### application.properties

```properties
server.port=10002
#eureka client
spring.application.name=imgservice
eureka.client.serviceUrl.defaultZone=http://localhost:8888/eureka

#properties for img upload
url.path=http://image.jt.com/
disk.path=C:/img
```



#### src/main/java

##### cn.tedu

###### StarterImgService.java

```java
package cn.tedu;

@SpringBootApplication
@EnableEurekaClient
public class StarterImgService {
	public static void main(String[] args) {
		SpringApplication.run(StarterImgService.class, args);
	}
}
```

##### cn.tedu.pic.controller

###### PicController.java

```java
package cn.tedu.pic.controller;

@RestController
public class PicController {
	//注入业务层
	@Autowired
	private PicService picService;
	//图片上传
	@RequestMapping("pic/upload")
	public PicUploadResult imgUp(MultipartFile pic){
		//在业务层处理图片的上传和数据的生成返回逻辑
		return picService.imgUp(pic);
	}
}

```

##### cn.tedu.pic.service

###### PicService.java

> 获取网页地址和需要存储到的磁盘地址
>
> > 在配置文件中已经做了
>
> 1.判断图片是否合法
>
> > 以`.jpg	.png	`等结尾
> >
> > > 获取上传文件的源文件名称（`getOriginalFilename`）
> > >
> > > 截取文件后缀
> > >
> > > 判断是否合法
>
> 2.生成一个共用路径
>
> 3.根据公用路径，访问静态文件
>
> > 直接调用工具类
> >
> > > 根据路径生成文件夹
> > >
> > > > 判断路径是否存在
> > > >
> > > > 生成的文件夹有多级目录
> > > >
> > > > > 防止一个文件夹中出现大量文件，导致查询效率低下
>
> 4.重命名文件
>
> > 使用工具类(`randomUUID`)
>
> 5.将pic输出到磁盘已建好的目录中
>
> > `pic.transferTo(new File(diskDir + fileName));`
> >
> > 需要判断是否成功
>
> 6.生成url地址
>
> > 网页可以直接访问静态图片的地址
>
> 返回数据

```java
package cn.tedu.pic.service;

@Service
public class PicService {
	//读取properties中的路径前缀 http://image.jt.com/ c:/img
	@Value("${url.path}")
	private String urlPath;
	@Value("${disk.path}")
	private String diskPath;
	public PicUploadResult imgUp(MultipartFile pic) {
		//准备一个空数据对象，用于接受处理的数据
		PicUploadResult result=new PicUploadResult();
		//1.获取图片的后缀,判断是否合法
			//图片源文件名称
		String oldName=pic.getOriginalFilename();
			//oldName="penguine.jpg"
		String extName=oldName
            .substring(oldName.lastIndexOf("."));
			//extName=".jpg"|".png"
			//通过正则表达式判断合法 只要是jpg,png,gif其中一个
		if(!extName.matches(".(jpg|png|gif)")){
			//if进入,说明后缀不合法
			result.setError(1);//表示上传失败
			return result;
		}
		//2.生成一个公用路径 /upload/3/d/2/2/d/g/h/j/
			//直接调用upload的路径生成器,根据图片名称生成路径
			//fileName 图片名称,upload是前缀
			//根据图片名称字符串,生成hash散列的8位字符的字符串
			//3d22dghj,将其截取/3/d/2/2/d/g/h/j/
			//根据传入的前缀名称 拼接形成公用文件夹结构名称
			///upload/3/d/2/2/d/g/h/j/
		String dir="/"+UploadUtil.
				getUploadPath(oldName, "upload")+"/";
		//3.根据公用路径,根据nginx访问的静态文件 位置c:/img生成文件夹
			//c:/img/upload/3/d/2/2/d/g/h/j/,防止一个文件夹中存在大量的图片
			//导致查询效率低下
		String diskDir=diskPath+dir;//将图片数据输出到这个文件夹
			//创建文件夹,需要先判断
		File _dir=new File(diskDir);
		if(!_dir.exists()){//如果不存在,需要先创建
			_dir.mkdirs();
		}
			//if没有进入,说明已经存在,直接使用,将pic中的数据
			//输出保存到文件夹,重命名的文件名称
		//4.重命名文件
			String fileName=UUID.randomUUID().toString()+extName;
			//5f0d34dc-157f-49ba-ad39-1b28927ba6ae_1005714.jpg
		//5.将pic输出为 _dir中的一个fileName,名称的文件
			try{
			//将对象的数据,按照指定的file的路径值,输出到磁盘中
			//生成一个文件
			pic.transferTo(new File(diskDir+fileName));
			}catch(Exception e){
				e.printStackTrace();
				result.setError(1);
				return result;
			}
		//6.生成url地址 http://image.jt.com/upload/1/2/d/3/d/3/d/3/
		//5f0d34dc-157f-49ba-ad39-1b28927ba6ae_1005714.jpg
			String urlName=urlPath+dir+fileName;
			result.setUrl(urlName);
		return result;
	}

}

```





### 用户系统

### easymall-user-service

### 接口文件

| js请求地址 | http://www.easymall.com/user/checkUserName                   |
| ---------- | ------------------------------------------------------------ |
| 后台接收   | /user/manage/checkUserName                                   |
| 请求方式   | Post                                                         |
| 请求参数   | String userName                                              |
| 返回数据   | 返回SysResult对象的json,其结构:Integer status; 200表示用户名已存在,其他表示可用String msg;成功返回 “ok”,失败返回其他信息Object data;根据需求携带其他数据 |

#### pom.xml

> 略

#### src/main/resourses

##### application.properties

> 端口10003
>
> 连接数据库
>
> 更改数据源
>
> mybatis的配置
>
> eureka客户端配置

```properties
server.port=10003

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
spring.application.name=userservice
eureka.client.serviceUrl.defaultZone=http://localhost:8888/eureka

```

##### mapper

###### UserMapper.xml

```xml
<?xml version="1.0" encoding="UTF-8" ?>
<!DOCTYPE mapper PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN"
"http://mybatis.org/dtd/mybatis-3-mapper.dtd">
<mapper namespace="cn.tedu.user.mapper.UserMapper">
	<select id="checkUsername" parameterType="String" 
	resultType="int">
		select count(user_id) from t_user where
		user_name=#{userName}
	</select>
	<insert id="saveUser" parameterType="User">
		insert into t_user (
		user_id,user_name,user_password,
		user_nickname,user_email,user_type
		) values (
		#{userId},#{userName},#{userPassword},
		#{userNickname},#{userEmail},#{userType}
		)
	</insert>
</mapper> 
```





#### src/main/java

##### cn.tedu

###### StarterUserService.java

```java
package cn.tedu;

@SpringBootApplication
@EnableEurekaClient
@MapperScan("cn.tedu.user.mapper")
public class StarterUserService {
	public static void main(String[] args) {
		SpringApplication.run(StarterUserService.class, args);
	}
	//在启动类中可以按照一个配置类来编写所有需要的配置内容
}
```

##### cn.tedu.user.controller

###### UserController.java

````java
package cn.tedu.user.controller;

@RestController
@RequestMapping("user/manage")
public class UserController {
	@Autowired
	private UserService userService;
	//校验用户名是否存在
	@RequestMapping("checkUserName")
	public SysResult checkUsername(String userName){
		//控制层判断反回数据
		int exist=userService.checkUsername(userName);
		if(exist==0){//不存在,可用
			return SysResult.ok();//status=200
		}else{//存在,不可用
			return SysResult.build(201, "", null);
		}
	}
    
	//注册表单提交
	@RequestMapping("save")
	public SysResult saveUser(User user){
		//判断成功失败结构
		try{
			userService.saveUser(user);
			return SysResult.ok();
		}catch(Exception e){
			e.printStackTrace();
			return SysResult.build(201, "", null);
		}
	}
}
````

##### cn.tedu.user.service

###### UserService.java

```java
package cn.tedu.user.service;

@Service
public class UserService {
	@Autowired
	private UserMapper userMapper;
	public int checkUsername(String userName) {
		//select count(user_id)
		return userMapper.checkUsername(userName);
	}
	public void saveUser(User user) {
		//加密password
		String md5Password=
		MD5Util.md5(user.getUserPassword());
		user.setUserPassword(md5Password);
		//登录比对校验数据的userPassword也加密比对
		//补充userId
		user.setUserId(UUID.randomUUID().toString());
		userMapper.saveUser(user);
	}
}

```

##### cn.tedu.user.mapper

###### UserMapper.java

```java
package cn.tedu.user.mapper;

public interface UserMapper {

	int checkUsername(String userName);

	void saveUser(User user);

}

```









