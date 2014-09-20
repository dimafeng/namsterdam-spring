package com.github.dimafeng.namsterdam.dao;


import com.github.dimafeng.namsterdam.model.Article;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

public class ArticleRepositoryImpl implements ArticleRepositoryCustom {

    static final Logger log = LoggerFactory.getLogger(ArticleRepositoryImpl.class);

    @Autowired
    private MongoTemplate mongoTemplate;

    @Override
    public long getAndIncViews(String id) {
        Query query = new Query(Criteria.where("_id").is(id));
        Update update = new Update().inc("views", 1);
        Article article = mongoTemplate.findAndModify(query, update, Article.class);
        return article.getViews() == null ? 0 : article.getViews();
    }
}
