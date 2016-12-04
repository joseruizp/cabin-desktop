package com.cabin.desktop;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class Runner implements CommandLineRunner {

    @Autowired
    private PWLauncher pwLauncher;

    public void run(String... arg0) throws Exception {
        java.awt.EventQueue.invokeLater(() -> {
            pwLauncher.setVisible(true);
        });
    }

}
