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
	 * 
	 * 功能描述：获取系统信息，推送给客户端
	 *
	 * <p> 创建时间：Jan 5, 2018 </p> 
	 * <p> 贡献者：小D学院, 官网：www.xdclass.net </p>
	 *
	 * @author <a href="mailto:xd@xdclass.net">小D老师</a>
	 * @since 0.0.1
	 */
	public void sendServerInfo() {

		int processors = Runtime.getRuntime().availableProcessors();
		
		Long freeMem = Runtime.getRuntime().freeMemory();
		
		Long maxMem = Runtime.getRuntime().maxMemory();
		
		String message = String.format("服务器可用处理器:%s; 虚拟机空闲内容大小: %s; 最大内存大小: %s", processors,freeMem,maxMem );
		
		template.convertAndSend("/topic/server_info",new OutMessage(message));
		
	}

	/**
	 * 
	 * 功能描述：v5 版本，股票信息推送
	 *
	 * <p> 创建时间：Jan 6, 2018 </p> 
	 * <p> 贡献者：小D学院, 官网：www.xdclass.net </p>
	 *
	 * @author <a href="mailto:xd@xdclass.net">小D老师</a>
	 * @since 0.0.1
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
	 * 
	 * 功能描述：发送在线用户
	 *
	 * <p> 创建时间：Jan 6, 2018 </p> 
	 * <p> 贡献者：小D学院, 官网：www.xdclass.net </p>
	 *
	 * @author <a href="mailto:xd@xdclass.net">小D老师</a>
	 * @since 0.0.1
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
	 *  
	 * 功能描述： v6: 用于多人聊天
	 *
	 * <p> 创建时间：Jan 6, 2018 </p> 
	 * <p> 贡献者：小D学院, 官网：www.xdclass.net </p>
	 *
	 * @author <a href="mailto:xd@xdclass.net">小D老师</a>
	 * @since 0.0.1
	 */
	public void sendTopicChat(InMessage message) {
		String msg = message.getFrom() +" 发送:"+message.getContent();
		template.convertAndSend("/topic/chat",new OutMessage(msg));
	}
	
	
}
