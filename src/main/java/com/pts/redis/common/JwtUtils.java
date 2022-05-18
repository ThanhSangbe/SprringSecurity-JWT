package com.pts.redis.common;

import java.util.Date;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import com.pts.redis.Service.UserDetailsImpl;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.SignatureException;



@Component
public class JwtUtils {
	
	@Value("${bezkoder.app.jwtSecret}")
	private String jwtSecret;
	@Value("${bezkoder.app.jwtExpirationMs}")
	private int jwtExpirationMs;
	
	public String generateJwtToken(Authentication authentication)
	{
			UserDetailsImpl userPrincipal = (UserDetailsImpl) authentication.getPrincipal();
			return Jwts.builder()
					.setSubject(userPrincipal.getUsername())
					.setIssuedAt(new Date())
					.setExpiration(new Date(new Date().getTime() + jwtExpirationMs))
					.signWith(SignatureAlgorithm.HS256, jwtSecret)
					.compact();
	}
	public String getUserNameFromTokent(String token)
	{
		return Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token).getBody().getSubject();
	}
	public boolean validateJwtToken(String authToken)
	{
		try {
			Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(authToken);
			return true;
			}
		catch (Exception e) {
			
		}
		return false;
		
	}
}
