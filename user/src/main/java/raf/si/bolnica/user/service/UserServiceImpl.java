package raf.si.bolnica.user.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import raf.si.bolnica.user.models.User;
import raf.si.bolnica.user.repositories.UserRepository;

@Service
@Transactional("transactionManager")
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public User fetchUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public User createEmployee(User user) {
        return userRepository.save(user);
    }
}
