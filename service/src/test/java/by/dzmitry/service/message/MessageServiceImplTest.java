package by.dzmitry.service.message;

import by.dzmitry.dao.chat.ChatDao;
import by.dzmitry.dao.message.MessageDao;
import by.dzmitry.dao.profile.ProfileDao;
import by.dzmitry.model.advert.Advert;
import by.dzmitry.model.category.Category;
import by.dzmitry.model.chat.Chat;
import by.dzmitry.model.message.Message;
import by.dzmitry.model.profile.Profile;
import by.dzmitry.service.config.TestConfig;
import by.dzmitry.service.exception.BusinessException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = TestConfig.class)
class MessageServiceImplTest {

    @Autowired
    private MessageDao messageDao;

    @Autowired
    private ChatDao chatDao;

    @Autowired
    private ProfileDao profileDao;

    @Autowired
    private MessageService messageService;

    @Test
    void MessageServiceImpl_addMessage() {
        Profile profile = getTestProfileSender();
        Chat chat = getTestChat();
        ArgumentCaptor<Message> messageValues = ArgumentCaptor.forClass(Message.class);
        doNothing().when(messageDao).create(messageValues.capture());
        when(chatDao.findById(chat.getId())).thenReturn(chat);
        when(profileDao.findById(profile.getId())).thenReturn(profile);

        messageService.addMessage("Hi", profile.getId(), chat.getId());
        verify(messageDao, times(1)).create(messageValues.getValue());
        assertEquals("Hi", messageValues.getValue().getValue());
        assertEquals(profile, messageValues.getValue().getProfile());
        assertEquals(chat, messageValues.getValue().getChat());
    }

    @Test
    void MessageServiceImpl_addMessage_businessExceptionProfileNotFound() {
        Profile profile = getTestProfileSender();
        Chat chat = getTestChat();
        ArgumentCaptor<Message> messageValues = ArgumentCaptor.forClass(Message.class);
        doNothing().when(messageDao).create(messageValues.capture());
        when(chatDao.findById(chat.getId())).thenReturn(chat);
        when(profileDao.findById(profile.getId())).thenReturn(null);

        Throwable throwable = assertThrows(BusinessException.class, () -> {
            messageService.addMessage("Hi", profile.getId(), chat.getId());
        });
        assertNotNull(throwable.getMessage());
        assertEquals("No such profile", throwable.getMessage());
    }

    @Test
    void MessageServiceImpl_addMessage_businessExceptionChatNotFound() {
        Profile profile = getTestProfileSender();
        Chat chat = getTestChat();
        ArgumentCaptor<Message> messageValues = ArgumentCaptor.forClass(Message.class);
        doNothing().when(messageDao).create(messageValues.capture());
        when(chatDao.findById(chat.getId())).thenReturn(null);
        when(profileDao.findById(profile.getId())).thenReturn(profile);

        Throwable throwable = assertThrows(BusinessException.class, () -> {
            messageService.addMessage("Hi", profile.getId(), chat.getId());
        });
        assertNotNull(throwable.getMessage());
        assertEquals("No such chat", throwable.getMessage());
    }

    @Test
    void MessageServiceImpl_answerMessage() {
        Chat chat = getTestChat();
        ArgumentCaptor<Message> messageValues = ArgumentCaptor.forClass(Message.class);
        doNothing().when(messageDao).create(messageValues.capture());
        when(chatDao.findById(chat.getId())).thenReturn(chat);

        messageService.answerMessage("Hi", chat.getId());
        verify(messageDao, times(1)).create(messageValues.getValue());
        assertEquals("Hi", messageValues.getValue().getValue());
        assertEquals(chat, messageValues.getValue().getChat());
    }

    @Test
    void MessageServiceImpl_answerMessage_businessExceptionChatNotFound() {
        Chat chat = getTestChat();
        ArgumentCaptor<Message> messageValues = ArgumentCaptor.forClass(Message.class);
        doNothing().when(messageDao).create(messageValues.capture());
        when(chatDao.findById(chat.getId())).thenReturn(null);

        Throwable throwable = assertThrows(BusinessException.class, () -> {
            messageService.answerMessage("Hi", chat.getId());
        });
        assertNotNull(throwable.getMessage());
        assertEquals("No such chat", throwable.getMessage());
    }

    @Test
    void MessageServiceImpl_modifyMessage() {
        Message message = getTestMessage();
        String newValue = "Nihao";
        when(messageDao.findById(message.getId())).thenReturn(message);

        messageService.modifyMessage(message.getId(), newValue);
        assertEquals(newValue, message.getValue());
    }

    @Test
    void MessageServiceImpl_modifyMessage_businessExceptionMessageNotFound() {
        Message message = getTestMessage();
        String newValue = "Nihao";
        when(messageDao.findById(message.getId())).thenReturn(null);

        Throwable throwable = assertThrows(BusinessException.class, () -> {
            messageService.modifyMessage(message.getId(), newValue);
        });
        assertNotNull(throwable.getMessage());
        assertEquals("No such message", throwable.getMessage());
    }

    @Test
    void MessageServiceImpl_deleteMessage() {
        Message message = getTestMessage();
        messageDao.create(message);
        doAnswer(invocationOnMock -> {
            Object argument = invocationOnMock.getArgument(0);
            assertEquals(message.getId(), argument);
            return null;
        }).when(messageDao).deleteById(isA(Integer.class));
        when(messageDao.findById(message.getId())).thenReturn(message);

        messageService.deleteMessage(message.getId());
    }

    @Test
    void MessageServiceImpl_deleteMessage_businessExceptionMessageNotFound() {
        Message message = getTestMessage();
        when(messageDao.findById(message.getId())).thenReturn(null);

        Throwable throwable = assertThrows(BusinessException.class, () -> {
            messageService.deleteMessage(message.getId());
        });
        assertNotNull(throwable.getMessage());
        assertEquals("No such message", throwable.getMessage());
    }


    private Advert getTestAdvert() {
        return new Advert(1, "Hello", LocalDate.parse("2020-11-23"), 300, "some text",
                getTestCategory(), getTestProfile());
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

    private Chat getTestChat() {
        return new Chat(1, getTestProfileSender(), getTestAdvert());
    }

    private Message getTestMessage() {
        return new Message(1, "Hello", LocalDate.parse("2020-03-02"), getTestProfileSender(), getTestChat());
    }
}
