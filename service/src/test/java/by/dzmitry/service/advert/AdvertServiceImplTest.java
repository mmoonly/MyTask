package by.dzmitry.service.advert;

import by.dzmitry.dao.advert.AdvertDao;
import by.dzmitry.dao.category.CategoryDao;
import by.dzmitry.dao.profile.ProfileDao;
import by.dzmitry.model.advert.Advert;
import by.dzmitry.model.category.Category;
import by.dzmitry.model.comment.Comment;
import by.dzmitry.model.profile.Profile;
import by.dzmitry.service.config.TestConfig;
import by.dzmitry.service.dto.AdvertDto;
import by.dzmitry.service.dto.CommentDto;
import by.dzmitry.service.dto.ProfileDto;
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

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = TestConfig.class)
class AdvertServiceImplTest {

    @Autowired
    private AdvertDao advertDao;

    @Autowired
    private CategoryDao categoryDao;

    @Autowired
    private ProfileDao profileDao;

    @Autowired
    private AdvertService advertService;

    @Test
    void AdvertServiceImpl_addAdvert() {
        Profile profile = getTestProfileHighRating();
        Category category = getTestCategory();
        ArgumentCaptor<Advert> advertValues = ArgumentCaptor.forClass(Advert.class);
        doNothing().when(advertDao).create(advertValues.capture());
        when(categoryDao.findById(category.getId())).thenReturn(category);
        when(profileDao.findById(profile.getId())).thenReturn(profile);
        Integer price = 200;

        advertService.addAdvert("Hello", "2020-03-06", price, "privet", profile.getId(), category.getId());
        verify(advertDao, times(1)).create(advertValues.getValue());
        assertEquals("Hello", advertValues.getValue().getHeading());
        assertEquals("2020-03-06", advertValues.getValue().getPublicationDate().toString());
        assertEquals(price, advertValues.getValue().getPrice());
        assertEquals("privet", advertValues.getValue().getDescription());
        assertEquals(profile, advertValues.getValue().getProfile());
        assertEquals(category, advertValues.getValue().getCategory());
        assertEquals(5, profile.getRating());
    }

    @Test
    void AdvertServiceImpl_deleteAdvert() {
        Profile profile = getTestProfileHighRating();
        Category category = getTestCategory();
        Advert advert = getTestAdvert(category, profile);
        doAnswer(invocationOnMock -> {
            Object argument = invocationOnMock.getArgument(0);
            assertEquals(advert.getId(), argument);
            return null;
        }).when(advertDao).deleteById(isA(Integer.class));
        when(advertDao.findById(advert.getId())).thenReturn(advert);

        advertService.deleteAdvert(advert.getId());
    }

    @Test
    void AdvertServiceImpl_deleteAdvert_businessExceptionAdvertNotFound() {
        Profile profile = getTestProfileHighRating();
        Category category = getTestCategory();
        Advert advert = getTestAdvert(category, profile);
        advertDao.create(advert);
        when(advertDao.findById(advert.getId())).thenReturn(null);

        Throwable throwable = assertThrows(BusinessException.class, () -> {
            advertService.deleteAdvert(advert.getId());
        });
        assertNotNull(throwable.getMessage());
        assertEquals("There is no such advert", throwable.getMessage());
    }

    @Test
    void AdvertServiceImpl_deleteAdvert_businessExceptionAdvertIsActive() {
        Profile profile = getTestProfileHighRating();
        Category category = getTestCategory();
        Advert advert = getTestAdvert(category, profile);
        advert.setActive(true);
        advertDao.create(advert);
        when(advertDao.findById(advert.getId())).thenReturn(advert);

        Throwable throwable = assertThrows(BusinessException.class, () -> {
            advertService.deleteAdvert(advert.getId());
        });
        assertNotNull(throwable.getMessage());
        assertEquals("This advert is active", throwable.getMessage());
    }

