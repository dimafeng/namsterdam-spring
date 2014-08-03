package com.github.dimafeng.namsterdam.dao;


import com.github.dimafeng.namsterdam.model.Menu;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface MenuRepository extends MongoRepository<Menu, String> {

    Menu findByUrl(String url);
}
