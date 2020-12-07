package by.dzmitry.dao.abstractdao;

import java.util.List;

public interface GenericDao<T, E> {

    List<T> findAll();

    T findById(E id);

    void create(T entity);

    void update(T entity);

    void deleteById(E id);
}