    @Test
    void AdvertServiceImpl_modifyAdvert() {
        Profile profile = getTestProfileHighRating();
        Category category = getTestCategory();
        Advert advert = getTestAdvert(category, profile);
        String newHeading = "new heading";
        Integer newPrice = 300;
        String newDescription = "new description";
        when(advertDao.findById(advert.getId())).thenReturn(advert);

        advertService.modifyAdvert(advert.getId(), newHeading, newPrice, newDescription);
        assertEquals(newHeading, advert.getHeading());
        assertEquals(newPrice, advert.getPrice());
        assertEquals(newDescription, advert.getDescription());
    }

    @Test
    void AdvertServiceImpl_modifyAdvert_businessExceptionNotFound() {
        Profile profile = getTestProfileHighRating();
        Category category = getTestCategory();
        Advert advert = getTestAdvert(category, profile);
        String newHeading = "new heading";
        Integer newPrice = 300;
        String newDescription = "new description";
        when(advertDao.findById(advert.getId())).thenReturn(null);

        Throwable throwable = assertThrows(BusinessException.class, () -> {
            advertService.modifyAdvert(advert.getId(), newHeading, newPrice, newDescription);
        });
        assertNotNull(throwable.getMessage());
        assertEquals("There is no such advert", throwable.getMessage());
    }

    @Test
    void AdvertServiceImpl_payAdvert() {
        Profile profile = getTestProfileHighRating();
        Category category = getTestCategory();
        String endPaidDate = "2020-11-24";
        Advert advert = getTestAdvert(category, profile);
        when(advertDao.findById(advert.getId())).thenReturn(advert);

        advertService.payAdvert(advert.getId(), endPaidDate);
        assertEquals(true, advert.getPaid());
        assertEquals(LocalDate.parse(endPaidDate), advert.getEndPaidDate());
    }

    @Test
    void AdvertServiceImpl_payAdvert_businessExceptionNotFound() {
        Profile profile = getTestProfileHighRating();
        Category category = getTestCategory();
        String endPaidDate = "2020-11-24";
        Advert advert = getTestAdvert(category, profile);
        when(advertDao.findById(advert.getId())).thenReturn(null);

        Throwable throwable = assertThrows(BusinessException.class, () -> {
            advertService.payAdvert(advert.getId(), endPaidDate);
        });
        assertNotNull(throwable.getMessage());
        assertEquals("There is no such advert", throwable.getMessage());
    }

    @Test
    void AdvertServiceImpl_closeAdvert() {
        Profile profile = getTestProfileHighRating();
        Category category = getTestCategory();
        Advert advert = getTestAdvert(category, profile);
        advert.setActive(true);
        advert.setPaid(false);
        advert.setEndPaidDate(LocalDate.parse("2020-11-20"));
        when(advertDao.findById(advert.getId())).thenReturn(advert);

        advertService.closeAdvert(advert.getId());
        assertEquals(false, advert.getPaid());
        assertEquals(false, advert.getActive());
        assertNull(advert.getEndPaidDate());
        assertEquals(7, profile.getRating());
    }

    @Test
    void AdvertServiceImpl_closeAdvert_businessExceptionNotFound() {
        Profile profile = getTestProfileHighRating();
        Category category = getTestCategory();
        Advert advert = getTestAdvert(category, profile);
        advert.setActive(true);
        advert.setPaid(false);
        advert.setEndPaidDate(LocalDate.parse("2020-11-20"));
        when(advertDao.findById(advert.getId())).thenReturn(null);

        Throwable throwable = assertThrows(BusinessException.class, () -> {
            advertService.closeAdvert(advert.getId());
        });
        assertNotNull(throwable.getMessage());
        assertEquals("There is no such advert", throwable.getMessage());
    }

    @Test
    void AdvertServiceImpl_getAdvertById() {
        Profile profile = getTestProfileHighRating();
        Category category = getTestCategory();
        Advert advert = getTestAdvert(category, profile);
        when(advertDao.findById(advert.getId())).thenReturn(advert);

        AdvertDto resultAdvert = advertService.getAdvertById(advert.getId());
        assertEquals(advert.getId(), resultAdvert.getId());
        assertEquals(advert.getHeading(), resultAdvert.getHeading());
        assertEquals(advert.getPublicationDate(), resultAdvert.getPublicationDate());
        assertEquals(advert.getPrice(), resultAdvert.getPrice());
        assertEquals(advert.getActive(), resultAdvert.getActive());
        assertEquals(advert.getPaid(), resultAdvert.getPaid());
        assertEquals(advert.getEndPaidDate(), resultAdvert.getEndPaidDate());
        assertEquals(advert.getDescription(), resultAdvert.getDescription());
    }

