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
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.lang.reflect.Method;
import java.util.List;

@Aspect
@Component
public class ControllerAspect {

    @Autowired
    private MenuRepository menuRepository;

    @Autowired
    private PropertyRepository propertyRepository;

    @Pointcut("@within(org.springframework.stereotype.Controller)")
    public void anyPublicMethod() {
    }

    @Around("anyPublicMethod() && @annotation(menuConsumer)")
    public Object menuConsumer(ProceedingJoinPoint pjp, MenuConsumer menuConsumer) throws Throwable {
        for (Object object : pjp.getArgs()) {
            if (object instanceof Model) {
                ((Model) object).addAttribute("menus", menuRepository.findAll(new Sort(Sort.Direction.ASC, "sortIndex")));
                List<Property> properties = propertyRepository.findAll();
                ((Model) object).addAttribute("property", properties != null && !properties.isEmpty() ? properties.get(0) : new Property());

                MethodSignature signature = (MethodSignature) pjp.getSignature();
                Method method = signature.getMethod();

                RequestMapping requestMapping = method.getAnnotation(RequestMapping.class);
                String mapping = requestMapping.value()[0];
                /**
                 * TODO change to regex
                 */
                if (mapping != null) {
                    if (mapping.startsWith("/s/")) {
                        String seleted = pjp.getArgs()[0].toString();
                        ((Model) object).addAttribute("selectedMenu", seleted);
                    } else if (mapping.equals("/") || mapping.startsWith("/article/")) {
                        ((Model) object).addAttribute("selectedMenu", "/");
                    }
                }
            }
        }

        return pjp.proceed();
    }
}
