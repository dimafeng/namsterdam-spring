package com.github.dimafeng.namsterdam.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
public class ViewCount {
    @Id
    private String id;
    @Indexed
    private String articleId;
    private long count;

    public ViewCount(String articleId, long count) {
        this();
        this.articleId = articleId;
        this.count = count;
    }

    public ViewCount() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getArticleId() {
        return articleId;
    }

    public void setArticleId(String articleId) {
        this.articleId = articleId;
    }

    public long getCount() {
        return count;
    }

    public void setCount(long count) {
        this.count = count;
    }
}
