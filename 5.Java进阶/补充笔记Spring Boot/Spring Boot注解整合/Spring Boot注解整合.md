## Spring Boot注解整合

----

### @RestController

> Spring 4.0版本之后的一个注解
>
> > 功能相当于@Controller和@ResponseBody之和

### @GetMapping("/hello")

> Spring后期推出的一个注解
>
> > 是`@RequestMapping(method = RequestMethod.GET)`的缩写

### @ConfigurationProperties(prefix = "book")

> 使用自定义属性给Bean类赋值时使用，在使用时需要在启动类上添加注解
>
> > `@EnableConfigurationProperties(XXXBean.class)`表示启动这个配置类

##### @EnableConfigurationProperties(XXXBean.class)表示启动这个配置类

> 必须
>
> 表示启动`@ConfigurationProperties(prefix = "book")`这一注解

##### @PropertySource(value = "classpath:application.properties")

> 可选
>
> 表示从application.properties读取数据值，给相应属性赋值

### @Alias("user")

> 使用@Alias可以表明实体类得别名

### @Configuration

> 表明这是一个配置类
>
> @Configuration用于定义配置类，可替换xml配置文件

##### @Bean

> 被注解的类内部包含有一个或多个被@Bean注解的方法
>
> > 这些方法将会被AnnotationConfigApplicationContext进行扫描
> >
> > 用于构建bean定义，初始化Spring容器

### @primary

> 一般在多数据源配置中使用，表明这是主数据源



### @Produces

> 用于定义方法返回的数据类型，可以定义一个或多个
>
> > `@Produces(MediaType.APPLICATION_JSON)`

###### 案例：

```java
    @GET
    @Path("countGroup")
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation("违规告警数量统计")
    @Authorization
    public ApiResult getCountGroup(
            @ApiParam(name = "groupId", value = "区域编号") @QueryParam("groupId") String groupId,
            @ApiParam(name = "precinctId", value = "企业编号") @QueryParam("precinctId") String precinctId,
            @ApiParam(name = "alarmType", value = "报警类型") @QueryParam("alarmType") int alarmType,
            @ApiParam(name = "startTime", value = "开始时间") @QueryParam("startTime") String startTime,
            @ApiParam(name = "endTime", value = "结束时间") @QueryParam("endTime") String endTime) {
        return ApiResult.Success(alarmService.getCountGroup(groupId, precinctId, alarmType, startTime, endTime));
    }
```



### @JsonFormat

> 时间格式化注解
>
> > 当我们存储在mysql中的数据是data类型，封装在实体类中的时候，就会编程英文时间格式，而不是：`yyyy-MM-dd HH:mm:ss`这样的中文时间，这时候我们需要用@JsonFormat转化时间格式
>
> > 它只会在类似@ResponseBody返回json数据的时候，才会返回格式化的yyyy-MM-dd HH:mm:ss时间,
> >
> > 你直接使用System.out.println()输出的话，仍然是类似“Fri Dec 01 21:05:20 CST 2017”这样的时间样式。
>
> > `@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")`

###### 案例

```java
package com.wiscom.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @program: base
 * @description: 人员轨迹
 * @author: 徐野宇
 * @create: 2019-11-19 09:17
 */
@Data
@ApiModel("人员轨迹")
public class TrajectoryVO {
    @ApiModelProperty(name="userDeviceId",value="蓝牙设备编号")
    private String userDeviceId;
    
    @ApiModelProperty(name = "addTime", value = "添加时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private String addTime;
}
```





### JPA相关注解

#### @Entity

> 表明这是一个实体类

#### @id

> 在属性上使用，表明这是主键

#### @GeneratedValue(strategy = GenerationType.IDENTITY)

> 注解在属性上，表明此字段自增长

#### @Column(nullable = false,unique = true)

> 可以设置字段的一些属性
>
> > nullable：未非空
> >
> > unique：唯一约束
> >
> > ......

#### @ManyToMany(fetch = FetchType.EAGER)

> 表示多对多的关系
>
> > EAGER
> >
> > > 表示取出这条数据时，它关联的数据也同时取出放入内存中
> >
> > LAZY
> >
> > > 取出这条数据时，它关联的数据并不取出来，在同一个session中，什么时候要用，就什么时候取(再次访问数据库)

#### @JoinTable(name = "UserRole",joinColumns = "{@}")

> >
> > @JoinTable(name = "UserRole",joinColumns = "{@JoinColumn(name="userId")}",inverseJoinColumns = {@JoinColumn(name = "roleId")})
> >
> > > name="UserRole"：注明中间表的表名
> > >
> > > joinColumns：两个表之间的关联字段



### @Transactional

##### @Transactional(isolation = Isolation.DEFAULT)

> 默认的事务隔离级别，使用数据库的事务隔离级别

##### @Transactional(isolation = Isolation.READ_UNCOMMITTED)

