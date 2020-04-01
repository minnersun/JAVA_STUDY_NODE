## Mybatis笔记整理

----

### 使用mybatis的准备工作

##### 使用mybatis需要导入tdt的约束文件

​	配置详见笔记

> mybatis-3-config.dtd
>
> mybatis-3-mapper.dtd

### Mybatis的结构

> 配置数据源：sqlMapConfig.xml
>
> > 别名标签，缓存的配置
>
> 映射文件
>
> > 主要写sql，映射关系
>
> 生成数据库连接
>
> > SqlSessionFactory
>
> 执行数据库的CRUD
>
> > SqlSession

##### 编写配置文件sqlMapConfig.xml，配置数据源

###### sqlMapConfig.xml

```XML
<?xml version="1.0" encoding="UTF-8" ?>
<!DOCTYPE configuration
PUBLIC "-//mybatis.org//DTD Config 3.0//EN"
"http://mybatis.org/dtd/mybatis-3-config.dtd">
<configuration>


<!-- 配置数据源  default默认使用的数据源 -->
	<environments default="mysql">
		<environment id="mysql">
			<transactionManager type="JDBC" />
			<dataSource type="POOLED">
				<property name="driver" value="com.mysql.jdbc.Driver" />
				<property name="url" value="jdbc:mysql://localhost:3306/mybatis_test" />
				<property name="username" value="root" />
				<property name="password" value="root" />
			</dataSource>
		</environment>
	</environments>
	
	
	<!-- 映射文件 -->
	<mappers>
		<mapper resource="UserMapper.xml"/>
	</mappers>

</configuration>
```

##### 编写映射文件，描述bean和表sql的映射关系

###### UserMapper.xml

```xml
<?xml version="1.0" encoding="UTF-8" ?>
<!DOCTYPE mapper
PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN"
"http://mybatis.org/dtd/mybatis-3-mapper.dtd">
<!-- mapper的命名空间  -->
<mapper namespace="cn.tedu.domain.UserMapper">
	<select id="selAll" resultType="cn.tedu.domain.User">
		select * from user
    </select>
</mapper>

```

##### 将映射文件配置到sqlMapConfig.xml中

###### sqlMapConfig.xml

```xml
	<!-- 映射文件 -->
	<mappers>
		<mapper resource="UserMapper.xml"/>
	</mappers>

```

##### 测试类

###### TestDemo.java

```java
package cn.tedu.test;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.junit.Before;
import org.junit.Test;

import cn.tedu.domain.User;

public class TestDemo {
	InputStream is = null;
	SqlSessionFactory ssf = null;
	SqlSession session = null;
	@Before
	public void demo() throws IOException{
		// 获取配置文件对应的字节输入流
        // 更具配置文件解析所有的映射关系
		is = Resources.getResourceAsStream("sqlMapConfig.xml");
		// 生成数据库连接
		ssf = new SqlSessionFactoryBuilder().build(is);
		
		// 获取会话
		session = ssf.openSession();
	}
	
	@Test
	public void test01() throws IOException{

		// 执行sql语句
		List<User> list = session.selectList("cn.tedu.domain.UserMapper.selectAll");
		System.out.println(list);
	}

}

```





### Mybatis传值

> `${}:不会自动转为字符串类型`
>
> `#{}：字符串类型会自动转化为字符串类型`

```sql
select * from user order by ${cname}; --> select * from user order by age;
select * from user order by #{cname}; --> select * from user order by 'age';
```



##### Map传值

###### TestDemo.java

```java
	@Test
	public void test02() throws IOException{
		
		// 创建map
		HashMap<String, String> map = new HashMap<String,String>();
		// 由map向sql语句中传值
		map.put("min", "30");
		map.put("max", "40");

		// 执行sql语句
		List<User> list = session.selectList("cn.tedu.domain.UserMapper.selAge",map);
		System.out.println(list);
	}
```

###### UserMapper.xml

```xml
	<select id="selAge" resultType="cn.tedu.domain.User">
		select * from user where age between #{min} and #{max}
	</select>
```



##### 对象传值

###### TestDemo.java

