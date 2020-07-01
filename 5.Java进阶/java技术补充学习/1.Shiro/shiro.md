## shiro学习笔记

------

### 官方快速入门案例解析

##### pom.xml

```xml
<?xml version="1.0" encoding="UTF-8"?>
    <project xmlns="http://maven.apache.org/POM/4.0.0"
        xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/maven-v4_0_0.xsd">

    <modelVersion>4.0.0</modelVersion>
    <groupId>org.apache.shiro.tutorials</groupId>
    <artifactId>shiro-tutorial</artifactId>
    <version>1.0.0-SNAPSHOT</version>
    <name>First Apache Shiro Application</name>
    <packaging>jar</packaging>

    <properties>
        <project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
    </properties>

    <build>
        <plugins>
            <plugin>
                <groupId>org.apache.maven.plugins</groupId>
                <artifactId>maven-compiler-plugin</artifactId>
                <version>3.8.0</version>
                <configuration>
                    <source>1.6</source>
                    <target>1.6</target>
                    <encoding>${project.build.sourceEncoding}</encoding>
                </configuration>
            </plugin>

        <!-- This plugin is only to test run our little application.  It is not
             needed in most Shiro-enabled applications: -->
            <plugin>
                <groupId>org.codehaus.mojo</groupId>
                <artifactId>exec-maven-plugin</artifactId>
                <version>1.1</version>
                <executions>
                    <execution>
                        <goals>
                            <goal>java</goal>
                        </goals>
                    </execution>
                </executions>
                <configuration>
                    <classpathScope>test</classpathScope>
                    <mainClass>Tutorial</mainClass>
                </configuration>
            </plugin>
        </plugins>
    </build>

    <dependencies>
        <dependency>
            <groupId>org.apache.shiro</groupId>
            <artifactId>shiro-core</artifactId>
            <version>1.4.1</version>
        </dependency>
        <!-- Shiro uses SLF4J for logging.  We'll use the 'simple' binding
             in this example app.  See http://www.slf4j.org for more info. -->
        <dependency>
            <groupId>org.slf4j</groupId>
            <artifactId>slf4j-simple</artifactId>
            <version>1.7.21</version>
            <scope>test</scope>
        </dependency>
        <dependency>
            <groupId>org.slf4j</groupId>
            <artifactId>jcl-over-slf4j</artifactId>
            <version>1.7.21</version>
            <scope>test</scope>
        </dependency>
    </dependencies>

</project>
```

##### src / main / java / Tutorial.java

```java
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.config.IniSecurityManagerFactory;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.util.Factory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Tutorial {

    private static final transient Logger log = LoggerFactory.getLogger(Tutorial.class);

    public static void main(String[] args) {
        log.info("My First Apache Shiro Application");
        //1.
        Factory<SecurityManager> factory = new IniSecurityManagerFactory("classpath:shiro.ini");
    	//2.
    	SecurityManager securityManager = factory.getInstance();
  		//3.
   		SecurityUtils.setSecurityManager(securityManager);
        
        // 使用SecurityUtils.getSubject(),可以获取获得当前正在执行的Subject
        Subject currentUser = SecurityUtils.getSubject();
        // 获取Subject与应用程序的当前会话
	    // 该session不需要web
        Session session = currentUser.getSession();
        session.setAttribute( "someKey", "aValue" );
        // currentUser.isAuthenticated()：判断当前角色是否认证
        if ( !currentUser.isAuthenticated() ) {
    		 // 使用用户名密码进行验证
    		 UsernamePasswordToken token = new UsernamePasswordToken("lonestarr", "vespa");
   			 // "记住我"操作
              // "记住"操作与"认证互斥"
    		 token.setRememberMe(true);
    		 // 对当前Subject进行登录
              try {
                  // 对当前Subject进行登录
   				 currentUser.login( token );
   			 //if no exception, that's it, we're done!
			} catch ( UnknownAccountException uae ) {
   				// 用户名不正确异常
			} catch ( IncorrectCredentialsException ice ) {
    			// 密码不匹配异常
			} catch ( LockedAccountException lae ) {
  			    // 被锁定异常无法登录
			}
  			    // 父级异常
			} catch ( AuthenticationException ae ) {
    			//unexpected condition - error?
			}
		}
         // 打印认证主体（该案例中为：用户名），currentUser.getPrincipal()：获取认证主体
		log.info( "User [" + currentUser.getPrincipal() + "] logged in successfully." );
    
    	// currentUser.hasRole( "schwartz" )：判断当前用户是否有schwartz这一角色
 	    if ( currentUser.hasRole( "schwartz" ) ) {
            log.info("May the Schwartz be with you!" );
	   } else {
            log.info( "Hello, mere mortal." );
	   }
    
       // 查看是否有权限对某一实体类采取行动
       if ( currentUser.isPermitted( "lightsaber:weild" ) ) {
           log.info("You may use a lightsaber ring.  Use it wisely.");
       } else {
           log.info("Sorry, lightsaber rings are for schwartz masters only.");
       }
       
      // 某一Subject是否有能力对某一实体类某一属性采取行为
      if ( currentUser.isPermitted( "winnebago:drive:eagle5" ) ) {
          log.info("You are permitted to 'drive' the 'winnebago' with license plate (id) 'eagle5'. 
                   " +"Here are the keys - have fun!");
      } else {
          log.info("Sorry, you aren't allowed to drive the 'eagle5' winnebago!");
      }
                   
      // 用户使用完成后进行注销，删除Subject所有标识信息并使其会话无效。
      currentUser.logout(); 
                   
         System.exit(0);
    }
}
```

##### src / main / resources / shiro.ini

```ini
# =============================================================================
# Tutorial INI configuration
#
# Usernames/passwords are based on the classic Mel Brooks' film "Spaceballs" :)
# =============================================================================

# -----------------------------------------------------------------------------
# Users and their (optional) assigned roles
# 用户及其可选分配的角色
# username = password, role1, role2, ..., roleN
# 格式为：用户名 = 密码，角色1，角色2，...
# -----------------------------------------------------------------------------
[users]
root = secret, admin
guest = guest, guest
presidentskroob = 12345, president
darkhelmet = ludicrousspeed, darklord, schwartz
lonestarr = vespa, goodguy, schwartz

# -----------------------------------------------------------------------------
# Roles with assigned permissions
# 用户及其可选分配的角色
# roleName = perm1, perm2, ..., permN
# 角色名称 = 实体类:具体行为:对某一个属性
# -----------------------------------------------------------------------------
[roles]
admin = *
schwartz = lightsaber:*
goodguy = winnebago:drive:eagle5
```





> `Subject currentUser = SecurityUtils.getSubject();`
>
> > 使用SecurityUtils.getSubject(),可以获取获得当前正在执行的Subject
>
> `Session session = currentUser.getSession();`
> `session.setAttribute( "someKey", "aValue" );`
>
> > 获取Subject与应用程序的当前会话
> >
> > 该session不需要web
> >
> > 
>
> 