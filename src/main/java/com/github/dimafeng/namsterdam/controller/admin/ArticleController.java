package com.github.dimafeng.namsterdam.controller.admin;

import com.github.dimafeng.namsterdam.dao.AbstractPostRepository;
import com.github.dimafeng.namsterdam.dao.ImageRepository;
import com.github.dimafeng.namsterdam.dao.UserRepository;
import com.github.dimafeng.namsterdam.model.AbstractPost;
import com.github.dimafeng.namsterdam.model.Article;
import com.github.dimafeng.namsterdam.model.Image;
import com.github.dimafeng.namsterdam.service.HTMLService;
import com.github.dimafeng.namsterdam.service.ImageService;
import com.github.dimafeng.namsterdam.service.MarkdownService;
import com.google.common.base.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@Controller
@RequestMapping("/admin/articles")
public class ArticleController extends CRUDMapping<AbstractPost,Article> {

    @Autowired
    private AbstractPostRepository abstractPostRepository;

    @Autowired
    private MarkdownService markdownService;
    
    @Autowired
    private HTMLService htmlService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ImageRepository imageRepository;

    @Autowired
    private ImageService imageService;

    public List<AbstractPost> getAll() {
        return abstractPostRepository.findByClass(Article.class.getName());
    }

    @Override
    public void processBeforeSave(Article article, Authentication authentication)  throws Exception {
        if (Strings.isNullOrEmpty(article.getTitle())) {
            article.setTitle("New Article");
        }
        
        updateArticle(article, authentication, true);
    }

    @Override
    public MongoRepository<AbstractPost, String> getRepository() {
        return abstractPostRepository;
    }
    

    private Article updateArticle(Article article, Authentication authentication, boolean publish) throws Exception {
        if (article.getId() == null || article.getId().isEmpty() || article.getUser() == null) {
            article.setUser(userRepository.findByEmail(authentication.getName()));
        }

        if (publish && !Strings.isNullOrEmpty(article.getBody())) {
            article.setBodyHTML(markdownService.processALL(article.getBody()));
            article.setUpdateDate(new Date());
        }

        if (article.getCreationDate() != null) {
            article.setCreationDate(new Date());
        }
        article.setUrlTitle(htmlService.translit(article.getTitle()));

        if (article.getGridImageId() != null) {
            int[] size = imageService.getSize(article.getGridImageId());
            article.setGridImageWidth(size[0]);
            article.setGridImageHeight(size[1]);
        }

        if (article.getDisplayDate() == null) {
            article.setDisplayDate(new Date());
        }

        return abstractPostRepository.save(article);
    }

    @RequestMapping(value = "/preview", method = RequestMethod.POST)
    @ResponseBody
    public String articlePreview(@RequestBody String text) throws Exception {
        return markdownService.processALL(text);
    }

    @RequestMapping(value = "/{articleId}/images", method = RequestMethod.GET)
    @ResponseBody
    public List<Image> getImagesForArticle(@PathVariable("articleId") String articleId) {
        return imageRepository.findByArticleId(articleId);
    }

    @RequestMapping(value = "/{articleId}/draft", method = RequestMethod.POST)
    @ResponseBody
    public Article draftArticles(@PathVariable("articleId") String id, @RequestBody Article article, Authentication authentication) throws Exception {
        article.setId(id);
        return updateArticle(article, authentication, false);
    }
}
