package com.github.dimafeng.namsterdam.dao;

import com.github.dimafeng.namsterdam.model.Article;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ArticleRepository extends MongoRepository<Article, String>, ArticleRepositoryCustom {

    Article findByUrlTitle(String urlTitle);

    @Query(value = "{'display' : ?0 }")
    Page<Article> findAllByDisplay(boolean display, Pageable pageSpecification);

//    @Query(value = "{'categories' : ?0, 'display' : 'true' }")
//    Page<Article> findByCategory(String category, Pageable pageSpecification);
}
