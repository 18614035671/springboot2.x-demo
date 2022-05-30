package com.example.demo.properties;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

/**
 * 自定义配置文件
 */
@Component
@PropertySource("classpath:my.properties")
@ConfigurationProperties(prefix = "my")
public class MyProperties {

    private int age;

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    @Override
    public String toString() {
        return "MyProperties{" +
                "age=" + age +
                '}';
    }
}
