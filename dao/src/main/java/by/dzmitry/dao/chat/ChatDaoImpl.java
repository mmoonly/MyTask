package by.dzmitry.dao.chat;

import by.dzmitry.dao.abstractdao.AbstractDao;
import by.dzmitry.model.chat.Chat;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Repository
public class ChatDaoImpl extends AbstractDao<Chat, Integer> implements ChatDao {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    protected EntityManager getEntityManager() {
        return entityManager;
    }

    @Override
    protected Class<Chat> getClassType() {
        return Chat.class;
    }
}
