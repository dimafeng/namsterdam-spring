package com.github.dimafeng.namsterdam.dao;

import com.github.dimafeng.namsterdam.model.Article;
import com.github.dimafeng.namsterdam.model.ViewCount;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

public class ViewCountRepositoryImpl implements ViewCountRepositoryCustom {
    @Autowired
    private MongoTemplate mongoTemplate;

    @Override
    public long getAndIncViews(String articleId) {
        Query query = new Query(Criteria.where("articleId").is(articleId));
        Update update = new Update().inc("count", 1);
        ViewCount viewCount = mongoTemplate.findAndModify(query, update, ViewCount.class);
        if(viewCount == null) {
            mongoTemplate.insert(new ViewCount(articleId, 1));
            return 1;
        }
        return viewCount.getCount();
    }
}
