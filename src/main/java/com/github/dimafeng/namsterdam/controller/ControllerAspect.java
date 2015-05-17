package com.github.dimafeng.namsterdam.controller;

import com.github.dimafeng.namsterdam.dao.MenuRepository;
import com.github.dimafeng.namsterdam.dao.PropertyRepository;
import com.github.dimafeng.namsterdam.model.Property;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.lang.reflect.Method;
import java.util.List;

@Aspect
@Component
public class ControllerAspect {

    @Value("${devmode:false}")
    private boolean devMode;

    @Value("${static.path:}")
    private String staticPath;

    @Autowired
    private MenuRepository menuRepository;

    @Autowired
    private PropertyRepository propertyRepository;

    @Pointcut("@within(org.springframework.stereotype.Controller)")
    public void anyPublicMethod() {
    }

    @Around("anyPublicMethod()")
    public Object menuConsumer(ProceedingJoinPoint pjp) throws Throwable {
        for (Object object : pjp.getArgs()) {
            if (object instanceof Model) {

                MethodSignature signature = (MethodSignature) pjp.getSignature();
                Method method = signature.getMethod();
                MenuConsumer menuConsumer = method.getAnnotation(MenuConsumer.class);

                Model model = (Model) object;

                model.addAttribute("devMode", devMode);
                model.addAttribute("staticPath", devMode ? "" : staticPath);

                if (menuConsumer != null) {
                    processMenuConsumer(pjp, model, method);
                }
            }
        }

        return pjp.proceed();
    }

    private void processMenuConsumer(ProceedingJoinPoint pjp, Model object, Method method) {
        RequestMapping requestMapping = method.getAnnotation(RequestMapping.class);
        String mapping = requestMapping.value()[0];

        object.addAttribute("menus", menuRepository.findAll(new Sort(Sort.Direction.ASC, "sortIndex")));
        List<Property> properties = propertyRepository.findAll();
        object.addAttribute("property", properties != null && !properties.isEmpty() ? properties.get(0) : new Property());


        /**
         * TODO change to regex
         */
        if (mapping != null) {
            if (mapping.startsWith("/s/")) {
                String seleted = pjp.getArgs()[0].toString();
                object.addAttribute("selectedMenu", seleted);
            } else if (mapping.equals("/") || mapping.startsWith("/article/")) {
                object.addAttribute("selectedMenu", "/");
            }
        }
    }
}
