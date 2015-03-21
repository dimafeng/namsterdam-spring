package com.github.dimafeng.namsterdam.dao;

import com.github.dimafeng.namsterdam.model.ViewCount;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ViewCountRepository extends MongoRepository<ViewCount, String>, ViewCountRepositoryCustom {
    
}
