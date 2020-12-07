package by.dzmitry.service.category;

import by.dzmitry.service.dto.AdvertDto;

import java.util.List;

public interface CategoryService {

    void addCategory(String thread);

    void addCategoryWithParent(Integer categoryId, String thread);

    void modifyCategory(Integer categoryId, String thread);

    void deleteCategory(Integer categoryId);

    List<AdvertDto> getAdvertsByCategory(Integer categoryId);
}
