## SpringMVC笔记

------

#### SpringMVC流程图详见课前笔记

> 

### 在配置文件中配置SpringMVC

###### web.xml

````xml
  <!-- 配置前端控制器 -->
  <servlet>         
  	<servlet-name>springmvc</servlet-name>
  	<servlet-class>org.springframework.web.servlet.DispatcherServlet</servlet-class>
  	
      <!-- 配置SpringMVC核心文件的位置  默认去WEB-INF下寻找--> 
  	<init-param>
  		<param-name>contextConfigLocation</param-name>
  		<param-value>classpath:/springmvc-servlet.xml</param-value>
  	</init-param>
      
  </servlet>

  <servlet-mapping>
  	<servlet-name>springmvc</servlet-name>
  	<url-pattern>*.action</url-pattern>		<!-- 所有请求都会被拦截	-->
  </servlet-mapping>
  
````

###### springmvc-servlet.xml

````xml
<!-- 配置映射关系 -->
<bean name="/hello.action" class="cn.tedu.demo.FirstController"></bean>

<!--  配置视图解析器 -->
<bean class="org.springframework.web.servlet.view.InternalResourceViewResolver">
	<property name="prefix" value="/WEB-INF/"></property>
	<property name="suffix" value=".jsp"></property>
</bean>
````

###### cn.tedu.demo.FirstController

```java
package cn.tedu.demo;

public class FirstController implements Controller{
	
	public ModelAndView handleRequest(HttpServletRequest arg0,
			HttpServletResponse arg1) throws Exception {
		// 处理数据和界面
		ModelAndView mav = new ModelAndView();
		// 添加数据
		mav.addObject("msg1", "hello");
		mav.addObject("msg2", "springmvc");
		// 设置界面
		mav.setViewName("first");//WEB-INF/first.jsp
		return mav;
    }
}
```

###### first.jsp

```jsp
<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>

<!DOCTYPE HTML>
<html>
  <head>
  </head>
  
  <body>
  	welcome
  	<hr>
 		<p>${msg1}</p>
 		<p>${msg2}</p>
  </body>
</html>
```



### 使用注解方式配置SpringMVC

###### web.xml

```xml-dtd
  <!-- 配置前端控制器 -->
  <servlet>
  	<servlet-name>springmvc</servlet-name>
  	<servlet-class>org.springframework.web.servlet.DispatcherServlet</servlet-class>
  	<!-- 配置SpringMVC核心文件的位置  默认去WEB-INF下寻找-->
  	<init-param>
  		<param-name>contextConfigLocation</param-name>
  		<param-value>classpath:/springmvc-servlet.xml</param-value>
  	</init-param>
  </servlet>
  <servlet-mapping>
  	<servlet-name>springmvc</servlet-name>
  	<url-pattern>*.action</url-pattern>
  </servlet-mapping>
```

###### springmvc-servlet.xml

```xml
<!-- 开启包扫描 -->
<context:component-scan base-package="cn.tedu.demo"></context:component-scan>
<context:annotation-config></context:annotation-config>
<!-- 开启mvc注解配置  -->
<mvc:annotation-driven></mvc:annotation-driven>

<!--  配置视图解析器 -->
<bean class="org.springframework.web.servlet.view.InternalResourceViewResolver">
	<property name="prefix" value="/WEB-INF/"></property>
	<property name="suffix" value=".jsp"></property>
</bean>
```

###### cn.tedu.demo.FirstController

```java
package cn.tedu.demo;

@Controller  //将FirstController交由Spring进行管理
public class FirstController {
	
	@RequestMapping("/test01.action")
	public ModelAndView test01(){
		ModelAndView mav = new ModelAndView();
		
		mav.addObject("msg1", "pig");
		mav.addObject("msg2", "fdsfd");
		
		mav.setViewName("first");
		return mav;
	}
	
	@RequestMapping("/test02.action")
	public String test02(){
		// 返回的字符串代表的是View的名称
		return "first";
	}
}
```

###### first.jsp

```jsp
<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>

<!DOCTYPE HTML>
<html>
  <head>
 </head>
  
  <body>
  	welcome
  	<hr>
 		<p>${msg1}</p>
 		<p>${msg2}</p>
  </body>
</html>
```



### SpringMVC细节

##### 可以在类上添加映射（@RequestMapping）

> 在类上添加@RequestMapping
>
> > 在方法上添加@RequestMapping

```java
@Controller  //将FirstController交由Spring进行管理
@RequestMapping("/Detail02")
public class FirstController {
	
	@RequestMapping("/test01.action")
	public ModelAndView test01(){
		ModelAndView mav = new ModelAndView();
		
		mav.addObject("msg1", "pig");
		mav.addObject("msg2", "fdsfd");

		mav.setViewName("first");
		return mav;
	}
}
```



