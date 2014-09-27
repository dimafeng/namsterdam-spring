package com.github.dimafeng.namsterdam.controller;

import com.github.dimafeng.namsterdam.model.Article;
import com.sun.syndication.feed.rss.Channel;
import com.sun.syndication.feed.rss.Item;
import org.springframework.web.servlet.view.feed.AbstractRssFeedView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class CustomRssViewer extends AbstractRssFeedView {

    public CustomRssViewer() {
        super();
        setContentType("application/rss+xml; charset=utf-8");
    }

    @Override
    protected void buildFeedMetadata(Map<String, Object> model, Channel feed,
                                     HttpServletRequest request) {

        feed.setTitle("Новый Амстердам");
        feed.setDescription("Наш блог – это наш собственный опыт проживания в одном из самых знаменитых городов мира – в Нью-Йорке");
        feed.setLink("http://namsterdam.us/");

        super.buildFeedMetadata(model, feed, request);
    }


    @Override
    protected List<Item> buildFeedItems(Map<String, Object> model, HttpServletRequest request, HttpServletResponse response) throws Exception {

        @SuppressWarnings("unchecked")
        List<Article> listContent = (List<Article>) model.get("feedContent");
        List<Item> items = new ArrayList<>(listContent.size());

        for (Article article : listContent) {

            Item item = new Item();
            item.setContent(item.getContent());

            item.setTitle(article.getTitle());
            item.setLink("http://namsterdam.us/article/" + article.getUrlTitle());
            item.setPubDate(article.getDisplayDate());

            items.add(item);
        }

        return items;
    }
}
