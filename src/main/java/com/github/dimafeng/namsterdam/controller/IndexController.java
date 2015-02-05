package com.github.dimafeng.namsterdam.controller;

import com.github.dimafeng.namsterdam.dao.ArticleRepository;
import com.github.dimafeng.namsterdam.dao.CategoryRepository;
import com.github.dimafeng.namsterdam.dao.MenuRepository;
import com.github.dimafeng.namsterdam.dao.UserRepository;
import com.github.dimafeng.namsterdam.model.Article;
import com.github.dimafeng.namsterdam.model.Menu;
import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Controller
public class IndexController {

    static final Logger log = LoggerFactory.getLogger(IndexController.class);

    private static final int ARTICLE_COUNT = 20;

    @Autowired
    private ArticleRepository articleRepository;

    @Autowired
    private MenuRepository menuRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @RequestMapping("/")
    @MenuConsumer
    public String showIndex(@RequestParam(required = false) Integer page ,Model model) {

        Pageable pageSpecification = new PageRequest(page == null ? 0 : page, ARTICLE_COUNT, new Sort(Sort.Direction.DESC, "displayDate"));

        Page<Article> articlePage = articleRepository.findAllByDisplay(true, pageSpecification);
        Long count = articleRepository.countByDisplay(true);
        int pageCount = Long.valueOf(count / ARTICLE_COUNT).intValue();
        List<Integer> list = new ArrayList<>(pageCount);

        for (int i=0;i<=pageCount;i++)
         list.add(i);

        model.addAttribute("maxPage",list.size());
        model.addAttribute("nextPage",page == null ? 0 : page+1);
        model.addAttribute("page",page == null ? 0 : page);
        model.addAttribute("previousPage",page == null ? 0 : page-1);
        model.addAttribute("pageCount", list);
        model.addAttribute("articles", articlePage.getContent());

        return "index";
    }

    @RequestMapping("/article/{articleName}")
    @MenuConsumer
    public String showArticle(@PathVariable("articleName") String articleName, Model model) {

        Article articlePage = articleRepository.findByUrlTitle(articleName);
        model.addAttribute("article", articlePage);

        articleRepository.getAndIncViews(articlePage.getId());

        return "article";
    }

    @RequestMapping("/categories")
    @MenuConsumer
    public String category(Model model) {
        model.addAttribute("categories", categoryRepository.findAll(new Sort(Sort.Direction.ASC, "sortIndex")));
        return "categories";
    }

    @RequestMapping("/category/{categoryName}")
    @MenuConsumer
    public String category(@PathVariable("categoryName") String categoryName,@RequestParam(required = false) Integer page, Model model) {
        Pageable pageSpecification = new PageRequest(page==null?0:page, 20);

        Page<Article> articlePage = articleRepository.findByCategory(new ObjectId(categoryRepository.findByUrlTitle(categoryName).getId()), pageSpecification);


        Long count = articleRepository.countByCategory(new ObjectId(categoryRepository.findByUrlTitle(categoryName).getId()));
        int pageCount = Long.valueOf(count / ARTICLE_COUNT).intValue();
        List<Integer> list = new ArrayList<>(pageCount);

        for (int i=0;i<=pageCount;i++)
            list.add(i);

        model.addAttribute("maxPage",list.size());
        model.addAttribute("nextPage",page == null ? 0 : page+1);
        model.addAttribute("page",page == null ? 0 : page);
        model.addAttribute("previousPage",page == null ? 0 : page-1);
        model.addAttribute("pageCount", list);


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

    @RequestMapping(value = "/feedAtom", method = RequestMethod.GET)
    public ModelAndView getFeedAtom(HttpServletResponse response) {
        response.setCharacterEncoding("UTF-8");

        ModelAndView mav = new ModelAndView();
        mav.setViewName("rssViewer");
        mav.addObject("feedContent", articleRepository.findAllByDisplay(true, new PageRequest(0, 20, new Sort(Sort.Direction.DESC, "displayDate"))).getContent());

        return mav;
    }

    @RequestMapping(value = "/feed", method = RequestMethod.GET)
    public void feedburner(HttpServletResponse response) throws IOException {
        response.sendRedirect("http://feeds.feedburner.com/namsterdam");
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