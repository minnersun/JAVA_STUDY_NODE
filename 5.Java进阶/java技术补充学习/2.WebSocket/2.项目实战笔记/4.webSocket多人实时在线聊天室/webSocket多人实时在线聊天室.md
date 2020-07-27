## webSocket多人实时在线聊天室

### src/main/java/xdclass_websocket

#### config

##### WebSocketConfig.java

```java
package xdclass_websocket.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.AbstractWebSocketMessageBrokerConfigurer;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;

import xdclass_websocket.intecepter.HttpHandShakeIntecepter;
import xdclass_websocket.intecepter.SocketChannelIntecepter;


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

	@Override
	public void configureClientInboundChannel(ChannelRegistration registration) {
		registration.interceptors( new SocketChannelIntecepter());
	}

	@Override
	public void configureClientOutboundChannel(ChannelRegistration registration) {
		registration.interceptors( new SocketChannelIntecepter());
	}
}
```

#### intecepter

##### SocketChannelIntecepter.java

```java
package xdclass_websocket.intecepter;

import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptorAdapter;

import xdclass_websocket.controller.v6.UserChatController;

/**
 * 功能描述：频道拦截器 ，类似管道，可以获取消息的一些meta数据
 */
public class SocketChannelIntecepter extends ChannelInterceptorAdapter{

	/**
	 * 在完成发送之后进行调用，不管是否有异常发生，一般用于资源清理
	 */
	@Override
	public void afterSendCompletion(Message<?> message, MessageChannel channel,
			boolean sent, Exception ex) {
		System.out.println("SocketChannelIntecepter->afterSendCompletion");
		super.afterSendCompletion(message, channel, sent, ex);
	}
	
	/**
	 * 在消息被实际发送到频道之前调用
	 */
	@Override
	public Message<?> preSend(Message<?> message, MessageChannel channel) {
		System.out.println("SocketChannelIntecepter->preSend");
		
		return super.preSend(message, channel);
	}

	/**
	 * 发送消息调用后立即调用
	 */
	@Override
	public void postSend(Message<?> message, MessageChannel channel,
			boolean sent) {
		System.out.println("SocketChannelIntecepter->postSend");
		
		StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(message);//消息头访问器
		
		if (headerAccessor.getCommand() == null ) return ;// 避免非stomp消息类型，例如心跳检测
		
		String sessionId = headerAccessor.getSessionAttributes().get("sessionId").toString();
		System.out.println("SocketChannelIntecepter -> sessionId = "+sessionId);
		
		switch (headerAccessor.getCommand()) {
		case CONNECT:
			connect(sessionId);
			break;
		case DISCONNECT:
			disconnect(sessionId);
			break;
		case SUBSCRIBE:
			
			break;
			
		case UNSUBSCRIBE:
			
			break;
		default:
			break;
		}
	}
	
	//连接成功
	private void connect(String sessionId){
		System.out.println("connect sessionId="+sessionId);
	}
	
	//断开连接
	private void disconnect(String sessionId){
		System.out.println("disconnect sessionId="+sessionId);
		//用户下线操作
		UserChatController.onlineUser.remove(sessionId);
	}
}
```

#### controller/v6

##### User.java

```java
package xdclass_websocket.controller.v6;

/** 
 * 功能描述：用户模型
 *
 */
public class User {
	private String username;
	private String pwd;
	public User() {}	
}
```

##### UserChatController.java

