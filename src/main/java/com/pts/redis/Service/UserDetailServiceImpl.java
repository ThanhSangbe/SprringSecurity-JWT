package com.pts.redis.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.pts.redis.Entity.User;
import com.pts.redis.repository.UserRepository;
@Service
public class UserDetailServiceImpl implements UserDetailsService {
	@Autowired
	UserRepository repository;
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		User user = repository.findByUsername(username).orElseThrow(()-> new UsernameNotFoundException("Username not found in my system"));
		
		return UserDetailsImpl.build(user);
	}

}
