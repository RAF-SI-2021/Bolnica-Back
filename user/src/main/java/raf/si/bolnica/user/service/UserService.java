package raf.si.bolnica.user.service;

import org.springframework.data.jpa.domain.Specification;
import raf.si.bolnica.user.models.User;
import raf.si.bolnica.user.query.UserSpecification;

import java.util.List;

public interface UserService {

    User fetchUserByEmail(String email);

    User fetchUserByUsername(String username);

    User fetchUserByLBZ(Long lbz);

    User createEmployee(User user);

    String generateNewPassword(User user);

    void savePassword(User user, String password);

    void deleteById(Long id);

    List<User> filterUsers(Specification userSpecification);
}
