package com.github.dimafeng.namsterdam.controller.admin;

import com.github.dimafeng.namsterdam.model.Model;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Secured("ROLE_ADMIN")
public abstract class CRUDMapping<T extends Model, R extends T> {

    @RequestMapping(value = "", method = RequestMethod.GET)
    @ResponseBody
    public List<T> getAll() {
        return getRepository().findAll();
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    @ResponseBody
    public T get(@PathVariable("id") String id) {
        return getRepository().findOne(id);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.POST)
    @ResponseBody
    public T save(@PathVariable("id") String id, @RequestBody R item, Authentication authentication) throws Exception {
        item.setId(id);
        return saveUpdate(item, authentication);
    }

    @RequestMapping(value = "", method = RequestMethod.POST)
    @ResponseBody
    public T saveUpdate(@RequestBody R item, Authentication authentication) throws Exception {
        processBeforeSave(item, authentication);
        return getRepository().save(item);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    @ResponseBody
    public void delete(@PathVariable("id") String id) {
        getRepository().delete(id);
    }

    public abstract void processBeforeSave(R item, Authentication authentication) throws Exception;

    public abstract MongoRepository<T, String> getRepository();
}
