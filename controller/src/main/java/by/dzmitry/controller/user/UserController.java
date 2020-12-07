package by.dzmitry.controller.user;

import by.dzmitry.model.user.User;
import by.dzmitry.service.user.UserServiceImpl;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping
@Api(tags = "Users")
public class UserController {

    @Autowired
    private UserServiceImpl userService;

    @PostMapping("/register")
    @ApiOperation("Used to add users.")
    public void registerUser(@RequestParam String login, String password) {
        User user = new User();
        user.setUsername(login);
        user.setPassword(password);
        userService.save(user);
    }

    @PostMapping("/admin")
    @ApiOperation("Used to add admins.")
    public void registerAdmin(@RequestParam String login, String password) {
        User user = new User();
        user.setUsername(login);
        user.setPassword(password);
        userService.saveAdmin(user);
    }
}
