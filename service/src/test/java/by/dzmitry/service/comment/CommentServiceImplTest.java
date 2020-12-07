package by.dzmitry.service.comment;

import by.dzmitry.dao.advert.AdvertDao;
import by.dzmitry.dao.comment.CommentDao;
import by.dzmitry.dao.profile.ProfileDao;
import by.dzmitry.model.advert.Advert;
import by.dzmitry.model.category.Category;
import by.dzmitry.model.comment.Comment;
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
class CommentServiceImplTest {

    @Autowired
    private CommentDao commentDao;

    @Autowired
    private AdvertDao advertDao;

    @Autowired
    private ProfileDao profileDao;

    @Autowired
    private CommentService commentService;

    @Test
    void CommentServiceImpl_addComment() {
        Advert advert = getTestAdvert(getTestCategory(), getTestProfile());
        Profile profile = getTestProfileSender();
        ArgumentCaptor<Comment> commentValues = ArgumentCaptor.forClass(Comment.class);
        doNothing().when(commentDao).create(commentValues.capture());
        when(advertDao.findById(advert.getId())).thenReturn(advert);
        when(profileDao.findById(profile.getId())).thenReturn(profile);

        commentService.addComment("Hi", profile.getId(), advert.getId());
        verify(commentDao, times(1)).create(commentValues.getValue());
        assertEquals("Hi", commentValues.getValue().getValue());
        assertEquals(profile, commentValues.getValue().getProfile());
        assertEquals(advert, commentValues.getValue().getAdvert());
    }

    @Test
    void CommentServiceImpl_addComment_businessExceptionAdvertNotFound() {
        Advert advert = getTestAdvert(getTestCategory(), getTestProfile());
        Profile profile = getTestProfileSender();
        ArgumentCaptor<Comment> commentValues = ArgumentCaptor.forClass(Comment.class);
        doNothing().when(commentDao).create(commentValues.capture());
        when(advertDao.findById(advert.getId())).thenReturn(null);
        when(profileDao.findById(profile.getId())).thenReturn(profile);

        Throwable throwable = assertThrows(BusinessException.class, () -> {
            commentService.addComment("Hi", profile.getId(), advert.getId());
        });
        assertNotNull(throwable.getMessage());
        assertEquals("No such advert", throwable.getMessage());
    }

    @Test
    void CommentServiceImpl_addComment_businessExceptionProfileNotFound() {
        Advert advert = getTestAdvert(getTestCategory(), getTestProfile());
        Profile profile = getTestProfileSender();
        ArgumentCaptor<Comment> commentValues = ArgumentCaptor.forClass(Comment.class);
        doNothing().when(commentDao).create(commentValues.capture());
        when(advertDao.findById(advert.getId())).thenReturn(advert);
        when(profileDao.findById(profile.getId())).thenReturn(null);

        Throwable throwable = assertThrows(BusinessException.class, () -> {
            commentService.addComment("Hi", profile.getId(), advert.getId());
        });
        assertNotNull(throwable.getMessage());
        assertEquals("No such profile", throwable.getMessage());
    }

    @Test
    void CommentServiceImpl_modifyComment() {
        Comment comment = getTestComment();
        String newValue = "Nihao";
        when(commentDao.findById(comment.getId())).thenReturn(comment);

        commentService.modifyComment(comment.getId(), newValue);
        assertEquals(newValue, comment.getValue());
    }

    @Test
    void CommentServiceImpl_modifyComment_businessExceptionAdvertNotFound() {
        Comment comment = getTestComment();
        String newValue = "Nihao";
        when(commentDao.findById(comment.getId())).thenReturn(null);

        Throwable throwable = assertThrows(BusinessException.class, () -> {
            commentService.modifyComment(comment.getId(), newValue);
        });
        assertNotNull(throwable.getMessage());
        assertEquals("No such comment", throwable.getMessage());
    }

    @Test
    void CommentServiceImpl_deleteComment() {
        Comment comment = getTestComment();
        commentDao.create(comment);
        doAnswer(invocationOnMock -> {
            Object argument = invocationOnMock.getArgument(0);
            assertEquals(comment.getId(), argument);
            return null;
        }).when(commentDao).deleteById(isA(Integer.class));
        when(commentDao.findById(comment.getId())).thenReturn(comment);

        commentService.deleteComment(comment.getId());
    }

    @Test
    void CommentServiceImpl_deleteComment_businessExceptionNotFound() {
        Comment comment = getTestComment();
        when(commentDao.findById(comment.getId())).thenReturn(null);

        Throwable throwable = assertThrows(BusinessException.class, () -> {
            commentService.deleteComment(comment.getId());
        });
        assertNotNull(throwable.getMessage());
        assertEquals("No such comment", throwable.getMessage());
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

    private Comment getTestComment() {
        return new Comment(1, "Hello", LocalDate.parse("2020-03-02"), getTestProfileSender(),
                getTestAdvert(getTestCategory(), getTestProfile()));
    }
}
