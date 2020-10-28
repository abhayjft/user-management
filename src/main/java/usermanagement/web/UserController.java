package usermanagement.web;

import java.security.Principal;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import usermanagement.model.Role;
import usermanagement.model.User;
import usermanagement.service.RoleService;
import usermanagement.service.UserService;


@Controller
public class UserController {

	@Autowired
	private UserService userService;
	
	@Autowired
	private RoleService roleService;
	
	@Autowired
	private BCryptPasswordEncoder passwordEncoder;
	
	@GetMapping("/users")
	public String users(Model model) {
		List<User> users = userService.getAllUser();
		model.addAttribute("users", users);
		return "user/users";
	}
	
	@GetMapping("/registration")
	public String registration(Model model) {
		User user = new User();
		model.addAttribute("user",user);
		return "/registration";
	}
	@PostMapping("/registration")
	public String register(@ModelAttribute("user") User user) {
		Role role = roleService.getRoleById(2);
		User usr = new User(user.getEmail(), passwordEncoder.encode(user.getPassword()), Arrays.asList(role));
		userService.saveUser(usr);
		return "redirect:/login";
	}
	
	
	@GetMapping("/users/new")
	public String newUser(Model model) {
		User user = new User();
		model.addAttribute("user",user);
		return "user/new_user";
	}
	
	@PostMapping("/users/new")
	public String saveUser(@ModelAttribute("user") User user) {
		Role role = roleService.getRoleById(2);
		User usr = new User(user.getEmail(), passwordEncoder.encode(user.getPassword()), Arrays.asList(role));
		userService.saveUser(usr);
		return "redirect:/users";
	}
	
	@GetMapping("/users/{id}/assignRoles")
	public String assignRoles(@PathVariable(value="id") long id, Model model) {
		User user = userService.getUserById(id);
		model.addAttribute("user",user);
		model.addAttribute("roleCollections", roleService.getAllRole());
		return "user/assign_roles";
	}

	/* @PreAuthorize("hasRole('ROLE_ADMIN')") */
	@PostMapping("/users/{id}/assignRoles")
	public String saveAssignRoles(@ModelAttribute("user") User user) {
		User usr = userService.getUserById(user.getId());
		//user.setPassword(passwordEncoder.encode(user.getPassword()));
		usr.setRoles(user.getRoles());
		userService.saveUser(usr);
		return "redirect:/users";
	}
	
	@GetMapping("/users/{id}/update")
	public String updateQuiz(@PathVariable(value="id") long id, Model model) {
		User user = userService.getUserById(id);
		model.addAttribute("user", user);
		model.addAttribute("roleCollections", roleService.getAllRole());
		return "/user/update_user";
	}
	
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@PostMapping("/users/{id}/update")
	public String saveUpdateQuiz(@ModelAttribute("user") User user) {
		User usr = userService.getUserById(user.getId());
		usr.setEmail(user.getEmail());
		userService.saveUser(usr);
		return "redirect:/users";
	}
	
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@GetMapping("users/{id}/delete")
	public String deleteUser(@PathVariable(value="id") long id) {
		User user = userService.getUserById(id);
		Collection <Role> roles = null;
		user.setRoles(roles);
		this.userService.deleteUserById(id);
		return "redirect:/users";
	}
	
	@GetMapping("/forgot_password")
	public String forgotPasswordForm() {
		return "/forgot_password";
	}

	//https://www.websparrow.org/spring/spring-boot-forgot-password-example
	@PostMapping("/forgot_password")
	public String forgotPassword(@RequestParam String email, final RedirectAttributes ra ) {
		
		String response = userService.forgotPassword(email);
		
		if (!response.startsWith("Invalid")) {
			response = "Success " + response;
			ra.addFlashAttribute("success", "Password reset email sent, check our email! ");
			return "redirect:/forgot_password";
		}
		ra.addFlashAttribute("failure", "No account associated with this email! ");
		
		//return "/forgot_password";
		return "redirect:/forgot_password";	
	}
	
	@GetMapping("/reset-password")
	public String resetPasswordForm(@RequestParam String token, Model model) {
		model.addAttribute("token",token);
		return "/reset_password";
	}
	
	@PostMapping("/reset-password")
	public String resetPassword(@RequestParam String token, @RequestParam String password) {
		 userService.resetPassword(token, password);
		 return "/login";
	}
	
	@GetMapping("/users/{id}/view")
	public String user(@PathVariable(value="id") long id, Model model) {
		User user = userService.getUserById(id);
		model.addAttribute("user",user);
		return "user/user";
	}
	
}
