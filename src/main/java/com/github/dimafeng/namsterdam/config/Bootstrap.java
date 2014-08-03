package com.github.dimafeng.namsterdam.config;

import com.github.dimafeng.namsterdam.dao.UserRepository;
import com.github.dimafeng.namsterdam.model.User;
import com.github.dimafeng.namsterdam.service.UserService;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

@Component
public class Bootstrap implements ApplicationListener {

    @Override
    public void onApplicationEvent(ApplicationEvent event) {
        if (event instanceof ContextRefreshedEvent) {
            ApplicationContext applicationContext = ((ContextRefreshedEvent) event).getApplicationContext();
            UserRepository userRepository = applicationContext.getBean(UserRepository.class);
            UserService userService = applicationContext.getBean(UserService.class);
            if(userRepository.findAll().isEmpty()) {
                User user = new User();
                user.setEmail("test@test.com");
                user.setPassword(userService.encode("test"));
                userRepository.save(user);
            }
        }
    }
}
