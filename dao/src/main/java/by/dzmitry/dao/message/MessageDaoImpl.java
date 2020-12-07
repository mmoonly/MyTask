package by.dzmitry.dao.message;

import by.dzmitry.dao.abstractdao.AbstractDao;
import by.dzmitry.model.message.Message;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Repository
public class MessageDaoImpl extends AbstractDao<Message, Integer> implements MessageDao {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    protected EntityManager getEntityManager() {
        return entityManager;
    }

    @Override
    protected Class<Message> getClassType() {
        return Message.class;
    }
}
