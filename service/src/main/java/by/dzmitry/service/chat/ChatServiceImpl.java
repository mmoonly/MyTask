package by.dzmitry.service.chat;

import by.dzmitry.dao.advert.AdvertDao;
import by.dzmitry.dao.chat.ChatDao;
import by.dzmitry.dao.profile.ProfileDao;
import by.dzmitry.model.advert.Advert;
import by.dzmitry.model.chat.Chat;
import by.dzmitry.model.message.Message;
import by.dzmitry.model.profile.Profile;
import by.dzmitry.service.dto.MessageDto;
import by.dzmitry.service.exception.BusinessException;
import by.dzmitry.service.util.ModelDtoMapper;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Log4j2
public class ChatServiceImpl implements ChatService {

    @Autowired
    private ChatDao chatDao;

    @Autowired
    private AdvertDao advertDao;

    @Autowired
    private ProfileDao profileDao;

    @Transactional
    @Override
    public Chat getChat(Integer advertId, Integer senderProfileId) {
        Advert advert = advertDao.findById(advertId);
        if (advert == null) {
            log.warn("There is no such advert while adding chat");
            throw new BusinessException("No such advert");
        }
        Profile profile = profileDao.findById(senderProfileId);
        if (profile == null) {
            log.warn("There is no such profile while adding chat");
            throw new BusinessException("No such profile");
        }
        if (advert.getProfile().equals(profile)) {
            log.warn("You can't chat with yourself while adding chat");
            throw new BusinessException("You can't chat with yourself");
        }
        Chat chat = getChatByAdvertAndProfile(advert, profile);
        if (chat == null) {
            chat = new Chat(profile, advert);
            chatDao.create(chat);
        }
        return chat;
    }

    @Transactional
    @Override
    public List<MessageDto> getMessages(Integer advertId, Integer senderProfileId) {
        Advert advert = advertDao.findById(advertId);
        if (advert == null) {
            log.warn("There is no such advert while adding chat");
            throw new BusinessException("No such advert");
        }
        Profile profile = profileDao.findById(senderProfileId);
        if (profile == null) {
            log.warn("There is no such profile while adding chat");
            throw new BusinessException("No such profile");
        }
        Chat chat = getChatByAdvertAndProfile(advert, profile);
        if (chat == null) {
            log.warn("There is no such chat while getting messages");
            throw new BusinessException("No such chat");
        }
        return convertMessageListToDto(chat.getMessages());
    }

    private Chat getChatByAdvertAndProfile(Advert advert, Profile profile) {
        List<Chat> chats = chatDao.findAll();
        if (chats != null) {
            for (Chat chat : chats) {
                if (chat.getSenderProfile().equals(profile) && chat.getAdvert().equals(advert)) {
                    return chat;
                }
            }
        }
        return null;
    }

    private List<MessageDto> convertMessageListToDto(List<Message> messages) {
        List<MessageDto> messagesDto = new ArrayList<>();
        for (Message message : messages) {
            messagesDto.add(ModelDtoMapper.convertToMessageDto(message));
        }
        return messagesDto;
    }
}
