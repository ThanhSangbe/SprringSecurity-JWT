package com.pts.redis.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.pts.redis.Entity.Roles;
import com.pts.redis.common.ERole;

public interface RoleRepository extends JpaRepository<Roles, Integer> {
	Optional<Roles> findByName(ERole name);
}
