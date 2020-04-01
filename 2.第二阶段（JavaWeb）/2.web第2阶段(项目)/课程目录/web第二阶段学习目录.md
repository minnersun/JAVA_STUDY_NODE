## web第二阶段代码整理

-----

### 第十天

> 网页注册代码

### 第十一天

> 配置信息，路径，ajax

> 全局配置信息
>
> 当前配置信息

> 获取当前配置信息（`this.getServletConfig().getServletContext()`）
>
> 获取全局配置信息（`this.getServletContext()`）

> ServletContext作为域对象使用

> 路径
>
> > 相对路径
> >
> > 绝对路径
> >
> > 全路径
> >
> > 类加载获取路径
> >
> > ServletContext获取路径

> ajax

### 第十二天

> cookie，session

> 小项目
>
> > 添加记住用户的功能
> >
> > 添加登陆功能
> >
> > 验证码校验
> >
> > 注销功能
>
> 将session的内容存放到cookie中

第十三天

> page指令（7个）

> jsp九大隐式对象

> pageContext详解
>
> > 可以作为其他隐式对象的入口
> >
> > 作为域对象使用
> >
> > 设置获取其他的域（scope可选值）
> >
> > 自动搜寻（`findAttribute(String name);`）
> >
> > 请求转发（`pageContext.forward(location);`）

> 四大作用域
>
> > `ServletContext  -–-– application -–-– web应用`
> >
> > `Session -–-–-– session -–-–会话范围`
> >
> > `request  ---- request --- 请求`
> >
> > `pageContext --— pageContext --— jsp`

### 第十四天

> jstl标签库
>
> > jsp引入jstl（`<%@taglib uri="" prefix=""%>`）
>
> jstl标签库中常用标签

> MVC
>
> > 详见笔记

### 第十五天

> Filter过滤器，MD5加密算法，Listener监听器

> Filter过滤器
>
> > Fileter的作用
> >
> > Filter的实现
> >
> > Filter的配置
> >
> > 责任链模式
>
> 修改easymall	------	添加过滤器

> MD5加密算法

> Listener监听器
>
> > 监听器的分类（四类八种）
> >
> > 监听器的案例（对日志进行相关操作）

### 第十六天

> 

> log4j
>
> > 导入jar包
> >
> > 引入properties文件
>
> 日志的优先级
>
> > off 最高等级， 用于关闭所有日志记录。
> >
> > fatal 指出每个严重的错误事件将会导致应用程序的退出。
> >
> > error 指出虽然发生错误事件， 但仍然不影响系统的继续运行。
> >
> > warn 表明会出现潜在的错误情形。
> >
> > info 一般和在粗粒度级别上， 强调应用程序的运行全程。
> >
> > debug 一般用于细粒度级别上， 对调试应用程序非常有帮助。
> >
> > all 最低等级， 用于打开所有日志记录
>
> 日志的输出目的地Appender
>
> 日志输出格式 layout
>
> 日志输出格式的正则
>
> Log4j实现

> 事务
>
> > 事务是指逻辑上的一组操作，这组操作要么一起成功，要么一起失败
>
> 手动管理事务 - sql方式
>
> 手动管理事务 - JDBC方式（c3p0提供的相关api）
>
> 通过jdbc手动控制事务实现转账 - 使用保存点
>
> 事务的四大特性
>
> > 原子性(Atomicity)
> >
> > 一致性(consistency)
> >
> > 隔离性(Isolation)
> >
> > 持久性(Durability)
>
> 隔离性                                                                                                                                                        
>
> > 线程安全分析
> >
> > 一个线程读，一个线程写可能会出现的问题(详细可看笔记)
> >
> > 数据库隔离级别
> >
> > 修改数据库隔离级别
> >
> > 查看当前窗口隔离级别:(select @@tx_isolation;)
>
> serializable隔离级别之下的情景分析--—锁机制
>
> > 共享锁和排它锁
> >
> > 表级锁和行级锁
>
> 死锁

