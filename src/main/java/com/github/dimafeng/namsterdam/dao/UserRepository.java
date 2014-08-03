package com.github.dimafeng.namsterdam.dao;

import com.github.dimafeng.namsterdam.model.Menu;
import com.github.dimafeng.namsterdam.model.Property;
import com.github.dimafeng.namsterdam.model.User;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface UserRepository extends MongoRepository<User, String> {
    User findByEmail(String email);
}
