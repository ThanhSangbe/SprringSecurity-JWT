package com.pts.redis.Controller;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.pts.redis.Entity.Roles;
import com.pts.redis.Entity.User;
import com.pts.redis.Service.UserDetailsImpl;
import com.pts.redis.common.ERole;
import com.pts.redis.common.JwtUtils;
import com.pts.redis.dto.JwtRespone;
import com.pts.redis.dto.LoginRequest;
import com.pts.redis.dto.MessageRespone;
import com.pts.redis.dto.SignupRequest;
import com.pts.redis.repository.RoleRepository;
import com.pts.redis.repository.UserRepository;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
	@Autowired
	AuthenticationManager authenticationManager;
	
	@Autowired 
	UserRepository userRepository;
	
	@Autowired
	PasswordEncoder passwordEncoder;
	@Autowired
	JwtUtils jwtUtils;
	@Autowired
	RoleRepository roleRepository;
	
	@PostMapping("/signin")
	public ResponseEntity<?> authenticateUser( @Validated @RequestBody LoginRequest loginRequest)
	{
		Authentication authentication = authenticationManager.authenticate(
				new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword())
				);
		SecurityContextHolder.getContext().setAuthentication(authentication);
		String jwt = this.jwtUtils.generateJwtToken(authentication);
		UserDetailsImpl userDetail = (UserDetailsImpl) authentication.getPrincipal();
		List<String> roles = userDetail.getAuthorities().stream().map(item -> item.getAuthority()).collect(Collectors.toList());
		return ResponseEntity.ok(new JwtRespone(jwt,userDetail.getId(),userDetail.getUsername(),userDetail.getEmail(),roles));
	}
	@PostMapping("/signup")
	public ResponseEntity<?> registerUser(@Validated @RequestBody SignupRequest signupRequest)
	{
		if(userRepository.existsByUsername(signupRequest.getUsername()))
		{
			return ResponseEntity.badRequest().body(new MessageRespone("User name is already token"));
		}
		User user = new User();
		user.setUsername(signupRequest.getUsername());
		user.setEmail(signupRequest.getEmail());
		user.setPassword(this.passwordEncoder.encode(signupRequest.getPassword()));
		Set<String> strRole = signupRequest.getRole();
		Set<Roles> roles = new HashSet<Roles>();
		if(strRole == null)
		{
			Roles r = roleRepository.findByName(ERole.ROLE_USER).orElseThrow(()-> new RuntimeException("Role is not found"));
			roles.add(r);
		}
		else 
		{
			strRole.forEach(role ->
			{
				switch (role) {
				case "admin":
					Roles rolex = roleRepository.findByName(ERole.ROLE_ADMIN).get();
					roles.add(rolex);
					break;
				case "mod":
					Roles roled = roleRepository.findByName(ERole.ROLE_MODERATOR).get();
					roles.add(roled);
					break;	
				default:Roles r = roleRepository.findByName(ERole.ROLE_USER).get();
				roles.add(r);
					break;
				}
			
			});
		}
		user.setRoles(roles);
		this.userRepository.save(user);
		
		return ResponseEntity.ok(new MessageRespone("User register successfully"));
		
	
}
}
