package by.dzmitry.controller.advert;

import by.dzmitry.service.advert.AdvertService;
import by.dzmitry.service.dto.AdvertDto;
import by.dzmitry.service.dto.CommentDto;
import by.dzmitry.service.dto.ProfileDto;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/adverts")
@Api(tags = "Adverts")
public class AdvertController {

    @Autowired
    private AdvertService advertService;

    @PostMapping
    @ApiOperation("Used to add adverts.")
    public void addAdvert(@RequestParam String heading, @RequestParam String publicationDate,
                          @RequestParam Integer price, @RequestParam String description,
                          @RequestParam Integer categoryId, @RequestParam Integer profileId) {
        advertService.addAdvert(heading, publicationDate, price, description, categoryId, profileId);
    }

    @DeleteMapping("/{id}")
    @ApiOperation("Used to delete adverts.")
    public void deleteAdvert(@PathVariable Integer id) {
        advertService.deleteAdvert(id);
    }

    @PutMapping("/{id}")
    @ApiOperation("Used to modify adverts.")
    public void modifyAdvert(@PathVariable Integer id, @RequestParam String heading,
                             @RequestParam Integer price, @RequestParam String description) {
        advertService.modifyAdvert(id, heading, price, description);
    }

    @PutMapping("/pay/{id}")
    @ApiOperation("Used to pay adverts for being in top.")
    public void payAdvert(@PathVariable Integer id, @RequestParam String endPaidDate) {
        advertService.payAdvert(id, endPaidDate);
    }

    @PutMapping("/close/{id}")
    @ApiOperation("Used to close adverts.")
    public void closeAdvert(@PathVariable Integer id) {
        advertService.closeAdvert(id);
    }

    @GetMapping("/{id}")
    @ApiOperation("Used to get one advert.")
    public ResponseEntity<AdvertDto> getAdvertById(@PathVariable Integer id) {
        return ResponseEntity.ok(advertService.getAdvertById(id));
    }

    @GetMapping
    @ApiOperation("Used to get all adverts.")
    public ResponseEntity<List<AdvertDto>> getAllAdverts(@RequestParam(required = false) String filter) {
        List<AdvertDto> adverts;
        if ("active".equals(filter)) {
            adverts = advertService.getActiveAdverts();
        } else if ("closed".equals(filter)) {
            adverts = advertService.getClosedAdverts();
        } else if ("paid".equals(filter)) {
            adverts = advertService.getPaidAdverts();
        } else {
            adverts = advertService.getAllAdverts();
        }
        return ResponseEntity.ok(adverts);
    }

    @GetMapping("/{id}/profile")
    @ApiOperation("Used to get profile by advert.")
    public ResponseEntity<ProfileDto> getProfileByAdvert(@PathVariable Integer id) {
        return ResponseEntity.ok(advertService.getProfileByAdvert(id));
    }

    @GetMapping("/{id}/comments")
    @ApiOperation("Used to get comments by advert.")
    public ResponseEntity<List<CommentDto>> getCommentsByAdvert(@PathVariable Integer id) {
        return ResponseEntity.ok(advertService.getCommentsByAdvert(id));
    }
}
