## WebSocket整合SpringBoot监听器

----

### WebSocket两种推送方法

##### @SendTo

> 不通用，固定发送给指定的订阅者

##### SimpMessagingTemplate

> 灵活，支持多种发送方式



### xdclass_websocket

#### src/main/java/

##### xdclass_websocket/model

###### InMessage.java

```java
package xdclass_websocket.model;

import java.util.Date;

public class InMessage {
	
	private String from;
	
	private String to;
	
	private String content;
	
	private Date time;
}
```

###### OutMessage.java

````java
package xdclass_websocket.model;

import java.util.Date;

public class OutMessage {

	private String from;
	
	private String content;
	
	private Date time = new Date();
}
````

##### xdclass_websocket/config/

###### WebSocketConfig.java

```java
package xdclass_websocket.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.AbstractWebSocketMessageBrokerConfigurer;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig extends AbstractWebSocketMessageBrokerConfigurer{

	/**
	 * 注册端点，发布或者订阅消息的时候需要连接此端点
	 * setAllowedOrigins 非必须，*表示允许其他域进行连接
	 * withSockJS  表示开始sockejs支持
	 */
	public void registerStompEndpoints(StompEndpointRegistry registry) {

		registry.addEndpoint("/endpoint-websocket").setAllowedOrigins("*").withSockJS();
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

##### xdclass_websocket/controller/v2

###### V2GameInfoController.java

```java
package xdclass_websocket.controller.v2;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import xdclass_websocket.model.InMessage;
import xdclass_websocket.model.OutMessage;
import xdclass_websocket.service.WebSocketService;


@Controller
public class V2GameInfoController {

	@Autowired
	private WebSocketService ws;
	
	@MessageMapping("/v2/chat")
	public void gameInfo(InMessage message) throws InterruptedException{
		
		ws.sendTopicMessage("/topic/game_rank",message);
	}
}
```

##### xdclass_websocket/service

###### WebSocketService.java

```java
package xdclass_websocket.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import xdclass_websocket.model.InMessage;
import xdclass_websocket.model.OutMessage;

/**
 * 
 * 功能描述：简单消息模板，用来推送消息
 *
 * <p> 创建时间：Jan 4, 2018 </p> 
 * <p> 贡献者：小D学院, 官网：www.xdclass.net </p>
 *
 * @author <a href="mailto:xd@xdclass.net">小D老师</a>
 * @since 0.0.1
 */
@Service
public class WebSocketService {

	@Autowired
	private SimpMessagingTemplate template;	//消息模板
	
	public void sendTopicMessage(String dest, InMessage message) throws InterruptedException{
	
		for(int i=0; i<20; i++){
			Thread.sleep(500L);
			template.convertAndSend(dest, new OutMessage(message.getContent()+i));
		}
	}
}
```

#### src/main/resources

##### static/v2

###### index.html

```html
<!DOCTYPE html>
<html>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<head>
    <title>Hello WebSocket</title>
    <link href="/webjars/bootstrap/css/bootstrap.min.css" rel="stylesheet">
    <link href="/v2/main.css" rel="stylesheet">
    <script src="/webjars/jquery/jquery.min.js"></script>
    <script src="/webjars/sockjs-client/sockjs.min.js"></script>
    <script src="/webjars/stomp-websocket/stomp.min.js"></script>
    <script src="/v2/app.js"></script>
</head>
<body>
<noscript><h2 style="color: #ff0000">Seems your browser doesn't support Javascript! Websocket relies on Javascript being
    enabled. Please enable
    Javascript and reload this page!</h2></noscript>
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
                    <label for="name">发布新公告</label>
                    <input type="text" id="content" class="form-control" placeholder="请输入...">
                </div>
                <button id="send" class="btn btn-default" type="submit">发布</button>
            </form>
        </div>
    </div>
    <div class="row">
        <div class="col-md-12">
            <table id="conversation" class="table table-striped">
                <thead>
                <tr>
                    <th>吃鸡游戏排行榜</th>
                </tr>
                </thead>
                <tbody id="notice">
                </tbody>
            </table>
        </div>
    </div>
</div>
</body>
</html>

