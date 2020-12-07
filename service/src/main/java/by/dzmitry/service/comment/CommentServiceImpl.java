package by.dzmitry.service.comment;

import by.dzmitry.dao.advert.AdvertDao;
import by.dzmitry.dao.comment.CommentDao;
import by.dzmitry.dao.profile.ProfileDao;
import by.dzmitry.model.advert.Advert;
import by.dzmitry.model.comment.Comment;
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
public class CommentServiceImpl implements CommentService {

    @Autowired
    private CommentDao commentDao;

    @Autowired
    private ProfileDao profileDao;

    @Autowired
    private AdvertDao advertDao;

    @Transactional
    @Override
    public void addComment(String value, Integer profileId, Integer advertId) {
        Profile profile = profileDao.findById(profileId);
        if (profile == null) {
            log.warn("There is no such profile while adding comment");
            throw new BusinessException("No such profile");
        }
        Advert advert = advertDao.findById(advertId);
        if (advert == null) {
            log.warn("There is no such advert while adding comment");
            throw new BusinessException("No such advert");
        }
        Comment comment = new Comment(value, LocalDate.now(), profile, advert);
        commentDao.create(comment);
    }

    @Transactional
    @Override
    public void modifyComment(Integer commentId, String newValue) {
        Comment comment = commentDao.findById(commentId);
        if (comment == null) {
            log.warn("There is no such comment while modifying comment");
            throw new BusinessException("No such comment");
        }
        comment.setValue(newValue);
        commentDao.update(comment);
    }

    @Transactional
    @Override
    public void deleteComment(Integer commentId) {
        Comment comment = commentDao.findById(commentId);
        if (comment == null) {
            log.warn("There is no such comment while deleting comment");
            throw new BusinessException("No such comment");
        }
        commentDao.deleteById(commentId);
    }
}
