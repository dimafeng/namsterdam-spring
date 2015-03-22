package com.github.dimafeng.namsterdam.controller.admin;

import com.github.dimafeng.namsterdam.dao.AbstractPostRepository;
import com.github.dimafeng.namsterdam.dao.UserRepository;
import com.github.dimafeng.namsterdam.model.AbstractPost;
import com.github.dimafeng.namsterdam.model.VideoPost;
import com.github.dimafeng.namsterdam.service.HTMLService;
import com.github.dimafeng.namsterdam.service.YouTubeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Date;
import java.util.List;

@Controller
@RequestMapping("/admin/videos")
public class VideoPostController implements CRUDMapping<AbstractPost, VideoPost> {

    @Autowired
    private AbstractPostRepository abstractPostRepository;

    @Autowired
    private YouTubeService youTubeService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private HTMLService htmlService;

    public List<AbstractPost> getAll() {
        return abstractPostRepository.findByClass(VideoPost.class.getName());
    }

    @Override
    public void processBeforeSave(VideoPost video, Authentication authentication) throws Exception {
        YouTubeService.VideoInfo videoInfo = youTubeService.getVideoInfo(video.getVideoId());
        video.setImageUrl(videoInfo.data.thumbnail.hqDefault);
        video.setTitle(videoInfo.data.title);

        if (video.getId() == null || video.getId().isEmpty() || video.getUser() == null) {
            video.setUser(userRepository.findByEmail(authentication.getName()));
        }

        video.setBodyHTML("test");

        if (video.getCreationDate() != null) {
            video.setCreationDate(new Date());
        }
        video.setUrlTitle(htmlService.translit(video.getTitle()));

        if (video.getDisplayDate() == null) {
            video.setDisplayDate(new Date());
        }
        video.setDisplay(true);
    }

    @Override
    public MongoRepository<AbstractPost, String> getRepository() {
        return abstractPostRepository;
    }
}