> 读未提交，最低级别的事务隔离级别，允许其他食物读取未提交的数据，这级别的事物容易产生脏读、不可重复度和幻读

##### @Transactional(isolation = Isolation.READ_COMMITTED)

> 读已提交，这种级别的事务隔离能读取其他事物已经修改的数据，不能读取未提交的数据，会产生不可重复读和幻读

##### @Transactional(isolation = Isolation.REPEATABLE_READ)

> 可重复度，这种级别的事务隔离可以防止不可重复读可脏读，但是会发生幻读

##### @Transactional(isolation = Isolation.SERIALIZABLE)

> 串行化，这是最高级别的事务隔离，会避免脏读，不可重复度和幻读。在这种隔离级别下事务会按照顺序进行

##### 对脏读、幻读、不可重复读的理解

> 脏读
>
> > 事务未提交，但是**其他事务**可以读到数据库中未提交的数据，并使用该数据
>
> 幻读
>
> > 事务1 多次读取数据库数据，每次得到的结果都不同（期间有其他事物对数据库数据进行**操作但是未提交**）
>
> 不可重复度
>
> > 事务1 多次读取数据库数据，每次得到的结果都不同（期间有其他事务对数据库数据进行**操作并提交**了）





### Swagger相关注解

##### @Api

>  用在Conntroller类上，协议集描述  
>
> > `@Api(value = "设备消息接口-DeviceController", description = "设备消息接口")`

##### @ApiOperation

> 作用在方法上，给API增加方法说明
>
> > `@ApiOperation("违规告警数量统计")`

##### @ApiParam

> 作用于参数
>
> > `@ApiParam(name = "page", value = "页码", defaultValue = "1", required = true) @QueryParam("page") int page,`
> >
> > > name：参数名
> > >
> > > value：说明

###### 案例

```java
@Component
@Path("alarm")
@ResponseBody
@Api(value = "报警消息接口-AlarmController", description = "报警消息接口")
public class AlarmController {    
	@GET
    @Path("countGroup")
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation("违规告警数量统计")
    @Authorization
    public ApiResult getCountGroup(
            @ApiParam(name = "groupId", value = "区域编号") @QueryParam("groupId") String groupId,
            @ApiParam(name = "precinctId", value = "企业编号") @QueryParam("precinctId") String precinctId,
            @ApiParam(name = "alarmType", value = "报警类型") @QueryParam("alarmType") int alarmType,
            @ApiParam(name = "startTime", value = "开始时间") @QueryParam("startTime") String startTime,
            @ApiParam(name = "endTime", value = "结束时间") @QueryParam("endTime") String endTime) {
        return ApiResult.Success(alarmService.getCountGroup(groupId, precinctId, alarmType, startTime, endTime));
    }
}
```

##### @ApiModel("报警分组统计实体")

> 用在返回对象类上,需要在controller引用才会生效
>
> > `@ApiModel("报警分组统计实体")`

##### @ApiModelProperty

> 使用在被 @ApiModel 注解的模型类的属性上
>
> > `@ApiModelProperty(name = "alarmTypeName", value = "报警类型名称")`
> >
> > > name：运行覆盖属性的名称。重写属性名称
> > >
> > > value：属性简要说明

###### 案例

```java
package com.wiscom.vo;

/**
 * @program: base
 * @description: 报警分组统计
 * @author: 徐野宇
 * @create: 2019-12-23 16:42
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ApiModel("报警分组统计实体")
public class AlarmGroupCount {
    @ApiModelProperty(name = "alarmType", value = "报警类型编号")
    private String alarmType;
    @ApiModelProperty(name = "alarmTypeName", value = "报警类型名称")
    private String alarmTypeName;
    @ApiModelProperty(name = "count", value = "报警数量")
    private int count;
}
```





### lombok相关注解

##### @Data 

> 注在类上，提供类的get、set、equals、hashCode、canEqual、toString方法

##### @AllArgsConstructor

> 注在类上，提供类的全参构造

##### @NoArgsConstructor

> 注在类上，提供类的无参构造

##### @Setter

> 注在属性上，提供 set 方法

##### @Getter 

> 注在属性上，提供 get 方法

##### @EqualsAndHashCode 

> 注在类上，提供对应的 equals 和 hashCode 方法

##### @Log4j/@Slf4j

> 注在类上，提供对应的 Logger 对象，变量名为 log

###### 案例

```java
package com.wiscom.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @program: base
 * @description:
 * @author: 徐野宇
 * @create: 2019-12-17 18:37
 */
@ApiModel("企业分组统计实体")
@Data
public class PrecinctCountVO {

    @ApiModelProperty(name = "precinctId", value = "分组编号")
    private String precinctId;

    @ApiModelProperty(name = "precinctName", value = "分组名称")
    private String precinctName;

    @ApiModelProperty(name = "count", value = "数量")
    private int count;
}
```