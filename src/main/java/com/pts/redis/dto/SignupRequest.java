package com.pts.redis.dto;

import java.util.Set;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@AllArgsConstructor
@NoArgsConstructor
@Data
public class SignupRequest {
	private String username;
	private String email;
	private String password;
	private Set<String> role;
}
