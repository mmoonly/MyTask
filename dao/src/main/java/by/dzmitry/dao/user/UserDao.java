package by.dzmitry.dao.user;

import by.dzmitry.model.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository(value = "userDao")
public interface UserDao extends JpaRepository<User, Integer> {

    User findByUsername(String name);
}
