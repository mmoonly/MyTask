package by.dzmitry.service.profile;

import by.dzmitry.dao.profile.ProfileDao;
import by.dzmitry.model.advert.Advert;
import by.dzmitry.model.category.Category;
import by.dzmitry.model.profile.Profile;
import by.dzmitry.service.config.TestConfig;
import by.dzmitry.service.dto.AdvertDto;
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
import java.util.List;

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
class ProfileServiceImplTest {

    @Autowired
    private ProfileDao profileDao;

    @Autowired
    private ProfileService profileService;

    @Test
    void ProfileServiceImpl_addProfile() {
        ArgumentCaptor<Profile> profilesValues = ArgumentCaptor.forClass(Profile.class);
        doNothing().when(profileDao).create(profilesValues.capture());

        profileService.addProfile("Petya", "Putin");
        verify(profileDao, times(1)).create(profilesValues.getValue());
        assertEquals("Petya", profilesValues.getValue().getName());
        assertEquals("Putin", profilesValues.getValue().getSurname());
    }

    @Test
    void ProfileServiceImpl_modifyProfile() {
        Profile profile = getTestProfile();
        String newName = "Kostya";
        String newSurname = "Pavlik";
        when(profileDao.findById(profile.getId())).thenReturn(profile);

        profileService.modifyProfile(profile.getId(), newName, newSurname);
        assertEquals(newName, profile.getName());
        assertEquals(newSurname, profile.getSurname());
    }

    @Test
    void ProfileServiceImpl_modifyProfile_businessExceptionProfileNotFound() {
        Profile profile = getTestProfile();
        String newName = "Kostya";
        String newSurname = "Pavlik";
        when(profileDao.findById(profile.getId())).thenReturn(null);

        Throwable throwable = assertThrows(BusinessException.class, () -> {
            profileService.modifyProfile(profile.getId(), newName, newSurname);
        });
        assertNotNull(throwable.getMessage());
        assertEquals("No such profile", throwable.getMessage());
    }

    @Test
    void ProfileServiceImpl_deleteProfile() {
        Profile profile = getTestProfile();
        profileDao.create(profile);
        doAnswer(invocationOnMock -> {
            Object argument = invocationOnMock.getArgument(0);
            assertEquals(profile.getId(), argument);
            return null;
        }).when(profileDao).deleteById(isA(Integer.class));
        when(profileDao.findById(profile.getId())).thenReturn(profile);

        profileService.deleteProfile(profile.getId());
    }

    @Test
    void ProfileServiceImpl_deleteProfile_businessExceptionProfileNotFound() {
        Profile profile = getTestProfile();
        when(profileDao.findById(profile.getId())).thenReturn(null);

        Throwable throwable = assertThrows(BusinessException.class, () -> {
            profileService.deleteProfile(profile.getId());
        });
        assertNotNull(throwable.getMessage());
        assertEquals("No such profile", throwable.getMessage());
    }

    @Test
    void ProfileServiceImpl_getAllProfiles() {
        List<Profile> testProfiles = getTestProfiles();
        when(profileDao.findAll()).thenReturn(testProfiles);

        List<ProfileDto> resultProfiles = profileService.getAllProfiles();
        assertNotNull(resultProfiles);
        assertEquals(testProfiles.size(), resultProfiles.size());
        for (int i = 0; i < resultProfiles.size(); i++) {
            assertEquals(testProfiles.get(i).getId(), resultProfiles.get(i).getId());
            assertEquals(testProfiles.get(i).getName(), resultProfiles.get(i).getName());
            assertEquals(testProfiles.get(i).getSurname(), resultProfiles.get(i).getSurname());
            assertEquals(testProfiles.get(i).getRating(), resultProfiles.get(i).getRating());
        }
    }

    @Test
    void ProfileServiceImpl_getAllAdvertsByProfile() {
        Profile profile = getTestProfile();
        List<Advert> testAdverts = getTestAdverts();
        profile.setAdverts(testAdverts);
        when(profileDao.findById(profile.getId())).thenReturn(profile);

        List<AdvertDto> resultAdverts = profileService.getAllAdvertsByProfile(profile.getId());
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
    void ProfileServiceImpl_getActiveAdvertsByProfile() {
        Profile profile = getTestProfile();
        List<Advert> testAdverts = getTestAdverts();
        profile.setAdverts(testAdverts);
        when(profileDao.findById(profile.getId())).thenReturn(profile);

        List<AdvertDto> resultAdverts = profileService.getActiveAdvertsByProfile(profile.getId());
        filterActiveAdverts(testAdverts);
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
    void ProfileServiceImpl_getClosedAdvertsByProfile() {
        Profile profile = getTestProfile();
        List<Advert> testAdverts = getTestAdverts();
        profile.setAdverts(testAdverts);
        when(profileDao.findById(profile.getId())).thenReturn(profile);

        List<AdvertDto> resultAdverts = profileService.getClosedAdvertsByProfile(profile.getId());
        filterClosedAdverts(testAdverts);
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


    private Profile getTestProfile() {
        return new Profile(1, "Vasya", "Kurolesov", 1);
    }

    private List<Profile> getTestProfiles() {
        List<Profile> profiles = new ArrayList<>();
        profiles.add(new Profile(1, "Vasya", "Kurolesov", 1));
        profiles.add(new Profile(2, "Petya", "Kurolesov", 1));
        profiles.add(new Profile(3, "Kostya", "Kurolesov", 1));
        return profiles;
    }

    private Category getTestCategory() {
        return new Category(1, "Thread");
    }

    private List<Advert> getTestAdverts() {
        List<Advert> adverts = new ArrayList<>();
        adverts.add(new Advert(1, "Hello", LocalDate.parse("2020-11-23"), 300, true, false,
                null, "some text", getTestCategory(), getTestProfile()));
        adverts.add(new Advert(2, "Low", LocalDate.parse("2020-11-24"), 400, true, true, LocalDate.now().plusDays(2),
                "texting..", getTestCategory(), getTestProfile()));
        adverts.add(new Advert(3, "High", LocalDate.parse("2020-11-20"), 100, false, false, null,
                "no text", getTestCategory(), getTestProfile()));
        adverts.add(new Advert(4, "Hi", LocalDate.parse("2020-11-22"), 200, false, false, null,
                "best description", getTestCategory(), getTestProfile()));
        adverts.add(new Advert(5, "Privet", LocalDate.parse("2020-11-21"), 150, true, false, null,
                "no value", getTestCategory(), getTestProfile()));
        return adverts;
    }

    private List<Advert> filterActiveAdverts(List<Advert> adverts) {
        adverts.removeIf(advert -> !advert.getActive());
        return adverts;
    }

    private List<Advert> filterClosedAdverts(List<Advert> adverts) {
        adverts.removeIf(Advert::getActive);
        return adverts;
    }

}
