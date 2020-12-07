package by.dzmitry.service.category;

import by.dzmitry.dao.category.CategoryDao;
import by.dzmitry.model.advert.Advert;
import by.dzmitry.model.category.Category;
import by.dzmitry.model.profile.Profile;
import by.dzmitry.service.config.TestConfig;
import by.dzmitry.service.dto.AdvertDto;
import by.dzmitry.service.exception.BusinessException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.isA;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = TestConfig.class)
class CategoryServiceImplTest {

    @Autowired
    private CategoryDao categoryDao;

    @Autowired
    private CategoryService categoryService;

    @Test
    void CategoryServiceImpl_addCategory() {
        ArgumentCaptor<Category> categoriesValues = ArgumentCaptor.forClass(Category.class);
        doNothing().when(categoryDao).create(categoriesValues.capture());

        categoryService.addCategory("Electronic");
        verify(categoryDao, times(1)).create(categoriesValues.getValue());
        assertEquals("Electronic", categoriesValues.getValue().getThread());
    }

    @Test
    void CategoryServiceImpl_addCategoryWithParent() {
        Category category = getTestCategory();
        ArgumentCaptor<Category> categoriesValues = ArgumentCaptor.forClass(Category.class);
        doNothing().when(categoryDao).create(categoriesValues.capture());
        when(categoryDao.findById(category.getId())).thenReturn(category);

        categoryService.addCategoryWithParent(category.getId(), "Phones");
        verify(categoryDao, times(1)).create(categoriesValues.getValue());
        assertEquals("Phones", categoriesValues.getValue().getThread());
    }

    @Test
    void CategoryServiceImpl_addCategoryWithParent_businessExceptionNotFound() {
        Category category = getTestCategory();
        ArgumentCaptor<Category> categoriesValues = ArgumentCaptor.forClass(Category.class);
        doNothing().when(categoryDao).create(categoriesValues.capture());
        when(categoryDao.findById(category.getId())).thenReturn(null);

        Throwable throwable = assertThrows(BusinessException.class, () -> {
            categoryService.addCategoryWithParent(category.getId(), "Phones");
        });
        assertNotNull(throwable.getMessage());
        assertEquals("No such category", throwable.getMessage());
    }

    @Test
    void CategoryServiceImpl_modifyCategory() {
        Category category = getTestCategory();
        String newThread = "new thread";
        when(categoryDao.findById(category.getId())).thenReturn(category);

        categoryService.modifyCategory(category.getId(), newThread);
        assertEquals(newThread, category.getThread());
    }

    @Test
    void CategoryServiceImpl_modifyCategory_businessExceptionNotFound() {
        Category category = getTestCategory();
        String newThread = "new thread";
        when(categoryDao.findById(category.getId())).thenReturn(null);

        Throwable throwable = assertThrows(BusinessException.class, () -> {
            categoryService.modifyCategory(category.getId(), newThread);
        });
        assertNotNull(throwable.getMessage());
        assertEquals("No such category", throwable.getMessage());
    }

    @Test
    void CategoryServiceImpl_deleteCategory() {
        Category category = getTestCategory();
        doAnswer(invocationOnMock -> {
            Object argument = invocationOnMock.getArgument(0);
            assertEquals(category.getId(), argument);
            return null;
        }).when(categoryDao).deleteById(isA(Integer.class));
        when(categoryDao.findById(category.getId())).thenReturn(category);

        categoryService.deleteCategory(category.getId());
    }

    @Test
    void CategoryServiceImpl_deleteCategory_businessExceptionNotFound() {
        Category category = getTestCategory();
        when(categoryDao.findById(category.getId())).thenReturn(null);

        Throwable throwable = assertThrows(BusinessException.class, () -> {
            categoryService.deleteCategory(category.getId());
        });
        assertNotNull(throwable.getMessage());
        assertEquals("No such category", throwable.getMessage());
    }

    @Test
    void CategoryServiceImpl_deleteCategory_businessExceptionDeleteParentCategory() {
        Category category = getTestCategoryWithParent();
        when(categoryDao.findById(category.getId())).thenReturn(category);

        Throwable throwable = assertThrows(BusinessException.class, () -> {
            categoryService.deleteCategory(category.getId());
        });
        assertNotNull(throwable.getMessage());
        assertEquals("Can't delete parent category", throwable.getMessage());
    }

