## Mybatis

-----

### 多表查询

> 笛卡儿积查询
>
> 内连接查询
>
> 外连接查询
>
> > 左外连接查询
> >
> > 右外连接查询
> >
> > 全外连接查询

#### 一对一

> 在任意一方设计外键保存另一张表的主键，维系表和表的关系

###### 创建sql语句

```sql
create table room(id int primary key,name varchar(255));
insert into room values (1,'梅花屋'),(2,'兰花屋'),(3,'桃花屋');
create table grade(id int primary key,name varchar(255), rid int);
insert into grade values (999,'向日葵班',2),(888,'玫瑰花班',3),(777,'菊花班',1);
```

###### Grade.java

```java
public class Grade {
	private int id;
	private String name;
	private Room room;
}
```

###### Room.java

```java
public class Room {
	private int id;
	private String name;
}
```

###### UserMapper.xml：映射文件

```xml
<!-- 一对一查询  -->
	<resultMap type="cn.tedu.domain.Grade" id="RM01">
		<id column="gid" property="id"/>
		<result column="gname" property="name"/>
		<association property="room" javaType="cn.tedu.domain.Room">
            <!-- 关联条件R_id对应G_id -->
			<id column="rid" property="id"/>
			<result column="rname" property="name"/>
		</association>
	</resultMap>
	
	<select id="s01" resultMap="RM01">
		select 
		room.id as rid,room.name as rname,grade.id as gid,grade.name as gname
		from 
		room inner join grade 
		on 
		room.id=grade.rid;
	</select>
```

###### TestDemo.java

```java
package cn.tedu.test;

public class TestDemo {
	InputStream is = null;
	SqlSessionFactory ssf = null;
	SqlSession session = null;
	@Before
	public void demo() throws IOException{
		// 获取配置文件对应的字节输入流
		is = Resources.getResourceAsStream("sqlMapConfig.xml");
		// 生成数据库连接
		ssf = new SqlSessionFactoryBuilder().build(is);
		
		// 获取会话
		session = ssf.openSession();
	}
	
	@Test
	public void test01() throws IOException{
		// 执行sql语句
		List list = session.selectList("cn.tedu.domain.UserMapper.s01");
		System.out.println(list);
	}

}

```



#### 一对多

> 在多的一方设计外键保存一的一方的主键，维系表和表的关系

###### Dept.java

```java
public class Dept {
	private int id;
	private String name;
	// 员工属性
	private List<Emp> list;
}
```

###### Emp.java

```java
public class Emp {
	private int id;
	private String name;
}
```

###### Emp2.java

```java
public class Emp2 {
	private int id;
	private String name;
	private Dept2 dept;
}
```

###### UserMapper.xml：映射文件

```xml
	<!-- 一对多查询   以部门角度处理问题 -->
	<resultMap type="cn.tedu.domain.Dept" id="RM02">
		<id column="did" property="id"/>
		<result column="dname" property="name"/>
		<collection property="list" ofType="cn.tedu.domain.Emp">
			<id column="eid" property="id"/>
			<result column="ename" property="name"/>
		</collection>
		
	</resultMap>
	
	<select id="s02" resultMap="RM03">
		select 
		dept.id as did,dept.name as dname,emp.id as eid,emp.name as ename 
		from
		dept inner join emp 
		on 
		dept.id=emp.deptid;
	</select>
	
	<!-- 多对一查询  以员工角度处理问题  -->
	<resultMap type="cn.tedu.domain.Emp2" id="RM03">
		<id column="eid" property="id"/>
		<result column="ename" property="name"/>
		<association property="dept" javaType="cn.tedu.domain.Dept2">
			<id column="did" property="id"/>
			<result column="dname" property="name"/>
		</association>
	</resultMap>
```

###### TestDemo.java

```java
	@Test
	public void test01() throws IOException{
		// 执行sql语句
		List list = session.selectList("cn.tedu.domain.UserMapper.s02");
		System.out.println(list);
	}
```



#### 多对多

> 设计一张第三方关系表，存储两张表的主键的对应关系，将一个多对多拆成两个一对多来存储

###### Stu.java

```java
public class Stu {
	private int id;
	private String name;
	// 一个学生有多个老师
	private List<Teacher> list;
}
```

###### Teacher.java

```java
public class Teacher {
	private int id;
	private String name;
}
```

###### Teacher2.java

```java
public class Teacher2 {
	private int id;
	private String name;	
	private List<Stu2> list;
}
```

###### Stu2.java

```java
public class Stu2 {
	private int id;	
	private String name;
}
```

###### UserMapper.xml：映射文件

