package by.dzmitry.dao.profile;

import by.dzmitry.dao.abstractdao.AbstractDao;
import by.dzmitry.model.profile.Profile;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Repository
public class ProfileDaoImpl extends AbstractDao<Profile, Integer> implements ProfileDao {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    protected EntityManager getEntityManager() {
        return entityManager;
    }

    @Override
    protected Class<Profile> getClassType() {
        return Profile.class;
    }
}
