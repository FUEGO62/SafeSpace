package com.bytebuilder.data.repository;

import com.bytebuilder.data.model.User;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface UserRepository extends MongoRepository<User, String> {
    User findByName(String name);
    boolean existsByName(String name);
}
