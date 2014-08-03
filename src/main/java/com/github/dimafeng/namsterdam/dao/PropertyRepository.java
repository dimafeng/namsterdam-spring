package com.github.dimafeng.namsterdam.dao;

import com.github.dimafeng.namsterdam.model.Menu;
import com.github.dimafeng.namsterdam.model.Property;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface PropertyRepository extends MongoRepository<Property, String> {
}
