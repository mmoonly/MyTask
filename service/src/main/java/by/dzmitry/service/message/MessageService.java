package by.dzmitry.service.message;

public interface MessageService {

    void addMessage(String value, Integer profileId, Integer chatId);

    void answerMessage(String value, Integer chatId);

    void modifyMessage(Integer messageId, String value);

    void deleteMessage(Integer messageId);
}
