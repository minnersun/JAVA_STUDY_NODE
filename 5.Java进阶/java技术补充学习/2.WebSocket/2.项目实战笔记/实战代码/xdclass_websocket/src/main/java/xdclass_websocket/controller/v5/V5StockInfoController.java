package xdclass_websocket.controller.v5;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Controller;

import xdclass_websocket.service.WebSocketService;


/**
 * 
 * 功能描述：股票推送controller
 *
 * <p> 创建时间：Jan 6, 2018 </p> 
 * <p> 贡献者：小D学院, 官网：www.xdclass.net </p>
 *
 * @author <a href="mailto:xd@xdclass.net">小D老师</a>
 * @since 0.0.1
 */
@Controller
public class V5StockInfoController {

	@Autowired
	private WebSocketService ws;
	
	//@Scheduled(fixedRate=1000)
	public void stockInfo(){
		ws.sendStockInfo();
	}
	
}
