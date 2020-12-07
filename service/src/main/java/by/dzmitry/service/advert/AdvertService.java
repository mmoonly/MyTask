package by.dzmitry.service.advert;

import by.dzmitry.service.dto.AdvertDto;
import by.dzmitry.service.dto.CommentDto;
import by.dzmitry.service.dto.ProfileDto;

import java.util.List;

public interface AdvertService {

    void addAdvert(String heading, String publicationDate, Integer price, String description, Integer categoryId, Integer profileId);

    void deleteAdvert(Integer advertId);

    void modifyAdvert(Integer advertId, String heading, Integer price, String description);

    void payAdvert(Integer advertId, String endPaidDate);

    void closeAdvert(Integer advertId);

    AdvertDto getAdvertById(Integer advertId);

    List<AdvertDto> getAllAdverts();

    List<AdvertDto> getActiveAdverts();

    List<AdvertDto> getClosedAdverts();

    List<AdvertDto> getPaidAdverts();

    ProfileDto getProfileByAdvert(Integer advertId);

    List<CommentDto> getCommentsByAdvert(Integer advertId);
}
