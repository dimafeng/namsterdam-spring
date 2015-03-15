package com.github.dimafeng.namsterdam.model;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
public class Image {
    @Id
    private String id;

    @JsonIgnore
    private byte[] data;

    @JsonIgnore
    private byte[] dataJPG;

    @Indexed
    private String articleId;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public byte[] getData() {
        return data;
    }

    public void setData(byte[] data) {
        this.data = data;
    }

    public byte[] getDataJPG() {
        return dataJPG;
    }

    public void setDataJPG(byte[] dataJPG) {
        this.dataJPG = dataJPG;
    }

    public String getArticleId() {
        return articleId;
    }

    public void setArticleId(String articleId) {
        this.articleId = articleId;
    }
}
