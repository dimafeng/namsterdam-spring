package com.github.dimafeng.namsterdam.controller;

import com.github.dimafeng.namsterdam.dao.ArticleRepository;
import com.github.dimafeng.namsterdam.dao.MenuRepository;
import com.github.dimafeng.namsterdam.dao.UserRepository;
import com.github.dimafeng.namsterdam.model.Article;
import com.github.dimafeng.namsterdam.model.Menu;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;


@Controller
public class IndexController {

    static final Logger log = LoggerFactory.getLogger(IndexController.class);

    @Autowired
    private ArticleRepository articleRepository;

    @Autowired
    private MenuRepository menuRepository;

    @Autowired
    private UserRepository userRepository;

    @RequestMapping("/")
    @MenuConsumer
    public String showIndex(Model model) {

        Pageable pageSpecification = new PageRequest(0, 10);

        Page<Article> articlePage = articleRepository.findAllByDisplay(true, pageSpecification);

        model.addAttribute("articles", articlePage.getContent());

        return "index";
    }

    @RequestMapping("/article/{articleName}")
    @MenuConsumer
    public String showArticle(@PathVariable("articleName") String articleName, Model model) {

        Article articlePage = articleRepository.findByUrlTitle(articleName);
        model.addAttribute("article", articlePage);

        return "article";
    }

    @RequestMapping("/category/{categoryName}")
    @MenuConsumer
    public String category(@PathVariable("categoryName") String categoryName, Model model) {
        Pageable pageSpecification = new PageRequest(0, 10);

        Page<Article> articlePage = articleRepository.findByCategory(categoryName, pageSpecification);
        model.addAttribute("articles", articlePage.getContent());

        return "index";
    }

    @RequestMapping("/s/{pageName}")
    @MenuConsumer
    public String page(@PathVariable("pageName") String pageName, Model model) {

        Menu menu = menuRepository.findByUrl(pageName);

        if (menu.isLink()) {
            throw new IllegalStateException();
        }

        model.addAttribute("menu", menu);

        return "menu";
    }

    @RequestMapping("/error500")
    public String error500() {
        return "errors/500";
    }

    @RequestMapping("/error404")
    public String error404() {
        return "errors/404";
    }
}