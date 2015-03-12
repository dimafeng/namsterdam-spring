package com.github.dimafeng.namsterdam.dao;

import com.github.dimafeng.namsterdam.model.Image;
import com.github.dimafeng.namsterdam.model.Menu;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface ImageRepository extends MongoRepository<Image, String> {
    List<Image> findByArticleId(String articleId);
}
