package com.cabin.desktop;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class Application {

    public static void main(String[] args) {
        new SpringApplicationBuilder(Application.class).headless(false).web(false).run(args);
    }

    @Bean
    public PWLauncher frame() {
        return new PWLauncher();
    }
}
