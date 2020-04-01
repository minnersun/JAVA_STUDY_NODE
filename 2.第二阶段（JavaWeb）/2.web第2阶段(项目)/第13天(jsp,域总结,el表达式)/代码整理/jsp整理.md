## jsp学习



### page指令

> import
>
> > 导入包
>
> session
>
> > 指明当前页面是否内置session对象
>
> buffer
>
> > out隐式对象所指明的缓冲区的大小，默认大小8kb
>
> errorPage
>
> > 如果页面出错将要跳转到的页面
>
> isErrorPage
>
> > 指定该页面是否为错误处理页面
>
> pageEncoding="utf-8"
>
> > 当前页面的编码格式（序列化和反序列化的编码格式）
>
> contentType="text/html; charset=utf-8"
>
> > 想页面输出时的编码格式



### jsp九大隐式对象

> page
>
> > 代表当前的servlet对象
>
> request
>
> > 得到用户的请求信息
>
> response
>
> > 服务器向客户的回应信息
>
> session
>
> > 用来保存用户的信息
>
> config
>
> > 代表当前servlet对象的配置信息，可以得到初始化参数
>
> application
>
> > 代表web应用的对象
>
> out
>
> > 代表输出缓冲区的对象
>
> exception
>
> > jsp页面发生异常，错误信息的对象
>
> pageContext
>
> > 代表当前页面上下文的对象

### pageContext详解

##### 可以作为其他隐式对象的入口

> > getException方法返回exception隐式对象
> > getPage方法返回page隐式对象
> > getRequest方法返回request隐式对象
> > getResponse方法返回response隐式对象
> > getServletConfig方法返回config隐式对象
> > getServletContext方法返回application隐式对象
> > getSession方法返回session隐式对象
> > getOut方法返回out隐式对象

##### 作为域对象使用

​	仅在当前页面有效

> setAttribute(String name,Object Value);
>
> getAttribute(String name);
>
> removeAttribute(String name)；
>
> getAttributeNames();

##### 设置/获取其他作用域的值

> setAttribute(String name,Object value,int scope);
>
> getAttribute(String name,int scope);
>
> removeAttribute(String name,int scope);
>
> getAttributeNamesInScope(scope);

##### scope可选值

> PageContext.APPLICATION_SCOPE
>
> > 代表servletContext域
>
> PageContext.SESSION_SCOPE
>
> > 代表session域
>
> PageContext.REQUEST_SCOPE
>
> > 代表request域
>
> PageContext.PAGE_SCOP
>
> > 代表pagecontext域

##### 自动搜寻

> findAttribute(String name);
>
> > 从小到大搜素

##### 请求转发

> pageContext.forward(location);





### 四大作用域

##### ServletContext  ---- application ---- web应用

> 生命周期
>
> > web应用加载，ServletContext对象产生
> >
> > web应用移除服务器或者服务关闭
>
> 作用范围
>
> > 整个web应用
>
> 主要功能
>
> > web应用范围内共享数据

##### Session ------ session ----会话范围

>  生命周期
>
> > 在调用request.getSession()时,session对象产生
> >
> > 服务器关闭，session超过最大生命时长，session.invalidate()主动释放，时关闭
>
> 作用范围
>
> > 整个会话范围
>
> 主要功能
>
> > 整个会话范围内共享数据

##### request  ---- request --- 请求

> 生命周期
>
> > 请求链开始的时候，request对象产生
> >
> > 请求链结束的时候，request对象销毁
>
> 作用范围
>
> > 整个请求链
>
> 主要功能
>
> > 在整个请求连中共享数据

##### pageContext --- pageContext --- jsp

> 生命周期
>
> > 在访问jsp页面的时候，pageContext对象产生
> >
> > 在页面关闭的时候，pageContext对象销毁
>
> 作用范围
>
> > 整个jsp页面
>
> 主要功能
>
> > 在整个jsp页面范围内共享数据







