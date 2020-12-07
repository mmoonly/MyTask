package by.dzmitry.service.category;

import by.dzmitry.dao.category.CategoryDao;
import by.dzmitry.model.advert.Advert;
import by.dzmitry.model.category.Category;
import by.dzmitry.service.dto.AdvertDto;
import by.dzmitry.service.exception.BusinessException;
import by.dzmitry.service.util.ModelDtoMapper;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Log4j2
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    private CategoryDao categoryDao;

    @Transactional
    @Override
    public void addCategory(String thread) {
        Category category = new Category(thread);
        categoryDao.create(category);
    }

    @Transactional
    @Override
    public void addCategoryWithParent(Integer categoryId, String thread) {
        Category parent = categoryDao.findById(categoryId);
        if (parent == null) {
            log.warn("There is no such category while adding category with parent");
            throw new BusinessException("No such category");
        }
        Category category = new Category(thread, parent);
        categoryDao.create(category);
    }

    @Transactional
    @Override
    public void modifyCategory(Integer categoryId, String thread) {
        Category category = categoryDao.findById(categoryId);
        if (category == null) {
            log.warn("There is no such category while modifying category");
            throw new BusinessException("No such category");
        }
        category.setThread(thread);
        categoryDao.update(category);
    }

    @Transactional
    @Override
    public void deleteCategory(Integer categoryId) {
        Category category = categoryDao.findById(categoryId);
        if (category == null) {
            log.warn("There is no such category while deleting category");
            throw new BusinessException("No such category");
        }
        if (category.getParent() != null) {
            log.warn("Can't delete parent category");
            throw new BusinessException("Can't delete parent category");
        }
        categoryDao.deleteById(categoryId);
    }

    @Transactional
    @Override
    public List<AdvertDto> getAdvertsByCategory(Integer categoryId) {
        Category category = categoryDao.findById(categoryId);
        if (category == null) {
            log.warn("There is no such category while deleting category");
            throw new BusinessException("No such category");
        }
        if (category.getParent() == null) {
            log.warn("No adverts in parent category");
            throw new BusinessException("This is parent category");
        }
        List<Advert> adverts = category.getAdverts();
        if (adverts.isEmpty()) {
            log.warn("No added adverts");
            throw new BusinessException("No adverts in is category");
        }
        return convertAdvertListToDto(adverts.stream()
                .sorted(Comparator.comparing(Advert::getActive).reversed()
                        .thenComparing(Advert::getPaid).reversed()
                        .thenComparing((a1, a2) -> a2.getProfile().getRating().compareTo(a1.getProfile().getRating())))
                .collect(Collectors.toList()));
    }

    private List<AdvertDto> convertAdvertListToDto(List<Advert> adverts) {
        List<AdvertDto> advertsDto = new ArrayList<>();
        for (Advert advert : adverts) {
            advertsDto.add(ModelDtoMapper.convertToAdvertDto(advert));
        }
        return advertsDto;
    }
}