```xml
	<!--  多对多查询  以学生视角解决问题 -->
	<resultMap type="cn.tedu.domain.Stu" id="RM04">
		<id column="sid" property="id"/>
		<result column="sname" property="name"/>
		<collection property="list" ofType="cn.tedu.domain.Teacher">
			<id column="tid" property="id"/>
			<result column="tname" property="name"/>
		</collection>
	</resultMap>
	
	<!-- 多对多查询  以老师视角解决问题  -->
	<resultMap type="cn.tedu.domain.Teacher2" id="RM05">
		<id column="tid" property="id"/>
		<result column="tname" property="name"/>
		<collection property="list" ofType="cn.tedu.domain.Stu2">
			<id column="sid" property="id"/>
			<result column="sname" property="name"/>
		</collection>
	</resultMap>
	
	
	<select id="s03" resultMap="RM05">
		select
		stu.id as sid,stu.name as sname,teacher.id as tid,teacher.name as tname
		from
		stu inner join stu_teacher
		on
		stu.id=stu_teacher.sid
		inner join teacher
		on
		teacher.id=stu_teacher.tid
	</select>
```





### Mybatis中的细节

#### 别名标签

###### sqlMapConfig.xml

```xml
	<typeAliases>
		<typeAlias type="cn.tedu.domain.User" alias="Ailas_User"/>
	</typeAliases>
```

###### UserMapper.xml：映射文件

```xml
	<select id="queryAll" resultType="Alias_User">
		select * from user;
	</select>
```



#### sql的复用

> `<include refid="test"/>`

> 如果某段sql语句的片段在映射文件中重复出现，`可以将其单独配置为一个引用，从而在需要时直接引用，减少配置量

###### UserMapper.xml：映射文件

```xml
	<sql id="test">
		select * from user
	</sql>
	<select id="selAll1" resultType="cn.tedu.domain.User">
		<include refid="test"/> where id = 3;
	</select>
```



#### Mybatis的缓存机制

> 缓存机制可以减轻数据库的压力，原理是在第一次查询时，将查询结果缓存起来，之后再查询同样的sql，不是真的去查询数据库，而是直接返回缓存中的结果。
>
> 缓存可以降低数据库的压力，但同时可能无法得到最新的结果数据

##### 一级缓存，二级缓存

> 可以使用第三方工具实现缓存
>
> > Redis内存数据库
>
> Mybatis提供的缓存机制来实现缓存

> 一级缓存
>
> > 缓存只在事务中有效，只有第一次查询时真正去查库，之后的查询都是从缓存中获得数据，如果==事务不同==则缓存无效
>
> 二级缓存（一般不会使用）
>
> > ==缓存在全局有效==，二级缓存作用时间长，可能造成的危害也大

##### Mybatis中的一级缓存

> Mybatis中的一级缓存默认就是开启的无法关闭

##### Mybatis中的二级缓存

> Mybatis中的二级缓存默认是关闭的

##### 配置选项开启二级缓存

> 要被二级缓存必须要实现序列化接口
>
> 且一定要被提交事务

###### sqlMapConfig.xml

```xml
	<!-- 开启二级缓存 -->
	<settings>
		<setting name="cacheEnabled" value="true"/>
	</settings>
```

###### UserMapper.xml：映射文件

```xml
	<!-- 二级缓存的开关 -->
	<cache />
```

###### User.java

```java
public class User implements Serializable{
	private int id;
	private String name;
	private int age;
}
```



##### 接口的使用

> 接口的名字和映射文件的名字相同
>
> 接口中方法的名字和要调用的映射文件中的标签的id相同
>
> 方法的参数和被调用的标签中的sql中需要的参数对应
>
> 返回类型需要和sql中的匹配
>
> 接口的位置必须和映射文件的命名空间相同

###### UserMapper.xml

```xml
<!-- mapper的命名空间  -->
<mapper namespace="cn.tedu.domain.UserMapper">

	<select id="selAll" resultType="cn.tedu.domain.User">
		select * from user
	</select>
	
	<insert id="insOne">
		insert into user values(null,#{name},#{age});
	</insert>
	
</mapper>
```

###### cn.tedu.domain.UserMapper

```java
package cn.tedu.domain;

public interface UserMapper {
	public List<User> selAll();
	public void insOne(User user);
}

```

###### TestDemo.java

```java
package cn.tedu.test;

public class TestDemo {
	InputStream is = null;
	SqlSessionFactory ssf = null;
	@Before
	public void demo() throws IOException{
		// 获取配置文件对应的字节输入流
		is = Resources.getResourceAsStream("sqlMapConfig.xml");
		// 生成数据库连接
		ssf = new SqlSessionFactoryBuilder().build(is);
		
		// 获取会话
	}
	
	@Test
	public void test01() throws IOException{
		SqlSession session = ssf.openSession();
		// 通过接口获取映射对象
		UserMapper mapper = session.getMapper(UserMapper.class);
		// 通过映射对象调用方法并打印结果
		System.out.println(mapper.selAll());
	}
}
```