    @Test
    void CategoryServiceImpl_getAdvertsByCategory() {
        Category category = getTestCategoryWithParent();
        List<Advert> testAdverts = getTestAdverts();
        category.setAdverts(testAdverts);
        testAdverts = sort(testAdverts);
        when(categoryDao.findById(category.getId())).thenReturn(category);

        List<AdvertDto> resultAdverts = categoryService.getAdvertsByCategory(category.getId());
        assertNotNull(resultAdverts);
        assertEquals(testAdverts.size(), resultAdverts.size());
        for (int i = 0; i < resultAdverts.size(); i++) {
            assertEquals(testAdverts.get(i).getId(), resultAdverts.get(i).getId());
            assertEquals(testAdverts.get(i).getHeading(), resultAdverts.get(i).getHeading());
            assertEquals(testAdverts.get(i).getPublicationDate(), resultAdverts.get(i).getPublicationDate());
            assertEquals(testAdverts.get(i).getPrice(), resultAdverts.get(i).getPrice());
            assertEquals(testAdverts.get(i).getPrice(), resultAdverts.get(i).getPrice());
            assertEquals(testAdverts.get(i).getActive(), resultAdverts.get(i).getActive());
            assertEquals(testAdverts.get(i).getPaid(), resultAdverts.get(i).getPaid());
            assertEquals(testAdverts.get(i).getEndPaidDate(), resultAdverts.get(i).getEndPaidDate());
            assertEquals(testAdverts.get(i).getDescription(), resultAdverts.get(i).getDescription());
        }
    }

    @Test
    void CategoryServiceImpl_getAdvertsByCategory_businessExceptionCategoryNotFound() {
        Category category = getTestCategoryWithParent();
        List<Advert> testAdverts = getTestAdverts();
        category.setAdverts(testAdverts);
        when(categoryDao.findById(category.getId())).thenReturn(null);

        Throwable throwable = assertThrows(BusinessException.class, () -> {
            categoryService.getAdvertsByCategory(category.getId());
        });
        assertNotNull(throwable.getMessage());
        assertEquals("No such category", throwable.getMessage());
    }

    @Test
    void CategoryServiceImpl_getAdvertsByCategory_businessExceptionParentCategory() {
        Category parentCategory = getTestCategory();
        Category category = getTestCategoryWithParent();
        List<Advert> testAdverts = getTestAdverts();
        category.setAdverts(testAdverts);
        when(categoryDao.findById(parentCategory.getId())).thenReturn(parentCategory);

        Throwable throwable = assertThrows(BusinessException.class, () -> {
            categoryService.getAdvertsByCategory(parentCategory.getId());
        });
        assertNotNull(throwable.getMessage());
        assertEquals("This is parent category", throwable.getMessage());
    }

    @Test
    void CategoryServiceImpl_getAdvertsByCategory_businessExceptionAdvertsNotFound() {
        Category category = getTestCategoryWithParent();
        List<Advert> testAdverts = new ArrayList<>();
        category.setAdverts(testAdverts);
        when(categoryDao.findById(category.getId())).thenReturn(category);

        Throwable throwable = assertThrows(BusinessException.class, () -> {
            categoryService.getAdvertsByCategory(category.getId());
        });
        assertNotNull(throwable.getMessage());
        assertEquals("No adverts in is category", throwable.getMessage());
    }


    private Category getTestCategory() {
        return new Category(1, "Electronic");
    }

    private Category getTestCategoryWithParent() {
        return new Category(2, "Phone", getTestCategory());
    }

    private Profile getTestProfileHighRating() {
        return new Profile(1, "Petya", "Vasechkin", 4);
    }

    private List<Advert> getTestAdverts() {
        List<Advert> adverts = new ArrayList<>();
        adverts.add(new Advert(1, "Hello", LocalDate.parse("2020-11-23"), 300, true, false,
                null, "some text", getTestCategory(), getTestProfileHighRating()));
        adverts.add(new Advert(2, "Low", LocalDate.parse("2020-11-24"), 400, true, true, LocalDate.now().plusDays(2),
                "texting..", getTestCategory(), getTestProfileHighRating()));
        adverts.add(new Advert(3, "High", LocalDate.parse("2020-11-20"), 100, false, false, null,
                "no text", getTestCategory(), getTestProfileHighRating()));
        adverts.add(new Advert(4, "Hi", LocalDate.parse("2020-11-22"), 200, false, false, null,
                "best description", getTestCategory(), getTestProfileHighRating()));
        adverts.add(new Advert(5, "Privet", LocalDate.parse("2020-11-21"), 150, true, false, null,
                "no value", getTestCategory(), getTestProfileHighRating()));
        return adverts;
    }

    private List<Advert> sort(List<Advert> adverts) {
        return adverts.stream()
                .sorted(Comparator.comparing(Advert::getActive).reversed()
                        .thenComparing(Advert::getPaid).reversed()
                        .thenComparing((a1, a2) -> a2.getProfile().getRating().compareTo(a1.getProfile().getRating())))
                .collect(Collectors.toList());
    }

}