##### @RequestMapping中的属性

> 可以配置多个路径
>
> > `@RequestMapping({"/test01.action","/test01*.action"})`
>
> params:有这些属性时被请求传过来时才能够被调用执行
>
> > `@RequestMapping(value="/test03.action",params={"username","password"})`
>
> method:method设置为POST时，只能接受POST的数据
>
> > `@RequestMapping(value="/test03.action",method=RequestMethod.POST)`
> >
> > > 常用POST，GET
>
> headers:只有传过来的请求包含相关头部时才会被调用（很少使用）
>
> > `@RequestMapping(value="/test03.action",headers={"host"})`



##### 注解属举例

> 指定要将当前处理器绑定到哪个访问路径上。
>
> 可以配置多个路径。

```java
@Controller  //将FirstController交由Spring进行管理
public class FirstController {
	
	@RequestMapping({"/test01.action","/test01*.action"})
	public ModelAndView test01(){
		ModelAndView mav = new ModelAndView();
		
		mav.addObject("msg1", "pig");
		mav.addObject("msg2", "fdsfd");
		
		mav.setViewName("first");
		return mav;
	}
}
```

###### params

> params
>
> > 有这些属性时被请求传过来时才能够被调用执行

```java
@Controller  //将FirstController交由Spring进行管理
public class FirstController {
	
	@RequestMapping(value="/test03.action",params={"username","password"})
	public ModelAndView test01(){
		ModelAndView mav = new ModelAndView();
		
		mav.addObject("msg1", "pig");
		mav.addObject("msg2", "fdsfd");
		
		mav.setViewName("first");
		return mav;
	}
}
```

###### method

> method=RequestMethod.POST
>
> > method设置为POST时，只能接受POST的数据

```java
@Controller  //将FirstController交由Spring进行管理
public class FirstController {
	
	@RequestMapping(value="/test03.action",method=RequestMethod.POST)
	public ModelAndView test01(){
		ModelAndView mav = new ModelAndView();
		
		mav.addObject("msg1", "pig");
		mav.addObject("msg2", "fdsfd");
		
		mav.setViewName("first");
		return mav;
	}
}
```

###### headers

> headers
>
> > 只有传过来的请求包含相关头部时才会被调用

```java
@Controller  //将FirstController交由Spring进行管理
public class FirstController {
	
	@RequestMapping(value="/test03.action",headers={"host"})
	public ModelAndView test01(){
		ModelAndView mav = new ModelAndView();
		
		mav.addObject("msg1", "pig");
		mav.addObject("msg2", "fdsfd");
		
		mav.setViewName("first");
		return mav;
	}
}
```



### 处理器方法支持的参数类型和返回值类型

##### 支持的方法参数类型

> HttpServletRequest
>
> > 代表当前请求的对象
>
> HttpServletResponse
>
> > 代表当前响应的对象
>
> HttpSession
>
> > 代表当前会话的对象
>
> WebRequest
>
> > SpringMVC提供的对象，相当于是request和session的合体，可以操作这两个域中的属性
>
> InputStream	OutputStream	Reader	Writer
>
> > 代表request中获取的输入流和response中获取的输出流
>
> @PathVariable
>
> > 可以将请求路径的指定部分获取赋值给指定方法参数
>
> @RequestParam
>
> > `public void test05(@RequestParam(value="nn") String nnn){
> > 		System.out.println(nnn);	
> > }`
>
> > 可以将指定请求参数赋值给指定方法参数，如果不写此注解，则默认会将同名的请求参数赋值给方法参数
>
> @CookieValue
>
> > 可以将请求中的指定名称的cookie赋值给指定方法参数
>
> @RequestHeader
>
> > 以将请求参数中的指定名称的头赋值给指定方法参数
>
> Model和ModelMap和java.util.Map
>
> > 向这些Model ModelMap Map中存入属性，相当于向模型中存入数据
>
> MultipartFile
>
> > 实现文件上传功能时，接收上传的文件对象

```java
package cn.tedu.demo;

@Controller  //将FirstController交由Spring进行管理
@RequestMapping("/cbt")
public class FirstController {

	@RequestMapping(value="/test01.action")
    // User中的属性在请求中要按照顺序发送
	public void test05(User user){
		System.out.println(user);	
	}
	
	@RequestMapping(value="/test02.action")
	public void test05(@RequestParam(value="nn") String nnn){
		System.out.println(nnn);	
	}
    
	@RequestMapping("/test03.action")
	public void test06(String[] like){
//		System.out.println(like);
		System.out.println(Arrays.toString(like));
	}
}

```



