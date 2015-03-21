package com.github.dimafeng.namsterdam.model;

import com.google.common.base.Strings;
import org.codehaus.jackson.annotate.JsonIgnore;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;
import java.util.List;

@Document
public class Article implements Model {
    @Id
    private String id;

    private String title;
    private String body;
    private String bodyHTML;
    private String smallText;
    private Date displayDate;
    private Date updateDate;
    private Date creationDate;
    private boolean display;
    @Indexed
    private String urlTitle;
    private String gridImageId;
    private String mainImageId;
    private int gridImageWidth;
    private int gridImageHeight;

    @Indexed
    @DBRef
    private List<Category> categoryList;

    @Indexed
    private List<String> tags;

    @DBRef
    private User user;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public Date getDisplayDate() {
        return displayDate;
    }

    public void setDisplayDate(Date displayDate) {
        this.displayDate = displayDate;
    }

    public String getBodyHTML() {
        return bodyHTML;
    }

    public void setBodyHTML(String bodyHTML) {
        this.bodyHTML = bodyHTML;
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

    public boolean isDisplay() {
        return display;
    }

    public void setDisplay(boolean display) {
        this.display = display;
    }

    public String getUrlTitle() {
        return urlTitle;
    }

    public void setUrlTitle(String urlTitle) {
        this.urlTitle = urlTitle;
    }

    public String getSmallText() {
        return smallText;
    }

    public void setSmallText(String smallText) {
        this.smallText = smallText;
    }

    public List<Category> getCategoryList() {
        return categoryList;
    }

    public void setCategoryList(List<Category> categoryList) {
        this.categoryList = categoryList;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    public String getGridImageId() {
        return gridImageId;
    }

    public void setGridImageId(String gridImageId) {
        this.gridImageId = gridImageId;
    }

    public String getMainImageId() {
        return mainImageId;
    }

    public void setMainImageId(String mainImageId) {
        this.mainImageId = mainImageId;
    }

    public int getGridImageWidth() {
        return gridImageWidth;
    }

    public void setGridImageWidth(int gridImageWidth) {
        this.gridImageWidth = gridImageWidth;
    }

    public int getGridImageHeight() {
        return gridImageHeight;
    }

    public void setGridImageHeight(int gridImageHeight) {
        this.gridImageHeight = gridImageHeight;
    }

    @JsonIgnore
    public boolean isShouldHaveBackgroudOnGrid() {
        return isHasGridImage() && (1.2 * gridImageWidth < gridImageHeight);
    }

    @JsonIgnore
    public boolean isHasGridImage() {
        return !Strings.isNullOrEmpty(gridImageId);
    }


    @JsonIgnore
    public boolean isHasCategory() {
        return categoryList != null && !categoryList.isEmpty();
    }

    @JsonIgnore
    public Category getFirstCategory() {
        return isHasCategory() ? categoryList.iterator().next() : null;
    }

    @JsonIgnore
    public boolean isHasMainImage() {
        return !Strings.isNullOrEmpty(mainImageId);
    }
}
