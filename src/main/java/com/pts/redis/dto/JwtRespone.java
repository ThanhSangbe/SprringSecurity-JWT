package com.pts.redis.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@AllArgsConstructor
@NoArgsConstructor
@Data
public class JwtRespone {
	private String token;
	private String type="Bearer";
	private Long id;
	private String username;
	private String email;
	private List<String> roles;
	public JwtRespone(String accessToken, Long id, String username, String email, List<String> roles)
	{
		this.token = accessToken;
		this.id=id;
		this.username = username;
		this.email = email;
		this.roles = roles;
	}
}