    @Test
    void AdvertServiceImpl_getAdvertById_businessExceptionNotFound() {
        Profile profile = getTestProfileHighRating();
        Category category = getTestCategory();
        Advert advert = getTestAdvert(category, profile);
        when(advertDao.findById(advert.getId())).thenReturn(null);

        Throwable throwable = assertThrows(BusinessException.class, () -> {
            advertService.getAdvertById(advert.getId());
        });
        assertNotNull(throwable.getMessage());
        assertEquals("There is no such advert", throwable.getMessage());
    }

    @Test
    void AdvertServiceImpl_getAllAdverts() {
        List<Advert> testAdverts = sort(getTestAdverts());
        when(advertDao.findAll()).thenReturn(getTestAdverts());

        List<AdvertDto> resultAdverts = advertService.getAllAdverts();
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
    void AdvertServiceImpl_getActiveAdverts() {
        List<Advert> testAdverts = sort(getTestAdverts());
        when(advertDao.getActiveAdverts()).thenReturn(getTestAdverts());

        List<AdvertDto> resultAdverts = advertService.getActiveAdverts();
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
    void AdvertServiceImpl_getClosedAdverts() {
        List<Advert> testAdverts = sortByRating(getTestAdverts());
        when(advertDao.getClosedAdverts()).thenReturn(getTestAdverts());

        List<AdvertDto> resultAdverts = advertService.getClosedAdverts();
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
    void AdvertServiceImpl_getPaidAdverts() {
        List<Advert> testAdverts = sortByRating(getTestAdverts());
        when(advertDao.getPaidAdverts()).thenReturn(getTestAdverts());

        List<AdvertDto> resultAdverts = advertService.getPaidAdverts();
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
    void AdvertServiceImpl_getProfileByAdvert() {
        Profile profile = getTestProfileHighRating();
        Category category = getTestCategory();
        Advert advert = getTestAdvert(category, profile);
        when(advertDao.findById(advert.getId())).thenReturn(advert);

        ProfileDto resultProfile = advertService.getProfileByAdvert(advert.getId());
        assertNotNull(resultProfile);
        assertEquals(profile.getId(), resultProfile.getId());
        assertEquals(profile.getName(), resultProfile.getName());
        assertEquals(profile.getSurname(), resultProfile.getSurname());
        assertEquals(profile.getRating(), resultProfile.getRating());
    }

    @Test
    void AdvertServiceImpl_getProfileByAdvert_businessExceptionNotFound() {
        Profile profile = getTestProfileHighRating();
        Category category = getTestCategory();
        Advert advert = getTestAdvert(category, profile);
        when(advertDao.findById(advert.getId())).thenReturn(null);

        Throwable throwable = assertThrows(BusinessException.class, () -> {
            advertService.getProfileByAdvert(advert.getId());
        });
        assertNotNull(throwable.getMessage());
        assertEquals("There is no such advert", throwable.getMessage());
    }

    @Test
    void AdvertServiceImpl_getCommentsByAdvert() {
        Advert advert = getTestAdvert(getTestCategory(), getTestProfileHighRating());
        List<Comment> comments = getTestComments(advert);
        advert.setComments(comments);
        when(advertDao.findById(advert.getId())).thenReturn(advert);

        List<CommentDto> resultComments = advertService.getCommentsByAdvert(advert.getId());
        assertNotNull(resultComments);
        assertEquals(comments.size(), resultComments.size());
        for (int i = 0; i < resultComments.size(); i++) {
            assertEquals(comments.get(i).getId(), resultComments.get(i).getId());
            assertEquals(comments.get(i).getValue(), resultComments.get(i).getValue());
            assertEquals(comments.get(i).getPublicationDate(), resultComments.get(i).getPublicationDate());
        }
    }

    @Test
    void AdvertServiceImpl_getCommentsByAdvert_businessExceptionNotFound() {
        Advert advert = getTestAdvert(getTestCategory(), getTestProfileHighRating());
        List<Comment> comments = getTestComments(advert);
        advert.setComments(comments);
        when(advertDao.findById(advert.getId())).thenReturn(null);

        Throwable throwable = assertThrows(BusinessException.class, () -> {
            advertService.getCommentsByAdvert(advert.getId());
        });
        assertNotNull(throwable.getMessage());
        assertEquals("There is no such advert", throwable.getMessage());
    }

    @Test
    void AdvertServiceImpl_getCommentsByAdvert_businessExceptionNoComments() {
        Advert advert = getTestAdvert(getTestCategory(), getTestProfileHighRating());
        List<Comment> comments = new ArrayList<>();
        advert.setComments(comments);
        when(advertDao.findById(advert.getId())).thenReturn(advert);

        Throwable throwable = assertThrows(BusinessException.class, () -> {
            advertService.getCommentsByAdvert(advert.getId());
        });
        assertNotNull(throwable.getMessage());
        assertEquals("No any comments", throwable.getMessage());
    }

    private List<Advert> getTestAdverts() {
        List<Advert> adverts = new ArrayList<>();
        adverts.add(new Advert(1, "Hello", LocalDate.parse("2020-11-23"), 300, true, false,
                null, "some text", getTestCategory(), getTestProfileHighRating()));
        adverts.add(new Advert(2, "Low", LocalDate.parse("2020-11-24"), 400, true, true, LocalDate.now().plusDays(2),
                "texting..", getTestCategory(), getTestProfileLowRating()));
        adverts.add(new Advert(3, "High", LocalDate.parse("2020-11-20"), 100, false, false, null,
                "no text", getTestCategory(), getTestProfileHighRating()));
        adverts.add(new Advert(4, "Hi", LocalDate.parse("2020-11-22"), 200, false, false, null,
                "best description", getTestCategory(), getTestProfileLowRating()));
        adverts.add(new Advert(5, "Privet", LocalDate.parse("2020-11-21"), 150, true, false, null,
                "no value", getTestCategory(), getTestProfileHighRating()));
        return adverts;
    }

    private Advert getTestAdvert(Category category, Profile profile) {
        return new Advert(1, "Hello", LocalDate.parse("2020-11-23"), 300, "some text", category, profile);
    }

    private Profile getTestProfileHighRating() {
        return new Profile(1, "Petya", "Vasechkin", 4);
    }

    private Profile getTestProfileLowRating() {
        return new Profile(2, "Valera", "Petrikov", 2);
    }

    private Category getTestCategory() {
        return new Category(1, "Thread");
    }

    private List<Comment> getTestComments(Advert advert) {
        List<Comment> comments = new ArrayList<>();
        comments.add(new Comment(1, "lol", LocalDate.parse("2020-03-11"), getTestProfileLowRating(), advert));
        comments.add(new Comment(2, "Hi", LocalDate.parse("2020-03-15"), getTestProfileLowRating(), advert));
        comments.add(new Comment(3, "Hello", LocalDate.parse("2020-03-08"), getTestProfileHighRating(), advert));
        return comments;
    }

    private List<Advert> sort(List<Advert> adverts) {
        return adverts.stream()
                .sorted(Comparator.comparing(Advert::getActive).reversed()
                        .thenComparing(Advert::getPaid).reversed()
                        .thenComparing((a1, a2) -> a2.getProfile().getRating().compareTo(a1.getProfile().getRating())))
                .collect(Collectors.toList());
    }

    private List<Advert> sortByRating(List<Advert> adverts) {
        return adverts.stream()
                .sorted((a1, a2) -> a2.getProfile().getRating().compareTo(a1.getProfile().getRating()))
                .collect(Collectors.toList());
    }
}
