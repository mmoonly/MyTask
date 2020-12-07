package by.dzmitry.dao.abstractdao;


import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaDelete;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;

public abstract class AbstractDao<T, E> implements GenericDao<T, E> {

    protected abstract EntityManager getEntityManager();

    protected abstract Class<T> getClassType();

    public AbstractDao() {

    }

    public List<T> findAll() {
        Class<T> type = getClassType();
        CriteriaBuilder builder = getEntityManager().getCriteriaBuilder();
        CriteriaQuery<T> query = builder.createQuery(type);
        Root<T> root = query.from(type);
        query.select(root);
        return getEntityManager().createQuery(query).getResultList();
    }

    public T findById(E id) {
        return getEntityManager().find(getClassType(), id);
    }

    public void create(T entity) {
        getEntityManager().persist(entity);
    }

    public void update(T entity) {
        getEntityManager().merge(entity);
    }

    public void deleteById(E id) {
        Class<T> type = getClassType();
        CriteriaBuilder builder = getEntityManager().getCriteriaBuilder();
        CriteriaDelete<T> query = builder.createCriteriaDelete(type);
        Root<T> root = query.from(type);
        query.where(builder.equal(root.get("id"), id));
        getEntityManager().createQuery(query).executeUpdate();
    }
}
