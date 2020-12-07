package by.dzmitry.service.user;

import by.dzmitry.dao.user.UserDao;
import by.dzmitry.model.user.Role;
import by.dzmitry.model.user.User;
import by.dzmitry.service.exception.BusinessException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service(value = "userService")
public class UserServiceImpl implements UserDetailsService {

    @Autowired
    private UserDao userDao;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public void save(User user) {
        if (userDao.findByUsername(user.getUsername()) != null) {
            throw new BusinessException("This username is already exist");
        }
        user.setRole(Role.ROLE_USER);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userDao.save(user);
    }

    public void saveAdmin(User user) {
        if (userDao.findByUsername(user.getUsername()) != null) {
            throw new BusinessException("This username is already exist");
        }
        user.setRole(Role.ROLE_ADMIN);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userDao.save(user);
    }

    @Override
    public UserDetails loadUserByUsername(String name) throws UsernameNotFoundException {
        User user = userDao.findByUsername(name);
        if (user == null) {
            throw new UsernameNotFoundException("User not found");
        }
        return new UserDetailsEntity().toUserDetails(userDao.findByUsername(name));
    }
}
