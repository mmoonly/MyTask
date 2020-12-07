package by.dzmitry.dao.advert;

import by.dzmitry.dao.abstractdao.GenericDao;
import by.dzmitry.model.advert.Advert;

import java.util.List;

public interface AdvertDao extends GenericDao<Advert, Integer> {

    List<Advert> getActiveAdverts();

    List<Advert> getClosedAdverts();

    List<Advert> getPaidAdverts();
}
