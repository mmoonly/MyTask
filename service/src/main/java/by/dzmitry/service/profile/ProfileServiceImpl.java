package by.dzmitry.service.profile;

import by.dzmitry.dao.profile.ProfileDao;
import by.dzmitry.model.advert.Advert;
import by.dzmitry.model.profile.Profile;
import by.dzmitry.service.dto.AdvertDto;
import by.dzmitry.service.dto.ProfileDto;
import by.dzmitry.service.exception.BusinessException;
import by.dzmitry.service.util.ModelDtoMapper;
import lombok.extern.log4j.Log4j2;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Log4j2
public class ProfileServiceImpl implements ProfileService {

    @Autowired
    private ProfileDao profileDao;

    @Transactional
    @Override
    public void addProfile(String name, String surname) {
        Profile profile = new Profile(name, surname);
        profileDao.create(profile);
    }

    @Transactional
    @Override
    public void modifyProfile(Integer profileId, String name, String surname) {
        Profile profile = profileDao.findById(profileId);
        if (profile == null) {
            log.warn("There is no such profile while modifying profile");
            throw new BusinessException("No such profile");
        }
        profile.setName(name);
        profile.setSurname(surname);
        profileDao.update(profile);
    }

    @Transactional
    @Override
    public void deleteProfile(Integer profileId) {
        Profile profile = profileDao.findById(profileId);
        if (profile == null) {
            log.warn("There is no such profile while deleting profile");
            throw new BusinessException("No such profile");
        }
        profileDao.deleteById(profileId);
    }

    @Override
    public List<ProfileDto> getAllProfiles() {
        List<Profile> profiles = profileDao.findAll();
        if (profiles.isEmpty()) {
            log.warn("No added profiles while getting all profile");
            throw new BusinessException("No added profiles");
        }
        return convertProfileListToDto(profiles);
    }

    @Transactional
    @Override
    public List<AdvertDto> getAllAdvertsByProfile(Integer profileId) {
        Profile profile = profileDao.findById(profileId);
        if (profile == null) {
            log.warn("There is no such profile while getting all adverts by profile");
            throw new BusinessException("No such profile");
        }
        List<Advert> adverts = profile.getAdverts();
        if (adverts.isEmpty()) {
            log.warn("No added adverts while getting all adverts by profile");
            throw new BusinessException("No added adverts");
        }
        return convertAdvertListToDto(adverts);
    }

    @Transactional
    @Override
    public List<AdvertDto> getActiveAdvertsByProfile(Integer profileId) {
        Profile profile = profileDao.findById(profileId);
        if (profile == null) {
            log.warn("There is no such profile while getting active adverts by profile");
            throw new BusinessException("No such profile");
        }
        List<Advert> adverts = profile.getAdverts();
        if (adverts.isEmpty()) {
            log.warn("No added adverts while getting active adverts by profile");
            throw new BusinessException("No added adverts");
        }
        adverts.removeIf(advert -> !advert.getActive());
        return convertAdvertListToDto(adverts);
    }

    @Transactional
    @Override
    public List<AdvertDto> getClosedAdvertsByProfile(Integer profileId) {
        Profile profile = profileDao.findById(profileId);
        if (profile == null) {
            log.warn("There is no such profile while getting closed adverts by profile");
            throw new BusinessException("No such profile");
        }
        List<Advert> adverts = profile.getAdverts();
        if (adverts.isEmpty()) {
            log.warn("No added adverts while getting closed adverts by profile");
            throw new BusinessException("No added adverts");
        }
        adverts.removeIf(Advert::getActive);
        return convertAdvertListToDto(adverts);
    }

    private List<AdvertDto> convertAdvertListToDto(List<Advert> adverts) {
        List<AdvertDto> advertsDto = new ArrayList<>();
        for (Advert advert : adverts) {
            advertsDto.add(ModelDtoMapper.convertToAdvertDto(advert));
        }
        return advertsDto;
    }

    private List<ProfileDto> convertProfileListToDto(List<Profile> profiles) {
        List<ProfileDto> profilesDto = new ArrayList<>();
        for (Profile profile : profiles) {
            profilesDto.add(ModelDtoMapper.convertToProfileDto(profile));
        }
        return profilesDto;
    }
}
