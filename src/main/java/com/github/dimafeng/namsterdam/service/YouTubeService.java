package com.github.dimafeng.namsterdam.service;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.net.URL;

@Service
public class YouTubeService {
    public VideoInfo getVideoInfo(String videoId) {
        try {
            try (InputStream inputStream = new URL("http://gdata.youtube.com/feeds/api/videos/" + videoId + "?v=2&alt=jsonc").openStream()) {
                return new ObjectMapper().readValue(inputStream, VideoInfo.class);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class VideoInfo {
        public Data data;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Data {
        public String title;
        public Thumbnail thumbnail;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Thumbnail {
        public String hqDefault;
    }
}