```java
package xdclass_websocket.controller.v6;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import xdclass_websocket.model.InMessage;
import xdclass_websocket.service.WebSocketService;



@Controller
public class UserChatController {
	@Autowired
	private WebSocketService ws;
	
	//模拟数据库用户的数据
	public static Map<String, String> userMap = new HashMap<String, String>();
	static{
		userMap.put("jack", "123");
		userMap.put("mary", "456");
		userMap.put("tom", "789");
		userMap.put("tim", "000");
		userMap.put("小D", "666");
	}
	
	//在线用户存储
	public static Map<String, User> onlineUser = new HashMap<>();
	static{
		onlineUser.put("123",new User("admin","888"));
	}
	/**
	 * 功能描述：用户登录
	 */
	@RequestMapping(value="login", method=RequestMethod.POST)
	public String userLogin( @RequestParam(value="username", required=true)String username, 
			@RequestParam(value="pwd",required=true) String pwd, HttpSession session) {
		
		String password = userMap.get(username);
		if (pwd.equals(password)) {
			User user = new User(username, pwd);
			String sessionId = session.getId();
			onlineUser.put(sessionId, user);
			return "redirect:/v6/chat.html";
		} else {
			return "redirect:/v6/error.html";
		}
		
	}
	/**
	 * 功能描述：用于定时给客户端推送在线用户
	 */
	@Scheduled(fixedRate = 2000)
	public void onlineUser() {
		
		ws.sendOnlineUser(onlineUser);
	}
	/**
	 * 功能描述 聊天接口
	 */
	@MessageMapping("/v6/chat")
	public void topicChat(InMessage message, SimpMessageHeaderAccessor headerAccessor){
		String sessionId = headerAccessor.getSessionAttributes().get("sessionId").toString();
		User user = onlineUser.get(sessionId);
		message.setFrom(user.getUsername());
		ws.sendTopicChat(message);
	}
}
```

#### service

##### WebSocketService.java

```java
package xdclass_websocket.service;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import xdclass_websocket.controller.v5.StockService;
import xdclass_websocket.controller.v6.User;
import xdclass_websocket.model.InMessage;
import xdclass_websocket.model.OutMessage;

/**
 * 功能描述：简单消息模板，用来推送消息
 */
@Service
public class WebSocketService {
	@Autowired
	private SimpMessagingTemplate template;
	
	public void sendTopicMessage(String dest, InMessage message) throws InterruptedException{
	
		for(int i=0; i<20; i++){
			Thread.sleep(500L);
			template.convertAndSend(dest, new OutMessage(message.getContent()+i));
		}
	}
	public void sendChatMessage(InMessage message) {
		template.convertAndSend("/chat/single/"+message.getTo(),
				new OutMessage(message.getFrom()+" 发送:"+ message.getContent()));
	}
	/**
	 * 功能描述：获取系统信息，推送给客户端
	 */
	public void sendServerInfo() {

		int processors = Runtime.getRuntime().availableProcessors();
		
		Long freeMem = Runtime.getRuntime().freeMemory();
		
		Long maxMem = Runtime.getRuntime().maxMemory();
		
		String message = String.format("服务器可用处理器:%s; 虚拟机空闲内容大小: %s; 最大内存大小: %s", processors,freeMem,maxMem );
		
		template.convertAndSend("/topic/server_info",new OutMessage(message));
		
	}

	/**
	 * 功能描述：v5 版本，股票信息推送
	 */
	public void sendStockInfo() {
		
		Map<String, String> stockInfoMap = StockService.getStockInfo();
    	String msgTpl = "名称: %s ; 价格: %s元 ; 最高价: %s ; 最低价: %s ; 涨跌幅: %s ; 市盈率TTM: %s ; 总市值: %s";
    	 
    	if (null != stockInfoMap) {
    		String msg = String.format(msgTpl, stockInfoMap.get("prod_name"), stockInfoMap.get("last_px"), stockInfoMap.get("high_px"), 
    				stockInfoMap.get("low_px"), stockInfoMap.get("px_change"), stockInfoMap.get("market_value"), stockInfoMap.get("amplitude") );	
    		
        	template.convertAndSend("/topic/stock_info",new OutMessage(msg));
    	}
	}

	/**
	 * 功能描述：发送在线用户
	 */
	public void sendOnlineUser(Map<String, User> onlineUser) {
		String msg = "";
		for(Map.Entry<String, User> entry : onlineUser.entrySet()){
			msg = msg.concat(entry.getValue().getUsername()+"||");
		} 
		System.out.println(msg);
		template.convertAndSend("/topic/onlineuser",new OutMessage(msg));
	}
	/**
	 * 功能描述： v6: 用于多人聊天
	 */
	public void sendTopicChat(InMessage message) {
		String msg = message.getFrom() +" 发送:"+message.getContent();
		template.convertAndSend("/topic/chat",new OutMessage(msg));
	}
}
```

### src/main/java/resourses

#### static/v6

##### chat.html

