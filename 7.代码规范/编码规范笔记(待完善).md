## 编码规范笔记（待完善）

> 读《阿里巴巴 Java 开发手册》v 1.2.0 整理

### 命名风格

##### 1、类名

> ==类名==使用` UpperCamelCase` 风格，必须遵从驼峰形式
>
> > 但以下情形例外：
> >
> > >`DO / BO / DTO / VO / AO`
>
> > 正例：
> >
> > > `MarcoPolo / UserDO / XmlService / TcpUdpDeal / TaPromotion`
> >
> > 反例：
> >
> > > `macroPolo / UserDo / XMLService / TCPUDPDeal / TAPromotion`

##### 2、方法名、参数名、成员变量、局部变量

> 2、==方法名、参数名、成员变量、局部变量==都统一使用` lowerCamelCase `风格，必须遵从
> 驼峰形式
>
> > 正例：
> >
> > > `localValue / getHttpMessage() / inputUserId`

##### 3、常量命名

> ==常量命名==全部大写，单词间用==下划线==隔开，力求语义表达完整清楚，不要嫌名字长
>
> > 正例：
> >
> > > `MAX_STOCK_COUNT`

##### 4、抽象类

> 4、==抽象类==命名使用==Abstract 或 Base开头==；

##### 5、异常类

> ==异常类==命名使用 ==Exception结尾==

##### 6、测试类

> ==测试类==命名以它要测试的类的名称开始，以 ==Test 结尾==

##### 7、定义数组类型

> 7、==定义数组类型==，中括号是数组类型的一部分
>
> > 正例：
> >
> > > `String[] args`
> >
> > 反例：
> >
> > > `String args[]`

##### 8、POJO 类中布尔类型变量

> ==POJO 类中布尔类型==的变量，都==不要加 is==，否则部分框架解析会引起==序列化错误==
>
> > 反例：
> >
> > > Boolean isDeleted的属性，它的方法也是 isDeleted(),
> > >
> > > ==RPC框架==在反向解析的时候，“以为”对应的属性名称是 ==deleted==，
> > >
> > > 导致==属性获取不到==，进而抛出异常

##### 9、包名

> 包名统一使用==小写==，点分隔符之间有且仅有==一个==自然语义的==英语单词==，
>
> 包名统一使用==单数形式==
>
> > 

##### 10、杜绝完全不规范的缩写

> 反例：
>
> >`AbstractClass` “缩写” 命名成 `AbsClass`；
> >
> >`condition` “缩写” 命名成 `condi`

##### 11、使用设计模式命名

> 如果使用到了设计模式，建议在类名中现出具体模式
>
> > 正例：
> >
> > > `public class OrderFactory;
> > > public class LoginProxy;
> > > public class ResourceObserver`

##### 12、接口类中的方法和属性

> 接口类中的方法和属性==不要加任何修饰符号==（public 也不要加）
>
> > 正例：
> >
> > > 接口方法签名：void f();
> > > 接口基础常量表示：String COMPANY = "alibaba";
> >
> > 反例
> >
> > > 接口方法定义：public abstract void f();
>
> 说明
>
> > JDK8 中接口允许有默认实现，那么这个 default 方法，是对所有实现类都有价值的默认实现。

##### 13、接口和实现类的命名

> 接口和实现类的命名有两套规则：
>
> > 1、对于 Service 和 DAO 类，基于 SOA 的理念，暴露出来的服务一定是接口，内部
> > 的实现类用 Impl 的后缀与接口区别
> >
> > > 正例：
> > >
> > > > `CacheServiceImpl` 实现 `CacheService` 接口
> >
> > 2、如果是形容能力的接口名称，取对应的形容词做接口名（通常是–able 的形式）
> >
> > > 正例：
> > >
> > > > `AbstractTranslator` 实现 `Translatable`

##### 14、枚举类命名

> 枚举类名建议带上 ==Enum 后缀==，枚举==成员名称需要全大写==，单词间用下划线隔开
>
> > 枚举其实就是==特殊的常量类==，且==构造方法==被默认强制是==私有==
> >
> > > 正例：
> > >
> > > > 枚举名字：DealStatusEnum，成员名称：SUCCESS / UNKOWN_REASON

##### 15、各层命名规约

###### A) Service/DAO 层方法命名规约

> 1） 获取单个对象的方法用 get 做前缀。
>
> 2） 获取多个对象的方法用 list 做前缀。
>
> 3） 获取统计值的方法用 count 做前缀。
>
> 4） 插入的方法用 save（推荐）或 insert 做前缀。
>
> 5） 删除的方法用 remove（推荐）或 delete 做前缀。
>
> 6） 修改的方法用 update 做前缀。

###### B) 领域模型命名规约

> 1） 数据对象：xxxDO，xxx 即为数据表名。
>
> 2） 数据传输对象：xxxDTO，xxx 为业务领域相关的名称。
>
> 3） 展示对象：xxxVO，xxx 一般为网页名称。
>
> 4） POJO 是 DO/DTO/BO/VO 的统称，禁止命名成 xxxPOJO





