package raf.si.bolnica.user.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import raf.si.bolnica.user.constants.Constants;
import raf.si.bolnica.user.exceptions.UnauthorisedException;
import raf.si.bolnica.user.jwt.JwtTokenProvider;
import raf.si.bolnica.user.models.User;
import raf.si.bolnica.user.repositories.UserRepository;
import raf.si.bolnica.user.requests.LoginRequestDTO;

import javax.servlet.http.HttpServletRequest;
import java.util.Objects;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;

@Service
@Transactional("transactionManager")
public class LoginServiceImpl implements LoginService {

    private static final Logger LOGGER = LoggerFactory.getLogger(LoginServiceImpl.class);

    @Autowired
    JwtTokenProvider jwtTokenProvider;

    @Autowired
    UserRepository userRepository;

    @Override
    public String login(LoginRequestDTO requestDTO, HttpServletRequest request) {
        User user = fetchUserDetails.apply(requestDTO);
        validateUserUsername.accept(user);
        validatePassword.accept(requestDTO, user);

        return jwtTokenProvider.createToken(requestDTO.getEmail(), user);
    }

    private final Function<LoginRequestDTO, User> fetchUserDetails = (loginRequestDTO) -> userRepository.findByEmail(loginRequestDTO.getEmail());

    private final Consumer<User> validateUserUsername = (admin) -> {
        if (Objects.isNull(admin))
            throw new UnauthorisedException(Constants.InvalidAdminUsername.MESSAGE, Constants.InvalidAdminUsername.DEVELOPER_MESSAGE);
    };

    private final BiConsumer<LoginRequestDTO, User> validatePassword = (requestDTO, admin) -> {
        if (!BCrypt.checkpw(requestDTO.getPassword(), admin.getPassword())) {
            throw new UnauthorisedException(Constants.ForgetPassword.MESSAGE, Constants.ForgetPassword.DEVELOPER_MESSAGE);
        }
    };

}
