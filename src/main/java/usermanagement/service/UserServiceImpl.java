package usermanagement.service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import usermanagement.model.Mail;
import usermanagement.model.User;
import usermanagement.repository.UserRepository;


@Service
public class UserServiceImpl implements UserService{
	
	private static final long EXPIRE_TOKEN_AFTER_MINUTES = 30;

	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private MailService mailService;
	
	@Autowired
	private BCryptPasswordEncoder passwordEncoder;
	
	@Override
	public List<User> getAllUser() {
		return (List<User>) userRepository.findAllUsers();
	}

	@Override
	public void saveUser(User user) {
		this.userRepository.save(user);
		
	}

	@Override
	public User getUserById(long id) {
		Optional <User> optional = userRepository.findById(id);
		User user = null;
		if(optional.isPresent()) {
			user = optional.get();
		} else {
			throw new RuntimeException(" User not found for id ::" + id);
		}
		return user;
	}

	@Override
	public void deleteUserById(long id) {
		this.userRepository.deleteById(id);
	}

	@Override
	public User getUserByEmail(String email) {
		return this.userRepository.findByEmail(email);
	}

	@Override
	public User getUserByTocken(String token) {
		return this.userRepository.findByToken(token);
	}
	
	@Override
	public String forgotPassword(String email) {
		Optional<User> userOptional = Optional
				.ofNullable(userRepository.findByEmail(email));

		if (!userOptional.isPresent()) {
			return "Invalid email id.";
		}

		User user = userOptional.get();
		user.setToken(generateToken());
		user.setTokenCreationDate(LocalDateTime.now());


		user = userRepository.save(user);
		
		String content = "Hi! " + user.getEmail() + " Click on the link to reset your password " + "http://localhost:8081/reset-password?token=" + user.getToken();
		
        Mail mail = new Mail();
        mail.setMailFrom("aktabhay81@gmail.com");
        mail.setMailTo(email);
        mail.setMailSubject(" Portal - Reset you password!  ");
        mail.setMailContent(content);
        mailService.sendEmail(mail);

		return "Mail sent!";
	}

	@Override
	public String resetPassword(String token, String password) {

		Optional<User> userOptional = Optional
				.ofNullable(userRepository.findByToken(token));

		if (!userOptional.isPresent()) {
			return "Invalid token.";
		}

		LocalDateTime tokenCreationDate = userOptional.get().getTokenCreationDate();

		if (isTokenExpired(tokenCreationDate)) {
			return "Token expired.";

		}
//http://localhost:8081/reset-password?token=a2a869ca-6
		User user = userOptional.get();

		user.setPassword(passwordEncoder.encode(password));
		user.setToken(null);
		user.setTokenCreationDate(null);


		userRepository.save(user);

		return "Your password successfully updated.";
	}

	@Override
	public String generateToken() {
		StringBuilder token = new StringBuilder();

		return token.append(UUID.randomUUID().toString())
				.append(UUID.randomUUID().toString()).toString();
	}
	
	@Override
	public boolean isTokenExpired(LocalDateTime tokenCreationDate) {
		LocalDateTime now = LocalDateTime.now();
		Duration diff = Duration.between(tokenCreationDate, now);

		return diff.toMinutes() >= EXPIRE_TOKEN_AFTER_MINUTES;
	}


}
