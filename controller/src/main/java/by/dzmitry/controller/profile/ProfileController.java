package by.dzmitry.controller.profile;

import by.dzmitry.service.dto.AdvertDto;
import by.dzmitry.service.dto.ProfileDto;
import by.dzmitry.service.profile.ProfileService;
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
@RequestMapping("/profiles")
@Api(tags = "Profiles")
public class ProfileController {

    @Autowired
    private ProfileService profileService;

    @PostMapping
    @ApiOperation("Used to add profiles.")
    public void addProfile(@RequestParam String name, @RequestParam String surname) {
        profileService.addProfile(name, surname);
    }

    @DeleteMapping("/{id}")
    @ApiOperation("Used to delete profiles.")
    public void deleteProfile(@PathVariable Integer id) {
        profileService.deleteProfile(id);
    }

    @PutMapping("/{id}")
    @ApiOperation("Used to modify profiles.")
    public void modifyAdvert(@PathVariable Integer id, @RequestParam String name,
                             @RequestParam String surname) {
        profileService.modifyProfile(id, name, surname);
    }

    @GetMapping
    @ApiOperation("Used to get all profiles.")
    public ResponseEntity<List<ProfileDto>> getAllProfiles() {
        return ResponseEntity.ok(profileService.getAllProfiles());
    }

    @GetMapping("/{id}/adverts")
    @ApiOperation("Used to get all adverts.")
    public ResponseEntity<List<AdvertDto>> getAdvertsByProfile(@PathVariable Integer id, @RequestParam(required = false) String filter) {
        List<AdvertDto> adverts;
        if ("active".equals(filter)) {
            adverts = profileService.getActiveAdvertsByProfile(id);
        } else if ("closed".equals(filter)) {
            adverts = profileService.getClosedAdvertsByProfile(id);
        } else {
            adverts = profileService.getAllAdvertsByProfile(id);
        }
        return ResponseEntity.ok(adverts);
    }
}
