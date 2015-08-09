package com.github.dimafeng.namsterdam.model;


import com.google.common.base.Strings;
import org.codehaus.jackson.annotate.JsonIgnore;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;
import java.util.List;

@Document(collection = "article")
public abstract class AbstractPost implements Model {
    @Id
    private String id;
    private String title;
    private String bodyHTML;
    private Date displayDate;
    private Date updateDate;
    private Date creationDate;
    @Indexed
    private String urlTitle;
    private boolean display;
    @DBRef
    private User user;

    @Indexed
    @DBRef
    private List<Category> categoryList;

    @Indexed
    private List<String> tags;

    @Override
    public String getId() {
        return id;
    }

    @Override
    public void setId(String id) {
        this.id = id;
    }

    public String getBodyHTML() {
        return bodyHTML;
    }

    public void setBodyHTML(String bodyHTML) {
        this.bodyHTML = bodyHTML;
    }

    public Date getDisplayDate() {
        return displayDate;
    }

    public void setDisplayDate(Date displayDate) {
        this.displayDate = displayDate;
    }

    public Date getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public String getUrlTitle() {
        return urlTitle;
    }

    public void setUrlTitle(String urlTitle) {
        this.urlTitle = urlTitle;
    }

    public boolean isDisplay() {
        return display;
    }

    public void setDisplay(boolean display) {
        this.display = display;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public List<Category> getCategoryList() {
        return categoryList;
    }

    public void setCategoryList(List<Category> categoryList) {
        this.categoryList = categoryList;
    }

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }
    
    @JsonIgnore
    public abstract String getGridImageUrl();

    @JsonIgnore
    public abstract boolean isHasGridImage();

    @JsonIgnore
    public boolean isVideo()
    {
        return this instanceof VideoPost;
    }

    @JsonIgnore
    public boolean isShouldHaveBackgroudOnGrid() {
        return false;
    }
}
