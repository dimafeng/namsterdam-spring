package com.github.dimafeng.namsterdam.controller.admin;


import com.github.dimafeng.namsterdam.dao.CategoryRepository;
import com.github.dimafeng.namsterdam.model.Category;
import com.github.dimafeng.namsterdam.service.HTMLService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/admin/categories")
public class CategoriesController extends CRUDMapping<Category,Category> {

    @Autowired
    private CategoryRepository repository;

    @Autowired
    private HTMLService htmlService;

    @Override
    public void processBeforeSave(Category item, Authentication authentication) {
        item.setUrlTitle(htmlService.translit(item.getName()));
    }

    @Override
    public MongoRepository<Category, String> getRepository() {
        return repository;
    }
}
