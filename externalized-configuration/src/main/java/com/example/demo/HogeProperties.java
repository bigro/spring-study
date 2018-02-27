package com.example.demo;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties("hoge")
public class HogeProperties {
    public String hoge;
    public String fuga;

    public String getHoge() {
        return hoge;
    }

    public void setHoge(String hoge) {
        this.hoge = hoge;
    }

    public String getFuga() {
        return fuga;
    }

    public void setFuga(String fuga) {
        this.fuga = fuga;
    }
}