```

###### app.js

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
        stompClient.subscribe('/topic/game_rank', function (result) {
        	showContent(JSON.parse(result.body));
        });
    });
}

function disconnect() {
    if (stompClient !== null) {
        stompClient.disconnect();
    }
    setConnected(false);
    console.log("Disconnected");
}

function sendName() {
	
    stompClient.send("/app/v2/chat", {}, JSON.stringify({'content': $("#content").val()}));
}

function showContent(body) {
    $("#notice").append("<tr><td>" + body.content + "</td> <td>"+new Date(body.time).toLocaleString()+"</td></tr>");
}

$(function () {
    $("form").on('submit', function (e) {
        e.preventDefault();
    });
    $( "#connect" ).click(function() { connect(); });
    $( "#disconnect" ).click(function() { disconnect(); });
    $( "#send" ).click(function() { sendName(); });
});


```





### SpringBoot针对WebSocket的四类监听器

#### 监听器类型

> SessionSubscribeEvent  订阅事件   
>
> SessionUnsubscribeEvent 取消订阅事件   
>
> SessionDisconnectEvent  断开连接事件   
>
> SessionDisconnectEvent  建立连接事件 

#### 监听器案例: 

##### src/main/xdclass_websocket/listener

> 需要监听器类需要实现接口`ApplicationListener<T>` T表示事件类型，下列几种都是对应的 websocket事件类型
>
> 在监听器类上注解 `@Component`，spring会把改类纳入管理 

###### ConnectEventListener.java

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

###### SubscribeEventListener.java

```java
package xdclass_websocket.listener;

import org.springframework.context.ApplicationListener;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionSubscribeEvent;

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
	}
}
```





### 点对点聊天案例

#### src/main/java/xdclass_websocket/controller/v3/

##### V3ChatRoomContoller.java

```java
package xdclass_websocket.controller.v3;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;

import xdclass_websocket.model.InMessage;
import xdclass_websocket.service.WebSocketService;

@Controller
public class V3ChatRoomContoller {

	@Autowired
	private WebSocketService ws;
    
	@MessageMapping("/v3/single/chat")
	public void singleChat(InMessage message) {
		ws.sendChatMessage(message);
	}
}
```

#### src/main/resources/static/v3

##### index.html

```html
<!DOCTYPE html>
<html>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<head>
    <title>Hello WebSocket</title>
    <link href="/webjars/bootstrap/css/bootstrap.min.css" rel="stylesheet">
    <link href="/v3/main.css" rel="stylesheet">
    <script src="/webjars/jquery/jquery.min.js"></script>
    <script src="/webjars/sockjs-client/sockjs.min.js"></script>
    <script src="/webjars/stomp-websocket/stomp.min.js"></script>
    <script src="/v3/app.js"></script>
</head>
<body>
<noscript><h2 style="color: #ff0000">Seems your browser doesn't support Javascript! Websocket relies on Javascript being
    enabled. Please enable
    Javascript and reload this page!</h2></noscript>
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
                    <input type="text" id="from" class="form-control" placeholder="我是">
                    <input type="text" id="to" class="form-control" placeholder="发送给谁">
                    
                    <input type="text" id="content" class="form-control" placeholder="请输入...">
                    
                    
                </div>
                <button id="send" class="btn btn-default" type="submit">发送</button>
            </form>
        </div>
    </div>
    <div class="row">
        <div class="col-md-12">
            <table id="conversation" class="table table-striped">
                <thead>
                <tr>
                    <th>记录</th>
                </tr>
                </thead>
                <tbody id="notice">
                </tbody>
            </table>
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
	var from = $("#from").val();
	var socket = new SockJS('/endpoint-websocket');
    stompClient = Stomp.over(socket);
    stompClient.connect({}, function (frame) {
        setConnected(true);
        console.log('Connected: ' + frame);
        stompClient.subscribe('/chat/single/'+from, function (result) {
        	showContent(JSON.parse(result.body));
        });
    });
}

function disconnect() {
    if (stompClient !== null) {
        stompClient.disconnect();
    }
    setConnected(false);
    console.log("Disconnected");
}

function sendName() {
	
    stompClient.send("/app/v3/single/chat", {}, JSON.stringify({'content': $("#content").val(), 'to':$("#to").val(), 'from':$("#from").val()}));
    
    
}

function showContent(body) {
    $("#notice").append("<tr><td>" + body.content + "</td> <td>"+new Date(body.time).toLocaleString()+"</td></tr>");
}

$(function () {
    $("form").on('submit', function (e) {
        e.preventDefault();
    });
    $( "#connect" ).click(function() { connect(); });
    $( "#disconnect" ).click(function() { disconnect(); });
    $( "#send" ).click(function() { sendName(); });
});


```