package com.github.dimafeng.namsterdam.dao;

import com.github.dimafeng.namsterdam.model.AbstractPost;
import com.github.dimafeng.namsterdam.model.Article;
import org.bson.types.ObjectId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AbstractPostRepository extends MongoRepository<AbstractPost, String> {

    AbstractPost findByUrlTitle(String urlTitle);

    @Query(value = "{'display' : ?0 }")
    Page<AbstractPost> findAllByDisplay(boolean display, Pageable pageSpecification);

    Long countByDisplay(boolean display);

    @Query(value = "{$and: [{ categoryList: { $elemMatch: { $id: ?0 } } }, {'display' : ?1}]}")
    Page<AbstractPost> findByCategory(ObjectId categoryId, boolean display, Pageable pageSpecification);

    @Query(value = "{ categoryList: { $elemMatch: { $id: ?0 } } }", count = true)
    Long countByCategory(ObjectId categoryId);

    @Query(value = "{'_class' : ?0 }")
    List<AbstractPost> findByClass(String cls);
}
