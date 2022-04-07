package raf.si.bolnica.user.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import raf.si.bolnica.user.models.User;
import raf.si.bolnica.user.repositories.UserRepository;
import java.security.SecureRandom;
import java.util.List;

@Service
@Transactional("transactionManager")
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public User fetchUserByEmail(String email) { return userRepository.findByEmail(email); }

    @Override
    public User fetchUserByUsername(String username) { return userRepository.findByKorisnickoIme(username); }

    @Override
    public User fetchUserByLBZ(Long lbz) { return userRepository.findByLicniBrojZaposlenog(lbz); }

    @Override
    public User saveEmployee(User user) {
        return userRepository.save(user);
    }

    public String generateNewPassword(User user) {
        //generating new random 8 char password from alphanumerical
        final String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";

        SecureRandom random = new SecureRandom();
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < 8; i++)
        {
            int randomIndex = random.nextInt(chars.length());
            sb.append(chars.charAt(randomIndex));
        }
        return sb.toString();
    }

    @Override
    public void savePassword(User user, String password) {
        // Encrypting password before saving it in database
        user.setPassword(BCrypt.hashpw(password, BCrypt.gensalt()));
        userRepository.save(user);
    }

    @Override
    public void deleteById(Long id) {
        userRepository.deleteById(id);
    }

}
