package com.github.dimafeng.namsterdam.dao;

import com.github.dimafeng.namsterdam.model.Article;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ArticleRepositoryCustom {
    long getAndIncViews(String id);
}
