package com.github.dimafeng.namsterdam.controller.admin;


import com.github.dimafeng.namsterdam.dao.CategoryRepository;
import com.github.dimafeng.namsterdam.model.Category;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/admin/categories")
public class CategoriesController implements CRUDMapping<Category> {

    @Autowired
    private CategoryRepository repository;

    @Override
    public void processBeforeSave(Category item) {

    }

    @Override
    public MongoRepository<Category, String> getRepository() {
        return repository;
    }
}
