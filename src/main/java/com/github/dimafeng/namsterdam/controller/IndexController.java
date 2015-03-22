package com.github.dimafeng.namsterdam.controller;

import com.github.dimafeng.namsterdam.dao.*;
import com.github.dimafeng.namsterdam.model.AbstractPost;
import com.github.dimafeng.namsterdam.model.Article;
import com.github.dimafeng.namsterdam.model.Menu;
import com.github.dimafeng.namsterdam.service.ImageService;
import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
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
    private AbstractPostRepository abstractPostRepository;

    @Autowired
    private MenuRepository menuRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ViewCountRepository viewCountRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private ImageService imageService;

    @RequestMapping("/")
    @MenuConsumer
    public String showIndex(@RequestParam(required = false) Integer page, Model model) {

        Pageable pageSpecification = new PageRequest(page == null ? 0 : page, ARTICLE_COUNT, new Sort(Sort.Direction.DESC, "displayDate"));

        Page<AbstractPost> articlePage = abstractPostRepository.findAllByDisplay(true, pageSpecification);
        Long count = abstractPostRepository.countByDisplay(true);
        int pageCount = Long.valueOf(count / ARTICLE_COUNT).intValue();
        List<Integer> list = new ArrayList<>(pageCount);

        for (int i = 0; i <= pageCount; i++)
            list.add(i);

        model.addAttribute("maxPage", list.size());
        model.addAttribute("nextPage", page == null ? 0 : page + 1);
        model.addAttribute("page", page == null ? 0 : page);
        model.addAttribute("previousPage", page == null ? 0 : page - 1);
        model.addAttribute("pageCount", list);
        model.addAttribute("articles", articlePage.getContent());

        return "index";
    }

    @RequestMapping("/article/{articleName}")
    @MenuConsumer
    public String showArticle(@PathVariable("articleName") String articleName, Model model) {

        AbstractPost articlePage = abstractPostRepository.findByUrlTitle(articleName);
        if (articlePage instanceof Article) {
            model.addAttribute("article", articlePage);

            model.addAttribute("viewCount", viewCountRepository.getAndIncViews(articlePage.getId()));

            return "article";
        }
        throw new IllegalStateException();
    }

    @RequestMapping("/categories")
    @MenuConsumer
    public String category(Model model) {
        model.addAttribute("categories", categoryRepository.findAll(new Sort(Sort.Direction.ASC, "sortIndex")));
        return "categories";
    }

    @RequestMapping("/category/{categoryName}")
    @MenuConsumer
    public String category(@PathVariable("categoryName") String categoryName, @RequestParam(required = false) Integer page, Model model) {
        Pageable pageSpecification = new PageRequest(page == null ? 0 : page, 20);

        Page<AbstractPost> articlePage = abstractPostRepository.findByCategory(new ObjectId(categoryRepository.findByUrlTitle(categoryName).getId()), true, pageSpecification);


        Long count = abstractPostRepository.countByCategory(new ObjectId(categoryRepository.findByUrlTitle(categoryName).getId()));
        int pageCount = Long.valueOf(count / ARTICLE_COUNT).intValue();
        List<Integer> list = new ArrayList<>(pageCount);

        for (int i = 0; i <= pageCount; i++)
            list.add(i);

        model.addAttribute("maxPage", list.size());
        model.addAttribute("nextPage", page == null ? 0 : page + 1);
        model.addAttribute("page", page == null ? 0 : page);
        model.addAttribute("previousPage", page == null ? 0 : page - 1);
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
        mav.addObject("feedContent", abstractPostRepository.findAllByDisplay(true, new PageRequest(0, 20, new Sort(Sort.Direction.DESC, "displayDate"))).getContent());

        return mav;
    }

    @RequestMapping(value = "/feed", method = RequestMethod.GET)
    public void feedburner(HttpServletResponse response) throws IOException {
        response.sendRedirect("http://feeds.feedburner.com/namsterdam");
    }

    @RequestMapping(value = "/images/{size}/{imageId}.jpg", method = RequestMethod.GET, produces = MediaType.IMAGE_JPEG_VALUE)
    @ResponseBody
    public byte[] images(@PathVariable("size") int size,
                         @PathVariable("imageId") String id,
                         @RequestParam(required = false) Boolean gridImage) throws Exception {
        return imageService.getImage(size, id, gridImage == null ? false : gridImage);
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