## 笔记目录

-------

### 第一天笔记

> xml文件的导入，生成方法
>
> jar包分析（.class	源代码	javadoc）
>
> 入门案例
>
> 别名标签（alias）
>
> 静态工厂	
>
> >  `<bean id="person" class="cn.tedu.factory.PersonStaticFactory" factory-method="getPerson"></bean>` 
>
> 实例工厂
>
> > `<bean id="calendarFactory" class="cn.tedu.factory.CalendarFactory"></bean>`
> >
> > `<bean id="calendar" factory-bean="calendarFactory" factory-method="getCalendar"/>`
>
> Spring创建工厂对象（不常用）
>
> 单例(singleton)和多例(prototype)
>
> 懒加载机制（lazy-init）
>
> bean中配置初始化和销毁时的方法
>
> > `<bean id="prodDao" class="cn.tedu.dao.ProdDao" init-method="initFunc" destroy-method="destroyFunc"></bean>`
>
> IOC(DI):控制反转（依赖注入）
>
> 自动装配（autowire）







### 第二天笔记

> 构造方法装配
>
> > `<constructor-arg>	</constructor-arg>`
>
> 元注解
>
> > @Target	表示注解可以使用的位置
> >
> > @Retention	表示注解作用范围
> >
> > @Documented 	注解是否要提取到文档中
> >
> > @Inherited	注解是否有继承性
>
> 反射注解
>
> > `isAnnotationPresent(Class)`:	boolean
> >
> > `getAnnotation()`:	annotation
> >
> > `getAnnotations()`:	annotation[ ]
>
> Spring注解方式实现IOC
>
> > @Controller	 @Service	@Repository	@Component
> >
> > > Controller	用于标识控制器
> > >
> > > Service	用于标识服务类
> > >
> > > Repository	用于标识持久层的类
>
> > 开启包扫描
> >
> > `<context:component-scan base-package="cn.tedu.beans"></context:component-scan>`
>
> 使用注解方式实现DI：@Value	(不推荐使用)
>
> 引入配置文件信息
>
> > `<context:property-placeholder location="classpath:/person-data.properties"/>`
>
> 开启包扫描，开启注解配置方式
>
> > `<context:annotation-config></context:annotation-config>`
>
> > @Autowired	
> >
> > > 实现自定义bean类型的属性注入
> >
> > @Qualifier	@Resource	
> >
> > > 自己设置bean容器的id
> >
> > @Scope
> >
> > > 配置修饰的类是单例还是多例
> >
> > @lazy
> >
> > > 配置修饰的类是否是懒加载
> >
> > @PostConstruct
> >
> > > 修饰类中的方法，申明这是一个初始化方法
> >
> > @PreDestroy
> >
> > > 修饰类中的方法，申明这是一个销毁的方法

### 第三天笔记

> 重写
>
> > 主要用于子类覆盖父类
>
> 装饰着设计模式
>
> > 主要用于增加方法，不改变原来的功能
> >
> > > 在构造方法中添加要装饰的对象
>
> 代理设计模式
>
> > 静态代理
> >
> > > 将功能性代码和逻辑性代码分开，没有解决代码重复的问题
> >
> > 动态代理
> >
> > > java基于接口实现
> > >
> > > > 接口中没有的方法，无法被代理
> >
> > > cglib基于继承实现
>
> AOP
>
> > 连接点
> >
> > 切入点
> >
> > 切面
> >
> > 通知
> >
> > 目标对象

### 第四天笔记

> 切入点表达式
>
> > within（包名.类名）粗粒度表达式，一般不用
>
> > execution（返回值类型	包名.类名.方法名(参数类型,参数类型…))，常使用
>
> 五大通知类型
>
> > 前置通知(@Before)
> >
> > > `<aop:before method="before" pointcut-ref="pc01"/>`
> >
> > 环绕通知(@Around)
> >
> > > `<aop:around method="around" pointcut-ref="pc01"/>`
> >
> > 后置通知(@AfterReturning)
> >
> > > `<aop:after-returning method="afterReturning" pointcut-ref="pc01" returning="msg"/>`
> >
> > 异常通知(@AfterThrowing)
> >
> > > `<aop:after-throwing method="afterThrowing" pointcut-ref="pc01" throwing="e"/>`
> >
> > 最终通知(@After)
> >
> > > `<aop:after method="afterFinal" pointcut-ref="pc01"/>`
>
> 五种通知的使用场景
>
> > 前置通知(@Before)
> >
> > > 记录日志（方法被调用）
> >
> > 环绕通知(@Around)
> >
> > > 控制事务，权限控制
> >
> > 后置通知(@AfterReturning)
> >
> > > 记录日志（方法被调用成功）
> >
> > 异常通知(@AfterThrowing)
> >
> > > 异常处理 控制事务
> >
> > 最终通知(@After)
> >
> > > 记录日志（方法已被调用，但不一定成功）
>
> 四个联系（第五天有补充）
>
> > 异常信息收集
> >
> > 统计业务方法执行时间
> >
> > 对AOP进行权限控制（第五天优化）
> >
> > 事务的开启（第五天内容）

### 第五天笔记

> 线程安全的三个条件
>
> > 多条线程
> >
> > > 可以加锁破坏
> >
> > 破坏共享线程
> >
> > > 用ThreadLocal破话
> >
> > 有写操作
>
> 使用ThreadLocal，破坏共享资源案例
>
> 小项目
>
> > 对权限控制（优化）
> >
> > 事务的开启
>
> Spring整合JDBC（三种配置方式）
>
> > 在applicationContext.xml中直接配置
> >
> > 使用properties配置
> >
> > 引入c3p0配置文件
>
> spring配置事务
>
> > 事务的传播级别
>
> > spring在容器中配置事务
> >
> > spring使用注解方式配置事务


