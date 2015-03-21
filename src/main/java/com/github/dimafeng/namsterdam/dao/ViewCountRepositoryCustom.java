package com.github.dimafeng.namsterdam.dao;


public interface ViewCountRepositoryCustom {
    long getAndIncViews(String id);
}
