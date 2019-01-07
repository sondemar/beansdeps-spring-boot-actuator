package com.sondecki.beansdeps;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.actuate.beans.BeansEndpoint;
import org.springframework.boot.actuate.endpoint.web.annotation.RestControllerEndpoint;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Map;

@RestControllerEndpoint(id = "beans-deps")
@RequiredArgsConstructor
public class BeansDepsEndpoint {

    private final BeansEndpoint beansEndpoint;

    @GetMapping
    public void getBeansDeps(){
        BeansEndpoint.ApplicationBeans beans = beansEndpoint.beans();
        Map<String, BeansEndpoint.ContextBeans> contexts = beans.getContexts();

    }
}
