package by.dzmitry.service.config;

import by.dzmitry.dao.advert.AdvertDao;
import by.dzmitry.dao.category.CategoryDao;
import by.dzmitry.dao.chat.ChatDao;
import by.dzmitry.dao.comment.CommentDao;
import by.dzmitry.dao.message.MessageDao;
import by.dzmitry.dao.profile.ProfileDao;
import by.dzmitry.dao.user.UserDao;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.mockito.Mockito.mock;

@Configuration
@ComponentScan("by.dzmitry.service")
public class TestConfig {

    @Bean
    public ProfileDao profileDao() {
        return mock(ProfileDao.class);
    }

    @Bean
    public AdvertDao advertDao() {
        return mock(AdvertDao.class);
    }

    @Bean
    public CategoryDao categoryDao() {
        return mock(CategoryDao.class);
    }

    @Bean
    public ChatDao chatDao() {
        return mock(ChatDao.class);
    }

    @Bean
    public CommentDao commentDao() {
        return mock(CommentDao.class);
    }

    @Bean
    public MessageDao messageDao() {
        return mock(MessageDao.class);
    }

    @Bean
    public UserDao userDao() {
        return mock(UserDao.class);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return mock(PasswordEncoder.class);
    }

}
