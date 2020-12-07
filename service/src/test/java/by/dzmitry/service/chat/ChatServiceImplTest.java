package by.dzmitry.service.chat;

import by.dzmitry.dao.advert.AdvertDao;
import by.dzmitry.dao.chat.ChatDao;
import by.dzmitry.dao.profile.ProfileDao;
import by.dzmitry.model.advert.Advert;
import by.dzmitry.model.category.Category;
import by.dzmitry.model.chat.Chat;
import by.dzmitry.model.message.Message;
import by.dzmitry.model.profile.Profile;
import by.dzmitry.service.config.TestConfig;
import by.dzmitry.service.dto.MessageDto;
import by.dzmitry.service.exception.BusinessException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = TestConfig.class)
class ChatServiceImplTest {

    @Autowired
    private ChatDao chatDao;

    @Autowired
    private ChatService chatService;

    @Autowired
    private AdvertDao advertDao;

    @Autowired
    private ProfileDao profileDao;

    @Test
    void ChatServiceImpl_getChat() {
        Category category = getTestCategory();
        Profile profile = getTestProfileSender();
        Advert advert = getTestAdvert(category, getTestProfile());
        ArgumentCaptor<Chat> chatValues = ArgumentCaptor.forClass(Chat.class);
        doNothing().when(chatDao).create(chatValues.capture());
        when(advertDao.findById(advert.getId())).thenReturn(advert);
        when(profileDao.findById(profile.getId())).thenReturn(profile);

        chatService.getChat(advert.getId(), profile.getId());
        verify(chatDao, times(1)).create(chatValues.getValue());
        assertEquals(advert, chatValues.getValue().getAdvert());
        assertEquals(profile, chatValues.getValue().getSenderProfile());
    }

    @Test
    void ChatServiceImpl_getChat_businessExceptionAdvertNotFound() {
        Category category = getTestCategory();
        Profile profile = getTestProfileSender();
        Advert advert = getTestAdvert(category, getTestProfile());
        ArgumentCaptor<Chat> chatValues = ArgumentCaptor.forClass(Chat.class);
        doNothing().when(chatDao).create(chatValues.capture());
        when(advertDao.findById(advert.getId())).thenReturn(null);
        when(profileDao.findById(profile.getId())).thenReturn(profile);

        Throwable throwable = assertThrows(BusinessException.class, () -> {
            chatService.getChat(advert.getId(), profile.getId());
        });
        assertNotNull(throwable.getMessage());
        assertEquals("No such advert", throwable.getMessage());
    }


    @Test
    void ChatServiceImpl_getChat_businessExceptionProfileNotFound() {
        Category category = getTestCategory();
        Profile profile = getTestProfileSender();
        Advert advert = getTestAdvert(category, getTestProfile());
        ArgumentCaptor<Chat> chatValues = ArgumentCaptor.forClass(Chat.class);
        doNothing().when(chatDao).create(chatValues.capture());
        when(advertDao.findById(advert.getId())).thenReturn(advert);
        when(profileDao.findById(profile.getId())).thenReturn(null);

        Throwable throwable = assertThrows(BusinessException.class, () -> {
            chatService.getChat(advert.getId(), profile.getId());
        });
        assertNotNull(throwable.getMessage());
        assertEquals("No such profile", throwable.getMessage());
    }

    @Test
    void ChatServiceImpl_getMessages() {
        Category category = getTestCategory();
        Profile profile = getTestProfileSender();
        Advert advert = getTestAdvert(category, getTestProfile());
        List<Chat> chats = getTestChat(profile, advert);
        List<Message> messages = getTestMessages(chats.get(0));
        chats.get(0).setMessages(messages);
        when(advertDao.findById(advert.getId())).thenReturn(advert);
        when(profileDao.findById(profile.getId())).thenReturn(profile);
        when(chatDao.findAll()).thenReturn(chats);

        List<MessageDto> resultMessages = chatService.getMessages(advert.getId(), profile.getId());
        assertNotNull(resultMessages);
        assertEquals(messages.size(), resultMessages.size());
        for (int i = 0; i < resultMessages.size(); i++) {
            assertEquals(messages.get(i).getId(), resultMessages.get(i).getId());
            assertEquals(messages.get(i).getValue(), resultMessages.get(i).getValue());
            assertEquals(messages.get(i).getSendDate(), resultMessages.get(i).getSendDate());
        }
    }

