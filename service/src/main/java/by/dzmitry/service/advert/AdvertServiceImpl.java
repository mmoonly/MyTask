package by.dzmitry.service.advert;

import by.dzmitry.dao.advert.AdvertDao;
import by.dzmitry.dao.category.CategoryDao;
import by.dzmitry.dao.profile.ProfileDao;
import by.dzmitry.model.advert.Advert;
import by.dzmitry.model.category.Category;
import by.dzmitry.model.comment.Comment;
import by.dzmitry.model.profile.Profile;
import by.dzmitry.service.dto.AdvertDto;
import by.dzmitry.service.dto.CommentDto;
import by.dzmitry.service.dto.ProfileDto;
import by.dzmitry.service.exception.BusinessException;
import by.dzmitry.service.util.ModelDtoMapper;
import lombok.extern.log4j.Log4j2;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Log4j2
public class AdvertServiceImpl implements AdvertService {

    @Autowired
    private AdvertDao advertDao;

    @Autowired
    private CategoryDao categoryDao;

    @Autowired
    private ProfileDao profileDao;

    @Transactional
    @Override
    public void addAdvert(String heading, String publicationDate, Integer price,
                          String description, Integer categoryId, Integer profileId) {
        Category category = categoryDao.findById(categoryId);
        if (category == null) {
            log.warn("There is no such category while adding");
            throw new BusinessException("There is no such category");
        }
        Profile profile = profileDao.findById(profileId);
        if (profile == null) {
            log.warn("There is no such profile while adding");
            throw new BusinessException("There is no such profile");
        }
        Advert advert = new Advert(heading, LocalDate.parse(publicationDate), price, description, category, profile);
        profile.setRating(profile.getRating() + 1);
        profileDao.update(profile);
        advertDao.create(advert);
    }

    @Transactional
    @Override
    public void deleteAdvert(Integer advertId) {
        Advert advert = advertDao.findById(advertId);
        if (advert == null) {
            log.warn("There is no such advert while deleting");
            throw new BusinessException("There is no such advert");
        }
        if (advert.getActive()) {
            log.warn("Advert is active while deleting");
            throw new BusinessException("This advert is active");
        }
        advertDao.deleteById(advertId);
    }

    @Transactional
    @Override
    public void modifyAdvert(Integer advertId, String heading, Integer price, String description) {
        Advert advert = advertDao.findById(advertId);
        if (advert == null) {
            log.warn("There is no such advert while modifying");
            throw new BusinessException("There is no such advert");
        }
        advert.setHeading(heading);
        advert.setPrice(price);
        advert.setDescription(description);
        advertDao.update(advert);
    }

    @Transactional
    @Override
    public void payAdvert(Integer advertId, String endPaidDate) {
        Advert advert = advertDao.findById(advertId);
        if (advert == null) {
            log.warn("There is no such advert while paying");
            throw new BusinessException("There is no such advert");
        }
        advert.setPaid(true);
        advert.setEndPaidDate(LocalDate.parse(endPaidDate));
        advertDao.update(advert);
    }

    @Transactional
    @Override
    public void closeAdvert(Integer advertId) {
        Advert advert = advertDao.findById(advertId);
        if (advert == null) {
            log.warn("There is no such advert while closing");
            throw new BusinessException("There is no such advert");
        }
        Profile profile = advert.getProfile();
        profile.setRating(profile.getRating() + 3);
        profileDao.update(profile);
        advert.setPaid(false);
        advert.setEndPaidDate(null);
        advert.setActive(false);
        advertDao.update(advert);
    }

    @Transactional
    @Override
    public AdvertDto getAdvertById(Integer advertId) {
        Advert advert = advertDao.findById(advertId);
        if (advert == null) {
            log.warn("There is no such advert while getting advert");
            throw new BusinessException("There is no such advert");
        }
        return ModelDtoMapper.convertToAdvertDto(advert);
    }


