package com.github.dimafeng.namsterdam.model;


public class VideoPost extends AbstractPost {

    private String videoId;
    private String imageUrl;

    public String getVideoId() {
        return videoId;
    }

    public void setVideoId(String videoId) {
        this.videoId = videoId;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    @Override
    public String getGridImageUrl() {
        return imageUrl;
    }

    @Override
    public boolean isHasGridImage() {
        return imageUrl!=null;
    }
}