    @Test
    void ChatServiceImpl_getMessages_businessExceptionAdvertNotFound() {
        Category category = getTestCategory();
        Profile profile = getTestProfileSender();
        Advert advert = getTestAdvert(category, getTestProfile());
        List<Chat> chats = getTestChat(profile, advert);
        List<Message> messages = getTestMessages(chats.get(0));
        chats.get(0).setMessages(messages);
        when(advertDao.findById(advert.getId())).thenReturn(null);
        when(profileDao.findById(profile.getId())).thenReturn(profile);
        when(chatDao.findAll()).thenReturn(chats);

        Throwable throwable = assertThrows(BusinessException.class, () -> {
            chatService.getMessages(advert.getId(), profile.getId());
        });
        assertNotNull(throwable.getMessage());
        assertEquals("No such advert", throwable.getMessage());
    }

    @Test
    void ChatServiceImpl_getMessages_businessExceptionProfileNotFound() {
        Category category = getTestCategory();
        Profile profile = getTestProfileSender();
        Advert advert = getTestAdvert(category, getTestProfile());
        List<Chat> chats = getTestChat(profile, advert);
        List<Message> messages = getTestMessages(chats.get(0));
        chats.get(0).setMessages(messages);
        when(advertDao.findById(advert.getId())).thenReturn(advert);
        when(profileDao.findById(profile.getId())).thenReturn(null);
        when(chatDao.findAll()).thenReturn(chats);

        Throwable throwable = assertThrows(BusinessException.class, () -> {
            chatService.getMessages(advert.getId(), profile.getId());
        });
        assertNotNull(throwable.getMessage());
        assertEquals("No such profile", throwable.getMessage());
    }

    @Test
    void ChatServiceImpl_getMessages_businessExceptionChatNotFound() {
        Category category = getTestCategory();
        Profile profile = getTestProfileSender();
        Advert advert = getTestAdvert(category, getTestProfile());
        List<Chat> chats = getTestChat(profile, advert);
        List<Message> messages = getTestMessages(chats.get(0));
        chats.get(0).setMessages(messages);
        when(advertDao.findById(advert.getId())).thenReturn(advert);
        when(profileDao.findById(profile.getId())).thenReturn(profile);
        when(chatDao.findAll()).thenReturn(null);

        Throwable throwable = assertThrows(BusinessException.class, () -> {
            chatService.getMessages(advert.getId(), profile.getId());
        });
        assertNotNull(throwable.getMessage());
        assertEquals("No such chat", throwable.getMessage());
    }

    private Advert getTestAdvert(Category category, Profile profile) {
        return new Advert(1, "Hello", LocalDate.parse("2020-11-23"), 300, "some text", category, profile);
    }

    private Profile getTestProfile() {
        return new Profile(1, "Petya", "Vasechkin", 4);
    }

    private Profile getTestProfileSender() {
        return new Profile(2, "Vasya", "Vasechkin", 2);
    }

    private Category getTestCategory() {
        return new Category(1, "Thread");
    }

    private List<Chat> getTestChat(Profile profileSender, Advert advert) {
        List<Chat> chats = new ArrayList<>();
        chats.add(new Chat(1, profileSender, advert));
        return chats;
    }

    private List<Message> getTestMessages(Chat chat) {
        List<Message> messages = new ArrayList<>();
        messages.add(new Message(1, "Hi", LocalDate.parse("2020-03-02"), getTestProfileSender(), chat));
        messages.add(new Message(2, "Hello", LocalDate.parse("2020-03-02"), getTestProfile(), chat));
        return messages;
    }
}