    @Transactional
    @Override
    public List<AdvertDto> getAllAdverts() {
        List<Advert> adverts = advertDao.findAll();
        if (adverts.isEmpty()) {
            log.warn("No added adverts while getting all adverts");
            throw new BusinessException("No added adverts");
        }
        return convertAdvertListToDto(adverts.stream()
                .sorted(Comparator.comparing(Advert::getActive).reversed()
                        .thenComparing(Advert::getPaid).reversed()
                        .thenComparing((a1, a2) -> a2.getProfile().getRating().compareTo(a1.getProfile().getRating())))
                .collect(Collectors.toList()));
    }

    @Transactional
    @Override
    public List<AdvertDto> getActiveAdverts() {
        List<Advert> adverts = advertDao.getActiveAdverts();
        if (adverts.isEmpty()) {
            log.warn("No added adverts while getting active adverts");
            throw new BusinessException("No active adverts");
        }
        return convertAdvertListToDto(adverts.stream()
                .sorted(Comparator.comparing(Advert::getActive).reversed()
                        .thenComparing(Advert::getPaid).reversed()
                        .thenComparing((a1, a2) -> a2.getProfile().getRating().compareTo(a1.getProfile().getRating())))
                .collect(Collectors.toList()));
    }

    @Transactional
    @Override
    public List<AdvertDto> getClosedAdverts() {
        List<Advert> adverts = advertDao.getClosedAdverts();
        if (adverts.isEmpty()) {
            log.warn("No added adverts while getting closed adverts");
            throw new BusinessException("No closed adverts");
        }
        return convertAdvertListToDto(adverts.stream()
                .sorted((a1, a2) -> a2.getProfile().getRating().compareTo(a1.getProfile().getRating()))
                .collect(Collectors.toList()));
    }

    @Transactional
    @Override
    public List<AdvertDto> getPaidAdverts() {
        List<Advert> adverts = advertDao.getPaidAdverts();
        if (adverts.isEmpty()) {
            log.warn("No added adverts while getting paid adverts");
            throw new BusinessException("No paid adverts");
        }
        return convertAdvertListToDto(adverts.stream()
                .sorted((a1, a2) -> a2.getProfile().getRating().compareTo(a1.getProfile().getRating()))
                .collect(Collectors.toList()));
    }

    @Transactional
    @Override
    public ProfileDto getProfileByAdvert(Integer advertId) {
        Advert advert = advertDao.findById(advertId);
        if (advert == null) {
            log.warn("There is no such advert while getting profile by advert");
            throw new BusinessException("There is no such advert");
        }
        return ModelDtoMapper.convertToProfileDto(advert.getProfile());
    }

    @Transactional
    @Override
    public List<CommentDto> getCommentsByAdvert(Integer advertId) {
        Advert advert = advertDao.findById(advertId);
        if (advert == null) {
            log.warn("There is no such advert while getting comments by advert");
            throw new BusinessException("There is no such advert");
        }
        List<Comment> comments = advert.getComments();
        if (comments.isEmpty()) {
            log.warn("There is no any comments while getting profile by advert");
            throw new BusinessException("No any comments");
        }
        return convertCommentListToDto(comments);
    }

    @Transactional
    @Scheduled(cron = "${cron.expression}")
    public void checkPaidAdvert() {
        List<Advert> adverts = advertDao.findAll();
        if (!adverts.isEmpty()) {
            for (Advert advert : adverts) {
                if (advert.getPaid()) {
                    if (advert.getEndPaidDate().isBefore(LocalDate.now())) {
                        advert.setPaid(false);
                        advert.setEndPaidDate(null);
                    }
                }
            }
        }
    }

    private List<CommentDto> convertCommentListToDto(List<Comment> comments) {
        List<CommentDto> commentDto = new ArrayList<>();
        for (Comment comment : comments) {
            commentDto.add(ModelDtoMapper.convertToCommentDto(comment));
        }
        return commentDto;
    }

    private List<AdvertDto> convertAdvertListToDto(List<Advert> adverts) {
        List<AdvertDto> advertsDto = new ArrayList<>();
        for (Advert advert : adverts) {
            advertsDto.add(ModelDtoMapper.convertToAdvertDto(advert));
        }
        return advertsDto;
    }

}

