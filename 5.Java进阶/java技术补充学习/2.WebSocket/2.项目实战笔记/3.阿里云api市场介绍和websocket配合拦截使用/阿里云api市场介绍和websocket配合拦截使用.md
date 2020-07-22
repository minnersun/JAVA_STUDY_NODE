## 阿里云api市场介绍和websocket配合拦截使用

-----

### 阿里云API市场网址

> `https://market.aliyun.com/data?spm=5176.8142029.388261.183.346bc16fAs3slP`

### src/main/java/xdclass_websocket/

#### intecepter

##### HttpHandShakeIntecepter.java

> 新增拦截器

```java
package xdclass_websocket.intecepter;

import java.util.Map;

import javax.servlet.http.HttpSession;

import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;
/**
 * 功能描述：http握手拦截器，可以通过这个类的方法获取resuest,和response
 *
 */
public class HttpHandShakeIntecepter implements HandshakeInterceptor{
	@Override
	public boolean beforeHandshake(ServerHttpRequest request,
			ServerHttpResponse response, WebSocketHandler wsHandler,
			Map<String, Object> attributes) throws Exception {

		System.out.println("【握手拦截器】beforeHandshake");

		if(request instanceof ServletServerHttpRequest) {
			ServletServerHttpRequest servletRequest = (ServletServerHttpRequest)request;
			HttpSession session =  servletRequest.getServletRequest().getSession();
			String sessionId = session.getId();
			System.out.println("【握手拦截器】beforeHandshake sessionId="+sessionId);
			attributes.put("sessionId", sessionId);
		}
        // 如果return false;	说明没有执行成功，会一直请求
		return true;
	}
    
	@Override
	public void afterHandshake(ServerHttpRequest request,
			ServerHttpResponse response, WebSocketHandler wsHandler,
			Exception exception) {
		System.out.println("【握手拦截器】afterHandshake");
		if(request instanceof ServletServerHttpRequest) {
			ServletServerHttpRequest servletRequest = (ServletServerHttpRequest)request;
			HttpSession session =  servletRequest.getServletRequest().getSession();
			String sessionId = session.getId();
			System.out.println("【握手拦截器】afterHandshake sessionId="+sessionId);
		}
	}
}
```

#### config

##### WebSocketConfig.java

> 拦截器还需要在config中配置一下才会使用

```java
package xdclass_websocket.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.AbstractWebSocketMessageBrokerConfigurer;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;

import xdclass_websocket.intecepter.HttpHandShakeIntecepter;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig extends AbstractWebSocketMessageBrokerConfigurer{
	/**
	 * 注册端点，发布或者订阅消息的时候需要连接此端点
	 * setAllowedOrigins 非必须，*表示允许其他域进行连接
	 * withSockJS  表示开始sockejs支持
	 */
	public void registerStompEndpoints(StompEndpointRegistry registry) {

		registry.addEndpoint("/endpoint-websocket").addInterceptors(new HttpHandShakeIntecepter())
		.setAllowedOrigins("*").withSockJS();
	}
	/**
	 * 配置消息代理(中介)
	 * enableSimpleBroker 服务端推送给客户端的路径前缀
	 * setApplicationDestinationPrefixes  客户端发送数据给服务器端的一个前缀
	 */
	@Override
	public void configureMessageBroker(MessageBrokerRegistry registry) {
		
		registry.enableSimpleBroker("/topic", "/chat");
		registry.setApplicationDestinationPrefixes("/app");
	}	
}
```

#### listener

##### ConnectEventListener.java

```java
package xdclass_websocket.listener;

import org.springframework.context.ApplicationListener;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionConnectEvent;

@Component
public class ConnectEventListener implements ApplicationListener<SessionConnectEvent>{

	public void onApplicationEvent(SessionConnectEvent event) {
		StompHeaderAccessor headerAccessor =  StompHeaderAccessor.wrap(event.getMessage());
		System.out.println("【ConnectEventListener监听器事件 类型】"+headerAccessor.getCommand().getMessageType());	
	}
}
```

##### SubscribeEventListener.java

```java
package xdclass_websocket.listener;

import org.springframework.context.ApplicationListener;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionSubscribeEvent;

/**
 * 功能描述：springboot使用，订阅事件
 *
 */
@Component
public class SubscribeEventListener implements ApplicationListener<SessionSubscribeEvent>{
	/**
	 * 在事件触发的时候调用这个方法
	 * 
	 * StompHeaderAccessor  简单消息传递协议中处理消息头的基类，
	 * 通过这个类，可以获取消息类型(例如:发布订阅，建立连接断开连接)，会话id等
	 * 
	 */
	public void onApplicationEvent(SessionSubscribeEvent event) {
		StompHeaderAccessor headerAccessor =  StompHeaderAccessor.wrap(event.getMessage());
		System.out.println("【SubscribeEventListener监听器事件 类型】"+headerAccessor.getCommand().getMessageType());
		System.out.println("【SubscribeEventListener监听器事件 sessionId】"+headerAccessor.getSessionAttributes().get("sessionId"));
	}
}
```

### 拦截顺序

> `【握手拦截器】beforeHandshake
> 【握手拦截器】beforeHandshake sessionId=F2F1C08A32646A3B3E4CE2AACCD69085
> 【握手拦截器】afterHandshake
> 【握手拦截器】afterHandshake sessionId=F2F1C08A32646A3B3E4CE2AACCD69085
> 【ConnectEventListener监听器事件 类型】CONNECT
> 【SubscribeEventListener监听器事件 类型】SUBSCRIBE
> 【SubscribeEventListener监听器事件 sessionId】F2F1C08A32646A3B3E4CE2AACCD69085`