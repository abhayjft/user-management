package usermanagement.service;

import java.time.LocalDateTime;
import java.util.List;

import usermanagement.model.User;

public interface UserService {
    List <User> getAllUser();
    void saveUser(User user);
    User getUserById(long id);
    void deleteUserById(long id);
    User getUserByEmail(String email);
    String forgotPassword(String email);
    String resetPassword(String token, String password);
    String generateToken();
    boolean isTokenExpired(final LocalDateTime tokenCreationDate);
	User getUserByTocken(String token);
}
