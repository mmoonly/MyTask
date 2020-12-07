package by.dzmitry.service.profile;

import by.dzmitry.service.dto.AdvertDto;
import by.dzmitry.service.dto.ProfileDto;

import java.util.List;

public interface ProfileService {

    void addProfile(String name, String surname);

    void modifyProfile(Integer profileId, String name, String surname);

    void deleteProfile(Integer profileId);

    List<ProfileDto> getAllProfiles();

    List<AdvertDto> getAllAdvertsByProfile(Integer profileId);

    List<AdvertDto> getActiveAdvertsByProfile(Integer profileId);

    List<AdvertDto> getClosedAdvertsByProfile(Integer profileId);
}