```java
	@Test
	public void test03() throws IOException{
		
		User user = new User();
		
		// 设置姓名和年龄
		user.setName("zsf");
		user.setAge(233);

		// 执行sql语句
		session.insert("cn.tedu.domain.UserMapper.insData",user);
		// 提交事务
		session.commit();
	}
```

###### UserMapper.xml

```xml
	<insert id="insData">
		insert into user values(null,#{name},#{age})
	</insert>
```



##### 单值传值

###### TestDemo.java

```java
	@Test
	public void test04() throws IOException{
	
		// 执行sql语句
		User user = session.selectOne("cn.tedu.domain.UserMapper.selOne",7);
		
		System.out.println(user);
	}
	
```

###### UserMapper.xml

```xml
	<select id="selOne" resultType="cn.tedu.domain.User">
		select * from user where id = #{id}
	</select>
```



##### update修改

###### TestDemo.java

```java
	@Test
	public void test05() throws IOException{
		User user = new User();
		user.setId(3);
//		user.setName("ldh");
		user.setAge(65);
		
		// 执行语句
		session.update("cn.tedu.domain.UserMapper.updateOne",user);
		
		session.commit();
	}
```

###### UserMapper.xml

```xml
	<update id="updateOne">
		update user
		<set>
			<if test="name != null">name=#{name},</if>
			<if test="age != 0">age=#{age},</if>
		</set>
		where id = #{id}
	</update>
```



##### select查询

###### TestDemo.java

```java
	@Test
	public void test06() throws IOException{
		User user = new User();
//		user.setId(3);
		user.setName("gfs");
		
		// 执行语句
		User userx = session.selectOne("cn.tedu.domain.UserMapper.selectOne", user);
		System.out.println(userx);
	}
```

###### UserMapper.xml

```xml
	<select id="selectOne" resultType="cn.tedu.domain.User">
		select * from user
		<where>
			<if test="id != 0">id = #{id}</if>
			<if test="name != null">and name=#{name}</if>
			<if test="age != 0"> and age=#{age}</if>
		</where>
	
	</select>
```



##### insert操作

###### TestDemo.java

```java
	@Test
	public void test07() throws IOException{
		User user = new User();
//		user.setId(3);
		user.setName("xdd");
		user.setAge(69);
		// 执行语句
		session.insert("cn.tedu.domain.UserMapper.insertUser",user);
		// 提交事务
		session.commit();
	}
```

###### UserMapper.xml

```xml
	<insert id="insertUser">
		insert into user
		<trim prefix="(" suffix=")" suffixOverrides=",">
			id,
			<if test="name != null">name,</if>
			<if test="age != 0">age,</if>
		</trim>
		values
		<trim prefix="(" suffix=")" suffixOverrides=",">
			id,
			<if test="name != null">#{name},</if>
			<if test="age != 0">#{age},</if>
		</trim>
	</insert>
```



##### delete删除

###### TestDemo.java

```java
	@Test
	public void test08() throws IOException{
		User user = new User();
		user.setId(12);
		user.setName("xdd");
		
		// 执行语句
		session.delete("cn.tedu.domain.UserMapper.deleteOne",user);
		// 提交事务
		session.commit();
	}
```

###### UserMapper.xml

```xml
	<delete id="deleteOne">
	delete from user
		<where>
			<if test="id != 0">id = #{id}</if>
			<if test="name != null">and name = #{name}</if>
			<if test="age != 0">and age = #{age}</if>
		</where>
	</delete>
```



##### 手动映射结果集

###### TestDemo.java

```java
	@Test
	public void test01() throws IOException{
		
		
		// 执行sql语句
		List<User> list = session.selectList("cn.tedu.domain.UserMapper.selectAll");
		System.out.println(list);
	}
```

###### UserMapper.xml

```xml
	<select id="selectAll" resultMap="resultRM">
		select * from user;
	</select>
	
	
	<resultMap type="cn.tedu.domain.User" id="resultRM">
		<!-- 必须配置主键列，而且必须值是id -->
		<id column="id" property="id"/>
		<!-- 如果是非主键列，列名和bean中属性名相同，那么可以不配置 -->
		<result column="name" property="username"/>
	</resultMap>
```

