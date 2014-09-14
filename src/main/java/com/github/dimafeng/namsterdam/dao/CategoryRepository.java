package com.github.dimafeng.namsterdam.dao;

import com.github.dimafeng.namsterdam.model.Article;
import com.github.dimafeng.namsterdam.model.Category;
import org.springframework.data.mongodb.repository.MongoRepository;


public interface CategoryRepository extends MongoRepository<Category, String> {
    Category findByName(String name);
}
