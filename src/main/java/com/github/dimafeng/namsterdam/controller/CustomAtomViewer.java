package com.github.dimafeng.namsterdam.controller;

import com.github.dimafeng.namsterdam.model.Article;
import com.sun.syndication.feed.atom.Content;
import com.sun.syndication.feed.atom.Entry;
import com.sun.syndication.feed.atom.Feed;
import com.sun.syndication.feed.atom.Link;
import org.springframework.web.servlet.view.feed.AbstractAtomFeedView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;

public class CustomAtomViewer {}

/*public class CustomAtomViewer extends AbstractAtomFeedView {

    @Override
    protected void buildFeedMetadata(Map<String, Object> model, Feed feed, HttpServletRequest request) {
        feed.setId("http://namsterdam.us");
        feed.setTitle("Новый Амстердам");
        @SuppressWarnings("unchecked")
        List<Article> listContent = (List<Article>) model.get("feedContent");
        for (Article content : listContent) {
            Date date = content.getDisplayDate();
            if (feed.getUpdated() == null || date.compareTo(feed.getUpdated()) > 0) {
                feed.setUpdated(date);
            }
        }
    }

    @Override
    protected List<Entry> buildFeedEntries(Map<String, Object> model,
                                           HttpServletRequest request, HttpServletResponse response) throws Exception {

        @SuppressWarnings("unchecked")
        List<Article> contentList = (List<Article>) model.get("feedContent");
        List<Entry> entries = new ArrayList<>(contentList.size());

        for (Article content : contentList) {
            Entry entry = new Entry();

            entry.setId("http://namsterdam.us/article/" + content.getUrlTitle());
            entry.setTitle(content.getTitle());
            entry.setUpdated(content.getDisplayDate());

            Link link = new Link();
            link.setHref("http://namsterdam.us/article/" + content.getUrlTitle());

            entry.setAlternateLinks(Arrays.asList(link));
            Content summary = new Content();
            summary.setValue(content.getBodyHTML());
            summary.setType("html");
            entry.setSummary(summary);

            entries.add(entry);
        }

        return entries;

    }
}*/
