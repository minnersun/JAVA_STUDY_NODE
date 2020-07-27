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
	 * 
	 * 功能描述：用户登录
	 *
	 * <p> 创建时间：Jan 6, 2018 </p> 
	 * <p> 贡献者：小D学院, 官网：www.xdclass.net </p>
	 *
	 * @author <a href="mailto:xd@xdclass.net">小D老师</a>
	 * @since 0.0.1
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
	 * 
	 * 功能描述：用于定时给客户端推送在线用户
	 *
	 * <p> 创建时间：Jan 6, 2018 </p> 
	 * <p> 贡献者：小D学院, 官网：www.xdclass.net </p>
	 *
	 * @author <a href="mailto:xd@xdclass.net">小D老师</a>
	 * @since 0.0.1
	 */
	@Scheduled(fixedRate = 2000)
	public void onlineUser() {
		
		ws.sendOnlineUser(onlineUser);
	}
	
	
	
	
	
	/**
	 * 
	 * 功能描述 聊天接口
	 * 
	 * message 消息体
	 * headerAccessor 消息头访问器，通过这个获取sessionId
	 *
	 * <p> 创建时间：Jan 6, 2018 </p> 
	 * <p> 贡献者：小D学院, 官网：www.xdclass.net </p>
	 *
	 * @author <a href="mailto:xd@xdclass.net">小D老师</a>
	 * @since 0.0.1
	 */
	@MessageMapping("/v6/chat")
	public void topicChat(InMessage message, SimpMessageHeaderAccessor headerAccessor){
		String sessionId = headerAccessor.getSessionAttributes().get("sessionId").toString();
		User user = onlineUser.get(sessionId);
		message.setFrom(user.getUsername());
		ws.sendTopicChat(message);
		
	}
	
	
	
	
}
