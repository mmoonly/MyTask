package by.dzmitry.service.message;

import by.dzmitry.dao.chat.ChatDao;
import by.dzmitry.dao.message.MessageDao;
import by.dzmitry.dao.profile.ProfileDao;
import by.dzmitry.model.advert.Advert;
import by.dzmitry.model.chat.Chat;
import by.dzmitry.model.message.Message;
import by.dzmitry.model.profile.Profile;
import by.dzmitry.service.exception.BusinessException;
import lombok.extern.log4j.Log4j2;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@Service
@Log4j2
public class MessageServiceImpl implements MessageService {

    @Autowired
    private MessageDao messageDao;

    @Autowired
    private ChatDao chatDao;

    @Autowired
    private ProfileDao profileDao;

    @Transactional
    @Override
    public void addMessage(String value, Integer profileId, Integer chatId) {
        Profile profile = profileDao.findById(profileId);
        if (profile == null) {
            log.warn("There is no such profile while adding message");
            throw new BusinessException("No such profile");
        }
        Chat chat = chatDao.findById(chatId);
        if (chat == null) {
            log.warn("There is no such chat while adding message");
            throw new BusinessException("No such chat");
        }
        Message message = new Message(value, LocalDate.now(), profile, chat);
        messageDao.create(message);
    }

    @Transactional
    @Override
    public void answerMessage(String value, Integer chatId) {
        Chat chat = chatDao.findById(chatId);
        if (chat == null) {
            log.warn("There is no such chat while adding message");
            throw new BusinessException("No such chat");
        }
        Advert advert = chat.getAdvert();
        Profile profile = advert.getProfile();
        Message message = new Message(value, LocalDate.now(), profile, chat);
        messageDao.create(message);
    }

    @Transactional
    @Override
    public void modifyMessage(Integer messageId, String value) {
        Message message = messageDao.findById(messageId);
        if (message == null) {
            log.warn("There is no message chat while modifying message");
            throw new BusinessException("No such message");
        }
        message.setValue(value);
        messageDao.update(message);
    }

    @Transactional
    @Override
    public void deleteMessage(Integer messageId) {
        Message message = messageDao.findById(messageId);
        if (message == null) {
            log.warn("There is no message chat while deleting message");
            throw new BusinessException("No such message");
        }
        messageDao.deleteById(messageId);
    }
}
