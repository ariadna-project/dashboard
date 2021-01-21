package com.ud.iot.spring.repositories;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.ud.iot.documents.DashboardUser;

@Repository
public interface UserRepository extends MongoRepository<DashboardUser, Long>{
	Optional<DashboardUser> findByName(String name);
}