package com.example.smartstudy.config;

import jakarta.websocket.server.ServerEndpointConfig;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

public class CustomSpringConfigurator extends ServerEndpointConfig.Configurator implements ApplicationContextAware {

    private static volatile BeanFactory context;

    @Override
    public <T> T getEndpointInstance(Class<T> clazz) {
        return context.getBean(clazz);
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        CustomSpringConfigurator.context = applicationContext;
    }

}


