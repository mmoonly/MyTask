package by.dzmitry.dao.comment;

import by.dzmitry.dao.abstractdao.AbstractDao;
import by.dzmitry.model.comment.Comment;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Repository
public class CommentDaoImpl extends AbstractDao<Comment, Integer> implements CommentDao {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    protected EntityManager getEntityManager() {
        return entityManager;
    }

    @Override
    protected Class<Comment> getClassType() {
        return Comment.class;
    }
}
