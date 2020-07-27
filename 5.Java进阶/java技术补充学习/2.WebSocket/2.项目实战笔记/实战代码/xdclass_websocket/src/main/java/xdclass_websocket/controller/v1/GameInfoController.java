package xdclass_websocket.controller.v1;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import xdclass_websocket.model.InMessage;
import xdclass_websocket.model.OutMessage;


@Controller
public class GameInfoController {

	
	@MessageMapping("/v1/chat")
	@SendTo("/topic/game_chat")
	public OutMessage gameInfo(InMessage message){
		System.out.println("GameInfoController->gameInfo");
		return new OutMessage(message.getContent());
	}

	
	

}



