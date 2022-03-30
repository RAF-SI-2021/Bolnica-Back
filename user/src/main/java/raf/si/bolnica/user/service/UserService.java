package raf.si.bolnica.user.service;

import raf.si.bolnica.user.models.User;

public interface UserService {

    User fetchUserByEmail(String email);
    String generateNewPassword(User user);
    void savePassword(User user, String password);
}
