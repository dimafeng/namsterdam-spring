package com.github.dimafeng.namsterdam.dao;

public interface ArticleRepositoryCustom {
    long getAndIncViews(String id);
}
