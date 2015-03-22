package com.github.dimafeng.namsterdam.model;

import com.google.common.base.Strings;
import org.codehaus.jackson.annotate.JsonIgnore;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;
import java.util.List;

public class Article extends AbstractPost {

    private String body;
    private String smallText;
    private String gridImageId;
    private String mainImageId;
    private int gridImageWidth;
    private int gridImageHeight;

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getSmallText() {
        return smallText;
    }

    public void setSmallText(String smallText) {
        this.smallText = smallText;
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

    public boolean isShouldHaveBackgroudOnGrid() {
        return isHasGridImage() && (1.2 * gridImageWidth < gridImageHeight);
    }

    @JsonIgnore
    public boolean isHasCategory() {
        return getCategoryList() != null && !getCategoryList().isEmpty();
    }

    @JsonIgnore
    public Category getFirstCategory() {
        return isHasCategory() ? getCategoryList().iterator().next() : null;
    }

    @JsonIgnore
    public boolean isHasMainImage() {
        return !Strings.isNullOrEmpty(mainImageId);
    }

    @Override
    public String getGridImageUrl() {
        return "/images/300/" + gridImageId + ".jpg" +
                (isShouldHaveBackgroudOnGrid() ? "?gridImage=true" : "");
    }

    @Override
    public boolean isHasGridImage() {
        return !Strings.isNullOrEmpty(gridImageId);
    }
}