##### 支持的返回值类型

> ModelAndView
>
> > 可以返回一个ModelAndView对象，在其中封装Model和View信息
>
> View
>
> > 可以直接返回一个代表视图的View对象
>
> 字符串
>
> > 直接返回视图的名称
>
> void
>
> > 如果返回值类型是void，则会自动返回和当前处理器路径名相同的视图名
>
> @ResponseBody
>
> > 当方法被@ResponseBody修饰时，默认将返回的对象转为json写入输出
>
> 除以上之外返回的任何内容都会被当做模型中的数据来处理，而返回的视图名等同于当前处理器路径名

### 获取Request Response对象



### 获取请求参数

###### 通过request对象获取

```java
	@RequestMapping(value="/test03.action")
	public void test03(HttpServletRequest request,HttpServletResponse response){
		// 返回的字符串代表的是View的名称
		
		System.out.println(request);
		// 获取请求参数
		String username = request.getParameter("username");
		int age = Integer.parseInt(request.getParameter("age"));
		String password = request.getParameter("password");
		User user = new User(age,username,password);
		System.out.println(user);
//		System.out.println(response);
		System.out.println(username + "~~" +age);
	}
```

###### 直接接收请求参数

```java
	@RequestMapping(value="/test04.action")
	public void test04(String username,int age){
		// 返回的字符串代表的是View的名称
		System.out.println(username + "~~" +age);
        
	}
```

###### 自动封装请求参数信息到bean

```java
	@RequestMapping(value="/test05.action")
	public void test05(User user){	// 请求中的参数必须与bean中的属性一一对应
		System.out.println(user);
	}
```

###### 处理复杂类型

> FirstController.java

```java
	@RequestMapping(value="/test01.action")
	public void test05(User user){
		System.out.println(user);	
	}
```

> test2.jsp

```jsp
   <form action="${pageContext.request.contextPath}/cbt/test01.action" method="get">
  		年龄:<input type="text" name="age">
  		姓名:<input type="text" name="username">
  		密码:<input type="text" name="password">
  		狗龄:<input type="text" name="Dog.age">
  		狗名:<input type="text" name="Dog.name">
  		<input type="submit" value="提交">
  	</form>
```

###### 请求参数中的名称和属性名不同的处理@RequestParam

> value
>
> > 参数名字，即入参的请求参数名字，如value=“delId”
>
> required
>
> > 是否必须，默认是true，表示请求中一定要有相应的参数，否则将报400错误码；
>
> defaultValue
>
> > 默认值，表示如果请求中没有同名参数时的默认值

```java
	@RequestMapping(value="/test02.action")
	public void test05(@RequestParam(value="nn") String nnn){
		System.out.println(nnn);	
	}
```

###### 请求参数中存在多个同名参数

> FirstController.java

```java
	@RequestMapping("/test03.action")
	public void test06(String[] like){
//		System.out.println(like);
		System.out.println(Arrays.toString(like));
    }
```

> test3.jsp

```jsp
	<form action="${pageContext.request.contextPath}/cbt/test03.action" method="get">
  		<input type="checkbox" name="like" value="wzry"/>王者荣耀
  		<input type="checkbox" name="like" value="lol"/>英雄联盟
  		<input type="checkbox" name="like" value="dota"/>刀塔
  		<input type="checkbox" name="like" value="chij"/>绝地求生
  		<input type="submit" value="提交">
  	</form>
```



### 请求参数中的中文乱码

##### SpringMVC提供了过滤器用来解决全站乱码

> ==只能解决post请求的乱码==

###### web.xml

```xml
 <!-- 使用SpringMVC的过滤器来解决中文乱码问题 针对于post提交 get提交无效  -->
  <filter>
  	<filter-name>characterEncodingFilter</filter-name>
  	<filter-class>org.springframework.web.filter.CharacterEncodingFilter</filter-class>
  	<init-param>
  		<param-name>encoding</param-name>
  		<param-value>utf-8</param-value>
  	</init-param>
  </filter>

  <filter-mapping>
  	<filter-name>characterEncodingFilter</filter-name>
  	<url-pattern>/*</url-pattern>
  </filter-mapping>
```

> get请求的乱码处理
>
> > `System.out.println(new String(username.getBytes("iso8859-1"),"utf-8"));`

### 日期数据的处理

###### test2.jsp

```jsp
	<form action="${pageContext.request.contextPath}/cbrt/test02.action" method="get">
  		年龄:<input type="text" name="age">
  		姓名:<input type="text" name="username">
  		日期:<input type="date" name="tday">
  		<input type="submit" value="提交">
  	</form>	
```

###### FirstController.java

