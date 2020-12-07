package by.dzmitry.service.chat;

import by.dzmitry.model.chat.Chat;
import by.dzmitry.service.dto.MessageDto;

import java.util.List;

public interface ChatService {

    Chat getChat(Integer advertId, Integer senderProfileId);

    List<MessageDto> getMessages(Integer advertId, Integer senderProfileId);
}