### 常量定义

##### 1、为定义的常量不能出现在代码中

> 反例：
>
> > String key = "Id#taobao_" + tradeId;
> > cache.put(key, value);

##### 2、使用long/Long初始赋值

> long 或者 Long 初始赋值时，==必须使用大写的 L==，不能是小写的 l，小写容易跟数字 1 混淆

##### 3、不要使用一个常量类维护所有常量

> 不要使用一个常量类维护所有常量，应该按常量功能进行归类，分开维护
>
> > 如：
> >
> > > 缓存相关的常量放在类：`CacheConsts `下；
> > >
> > > 系统配置相关的常量放在类：`ConfigConsts `下。

##### 4、变量值在一个范围内变化，定义为枚举类

> `public Enum {`
>
> ` MONDAY(1), TUESDAY(2), WEDNESDAY(3), THURSDAY(4), FRIDAY(5), SATURDAY(6),SUNDAY(7);` 
>
> `}`



### OOP规约

##### 1、访问类中静态变量或静态方法

> **避免**通过==创建对象==的形式访问类中的静态变量或方法，这会增加==编译器==的解析成本，==直接用类名访问即可==

##### 2、所有的覆写方法，必须加@Override 注解

> `getObject()`与 `get0bject()`的问题。一个是**字母的 O**，一个是**数字的 0**，
>
> 加@Override可以准确判断是否覆盖成功
>
> 另外，如果在抽象类中对==方法签名进行修改==，其实现类会马上==编译报错==。

##### 3、Java的可变参数

> ==相同参数类型，相同业务含义==，才可以使用 Java 的可变参数，**避免使用 Objec**t
>
> > 可变参数必须放置在参数列表的最后。（提倡同学们尽量==不用可变参数编程==）
>
> > public User getUsers(String type, Integer... ids) {...}

##### 4、不能使用过时的类或方法

> > `java.net.URLDecoder` 中的方法 `decode(String encodeStr) `这个方法已经过时,
> >
> > 应该使用双参数 `decode(String source, String encode)`。
>
> 接口提供方既然明确是过时接口，那么有义务同时提供新的接口；
>
> 作为调用方来说，有义务去考证过时方法的新实现是什么。

##### 5、equals方法的使用

> Object 的 equals 方法容易抛空指针异常，应使用常量或确定有值的对象来调用equals
>
> > 所有的相同类型的包装类对象之间值的比较，全部使用 equals 方法比较
>
> > 正例：
> >
> > > ` "test".equals(object);`
> >
> > 反例：
> >
> > > object.equals("test");

##### 6、序列化的serialVersuionUID字段

> 序列化类新增属性时，请**不要修改** serialVersionUID 字段，避免反序列失败；
>
> 如果完全不兼容升级，避免反序列化混乱，那么请修改 serialVersionUID 值
>
> > 注意 serialVersionUID 不一致会抛出序列化运行时异常。

##### 7、pojo类必须重写toString方法

> 如果继承了另一个 POJO 类，注意在前面加一下 super.toString
>
> 在方法执行==抛出异常==时，可以直接调用 POJO 的 toString()方法打印其属性值，便于排查问题

##### 8、使用spilt需要对最后一个分隔符检查

> 使用索引访问用 String 的 split 方法得到的数组时，需做最后一个分隔符后有无
> 内容的检查，否则会有抛 `IndexOutOfBoundsException` 的风险
>
> > 举例
> >
> > > `String str = "a,b,c,,";
> > > String[] ary = str.split(",");
> > > //预期大于 3，结果是 3
> > > System.out.println(ary.length); `

##### 9、getter/setter方法中不要增加业务逻辑

> setter 方法中，参数名称与类成员变量名称一致，this.成员名 = 参数名。在
> getter/setter 方法中，不要增加业务逻辑，==增加排查问题的难度==
>
> > 反例
> >
> > > `public Integer getData() {
> > > if (true) {
> > > return this.data + 100;
> > > } else {
> > > return this.data - 100;
> > > }
> > > } `

##### 10、对字符串进行扩展时，需要使用StringBuilder的append

> 反编译出的字节码文件显示每次循环都会 new 出一个 StringBuilder 对象，然后进行
> append 操作，最后通过 toString 方法返回 String 对象，造成内存资源浪费
>
> > `String str = "start";
> > for (int i = 0; i < 100; i++) {
> > str = str + "hello";
> > } `



### 集合处理

##### 1、重写equals

> 只要重写equals，就必须重写hashCode
>
> > 因为 Set 存储的是不重复的对象，依据 hashCode 和 equals 进行判断，所以 Set 存储的对象必须重写这两个方法
>
> > 如果自定义对象做为 Map 的键，那么必须重写 hashCode 和 equals