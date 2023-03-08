package m09uf3.impl.chatroom;

import java.util.logging.Logger;

import m09uf3.prob.chatroom.ChatRoomHandler;

public class ChatRoomHandlerImpl implements ChatRoomHandler {
	static final Logger LOGGER = Logger.getLogger(ChatRoomHandlerImpl.class.getName());
	@Override
	public void message(String username, String message) {
	LOGGER.info("recived "+username+":"+message);
		
	}

}
