package net.vesseldoc.server.service;

import net.vesseldoc.server.model.DAOUser;
import net.vesseldoc.server.model.UserDTO;
import net.vesseldoc.server.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class JwtUserDetailsService implements UserDetailsService {
	
	@Autowired
	private UserRepository userRepository;

	@Autowired
	private PasswordEncoder bcryptEncoder;

	/**
	 * Loads user by username
	 *
	 * @return details of user.
	 */
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		DAOUser user = userRepository.findByUsername(username);
		if (user == null) {
			throw new UsernameNotFoundException("User not found with username: " + username);
		}
		return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(),
				new ArrayList<>());
	}

	/**
	 * Saves the user
	 * @param user user
	 * @return user saved
	 */
	public DAOUser save(UserDTO user) {
		DAOUser newUser = new DAOUser();
		newUser.setUsername(user.getUsername());
		newUser.setPassword(bcryptEncoder.encode(user.getPassword()));
		//newUser.setDisplayName(user.getDisplayName());

		return userRepository.save(newUser);
	}
}