```html
<!DOCTYPE html>
<html>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<head>
    <title>devsq聊天室</title>
    <link href="/webjars/bootstrap/css/bootstrap.min.css" rel="stylesheet">
    <link href="/v6/main.css" rel="stylesheet">
    <script src="/webjars/jquery/jquery.min.js"></script>
    <script src="/webjars/sockjs-client/sockjs.min.js"></script>
    <script src="/webjars/stomp-websocket/stomp.min.js"></script>
    <script src="/v6/app.js"></script>
</head>
<body>

<div id="main-content" class="container">
    <div class="row">
        <div class="col-md-6">
            <form class="form-inline">
                <div class="form-group">
                    <label for="connect">建立连接通道:</label>
                    <button id="connect" class="btn btn-default" type="submit">Connect</button>
                    <button id="disconnect" class="btn btn-default" type="submit" disabled="disabled">Disconnect
                    </button>
                </div>
            </form>
        </div>
        <div class="col-md-6">
            <form class="form-inline">
                <div class="form-group">
                    <input type="text" id="content" class="form-control" placeholder="请输入...">
                </div>
                <button id="send" class="btn btn-default" type="submit">发送</button>
            </form>
        </div>
    </div>
    <div class="row">
        <div class="col-md-6">
            <table id="conversation" class="table table-striped">
                <thead>
                <tr>
                    <th>实时在线用户列表</th>
                </tr>
                </thead>
                <tbody id='online'>
                </tbody>
            </table>
        </div>
        
         <div class="col-md-6">
            <table id="conversation" class="table table-striped">
                <thead>
                <tr>
                    <th>聊天记录</th>
                </tr>
                </thead>
                <tbody id='record'>
                </tbody>
            </table>
        </div> 
    </div>
</div>
</body>
</html>
```

##### index.html

```html
<!DOCTYPE html>
<html>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<head>
    <title>Hello WebSocket</title>
    <link href="/webjars/bootstrap/css/bootstrap.min.css" rel="stylesheet">
    <link href="/v6/main.css" rel="stylesheet">
    <script src="/webjars/jquery/jquery.min.js"></script>
</head>
<body>

<div id="main-content" class="container">
    <div class="row">
        <div class="col-md-6">
            <form class="form-inline" method='post'  action="/login">
                <div class="form-group">
                    <input type="text" name="username" class="form-control" placeholder="用户名">
                    <input type="password" name="pwd" class="form-control" placeholder="密码">
                    <input type="submit" class="default" value="登录" />
                </div>
            </form>
        </div>
    </div>
</div>
</body>
</html>

```

##### app.js

```js
var stompClient = null;

function setConnected(connected) {
    $("#connect").prop("disabled", connected);
    $("#disconnect").prop("disabled", !connected);
    if (connected) {
        $("#conversation").show();
    }
    else {
        $("#conversation").hide();
    }
    $("#notice").html("");
}

function connect() {
	var socket = new SockJS('/endpoint-websocket');
    stompClient = Stomp.over(socket);
    stompClient.connect({}, function (frame) {
        setConnected(true);
        console.log('Connected: ' + frame);
        
        //订阅群聊消息
        stompClient.subscribe('/topic/chat', function (result) {
        	showContent(JSON.parse(result.body));
        });
        
        //订阅在线用户消息
        stompClient.subscribe('/topic/onlineuser', function (result) {
        	showOnlieUser(JSON.parse(result.body));
        });
        
        
    });
}


//断开连接
function disconnect() {
    if (stompClient !== null) {
        stompClient.disconnect();
    }
    setConnected(false);
    console.log("Disconnected");
}

//发送聊天记录
function sendContent() {
    stompClient.send("/app/v6/chat", {}, JSON.stringify({'content': $("#content").val()}));
    
}

//显示聊天记录
function showContent(body) {
    $("#record").append("<tr><td>" + body.content + "</td> <td>"+new Date(body.time).toLocaleTimeString()+"</td></tr>");
}

//显示实时在线用户
function showOnlieUser(body) {
    $("#online").html("<tr><td>" + body.content + "</td> <td>"+new Date(body.time).toLocaleTimeString()+"</td></tr>");
}

$(function () {
    
	connect();//自动上线
	
	$("form").on('submit', function (e) {
        e.preventDefault();
    });
     
    $( "#disconnect" ).click(function() { disconnect(); });
    $( "#send" ).click(function() {
    	sendContent(); 
    });
});
```