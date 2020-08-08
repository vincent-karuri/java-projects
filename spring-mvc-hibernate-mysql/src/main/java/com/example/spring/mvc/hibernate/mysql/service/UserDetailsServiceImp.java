package com.example.spring.mvc.hibernate.mysql.service;

import com.example.spring.mvc.hibernate.mysql.entity.User;
import com.example.spring.mvc.hibernate.mysql.repository.UserDetailsDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * Created by Vincent Karuri on 08/08/2020
 */

@Service("userDetailsService")
public class UserDetailsServiceImp implements UserDetailsService {

	@Autowired
	private UserDetailsDao userDetailsDao;

	@Override
	public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
		User user = userDetailsDao.findUserByUserName(userName);
		org.springframework.security.core.userdetails.User.UserBuilder userBuilder = null;
		if (user != null) {
			userBuilder = org.springframework.security.core.userdetails.User.withUsername(userName);
			userBuilder.password(user.getPassword());
			userBuilder.disabled(!user.isEnabled());
			String[] authorities = user.getAuthorities()
					.stream().map(a -> a.getAuthority()).toArray(String[]::new);

			userBuilder.authorities(authorities);
		} else {
			throw new UsernameNotFoundException("User not found.");
		}
		return userBuilder.build();
	}
}