```java
	@InitBinder
	public void binding(ServletRequestDataBinder bind){
		// 设置日期默认的解析格式
		bind.registerCustomEditor(Date.class, new CustomDateEditor(new SimpleDateFormat("yyyy-MM-dd"), true));
	}
	
	@RequestMapping(value="/test02.action")
	public void test02(String username,int age,Date tday){
		// 2019-08-21    2019/08/21
		System.out.println(username);
		System.out.println(age);
		System.out.println(tday);
	}
```

### SpringMVC文件上传

###### 文件上传的三个必要条件

> 表单必须是Post提交
>
> 表单必须是enctype="multipart/form-data"
>
> 文件上传项必须有name属性

###### test2.jsp

```jsp
<form action="${pageContext.request.contextPath}/cbrt/test02.action" method="post" enctype="multipart/form-data">
  		年龄:<input type="text" name="age">
  		姓名:<input type="text" name="username">
  		<input type="file" name="fx">
  		
  		<input type="submit" value="提交">
</form>
```

###### springmvc-servlet.xml

> 在配置文件中配置文件上传工具

```xml
<!-- 配置文件上传 -->
<bean id="multipartResolver" class="org.springframework.web.multipart.commons.CommonsMultipartResolver">
	<!--  上传最大体积 -->
	<!-- <property name="maxUploadSize"></property> -->
	
	<!-- 上传占用最大内存大小 -->
	<!-- <property name="maxInMemorySize"></property> -->
	<!-- 上传后存放临时文件夹的位置 -->
	<!-- <property name="uploadTempDir"></property> -->
</bean>
```

###### FirstController.java

> 实现文件上传

```java
	@RequestMapping(value="/test02.action")
	public void test02(String username,int age,MultipartFile fx) throws IOException{
		// 向本地c盘存放客户端上传的文件	getOriginalFilename：得到文件的原始名称
		FileUtils.writeByteArrayToFile(new File("c://"+fx.getOriginalFilename()), fx.getBytes());
	}
```



### RESTFul风格的请求

> 普通的get请求
>
> > `Url:localhost:XXXX/addUser.action?name=tom&age=18`
>
> restfull风格的请求
>
> > Url:localhost:XXXX/addUser/tom/18.action

###### FirstController.java

```java
@Controller  //将FirstController交由Spring进行管理

public class FirstController {

	@RequestMapping("/restful/{username}/{age}.action")
	public void restful(@PathVariable String username,@PathVariable int age){
		System.out.println(username + "~" + age);
	}
}
```



### pringMVC中的重定向和转发的实现

```java
	// 转发
	@RequestMapping("/test01.action")
	public String test01(){
		return "forward:/index.jsp";
	}
	// 转发
	@RequestMapping("/test02.action")
	public String test02(){
		return "forward:/test01.action";
	}
	// 重定向
	@RequestMapping("/test03.action")
	public String test03(){
		return "redirect:/index.jsp";
	}
```

### SpringMVC中session的使用

> model中的域默认是request域
>
> 可以通过在类上加@SessionAttributes注解将他变为session域

```java
@Controller  //将FirstController交由Spring进行管理
@SessionAttributes("msg")
public class FirstController {
	// 四大作用域
	// request: 默认SpringMVC使用的就是request
	// session:可以在处理器的形参中直接获取HttpSession对象
			// 也可以先在处理器的形参中获取HttpServletRequest,然后再间接的获取session
	// servletContext:可以通过HttpServletRequest获取session，在获取servletContext
	// pageContext
	@RequestMapping("/test01.action")
	public String test01(Model model){
		model.addAttribute("msg", "red apple");
		return "first";
	}
}
```

### 异常处理

##### 为当前Controller配置错误处理

> 当前Controller出错时执行的方法
>
> > @ExceptionHandler

###### FirstController.java

```java
@Controller 
public class FirstController {	
	// 作为异常的拦截方法
	@ExceptionHandler
	public void exceptionDemo(Exception e){
		System.out.println(e.getMessage());
	}
}
```

##### 注解方式配置全局的错误处理

> 全局出现异常时使用此方法处理
>
> > @ControllerAdvice

###### FirstController.java

```java
@Controller 
@ControllerAdvice  //为全局配置异常拦截
public class FirstController {	
	// 作为异常的拦截方法
	@ExceptionHandler
	public void exceptionDemo(Exception e){
		System.out.println(e.getMessage());
	}
}
```

### 实现返回一段数据 - AJAX 

```java
	@RequestMapping("test07.action")
	@ResponseBody
	public Hero test07() throws IOException{
		Hero  hero = new Hero();
		hero.setAge(16);
		hero.setName("张三丰");
		hero.setJob("道士");
		
		return hero;
	}
```

