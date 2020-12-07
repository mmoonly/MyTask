package by.dzmitry.dao.advert;

import by.dzmitry.dao.abstractdao.AbstractDao;
import by.dzmitry.model.advert.Advert;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;

@Repository
public class AdvertDaoImpl extends AbstractDao<Advert, Integer> implements AdvertDao {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    protected EntityManager getEntityManager() {
        return entityManager;
    }

    @Override
    protected Class<Advert> getClassType() {
        return Advert.class;
    }

    @Override
    public List<Advert> getActiveAdverts() {
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Advert> query = builder.createQuery(Advert.class);
        Root<Advert> root = query.from(Advert.class);
        query.select(root);
        query.where(builder.equal(root.get("active"), true));
        return entityManager.createQuery(query).getResultList();
    }

    @Override
    public List<Advert> getClosedAdverts() {
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Advert> query = builder.createQuery(Advert.class);
        Root<Advert> root = query.from(Advert.class);
        query.select(root);
        query.where(builder.equal(root.get("active"), false));
        return entityManager.createQuery(query).getResultList();
    }

    @Override
    public List<Advert> getPaidAdverts() {
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Advert> query = builder.createQuery(Advert.class);
        Root<Advert> root = query.from(Advert.class);
        query.select(root);
        query.where(builder.equal(root.get("paid"), true));
        return entityManager.createQuery(query).getResultList();
    }
}
