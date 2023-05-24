package com.umcs.enterprise.env;

import me.paulschwarz.springdotenv.DotenvPropertySource;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

@Component

public class DotenvConfig {

    public DotenvConfig(ConfigurableEnvironment env) { DotenvPropertySource.addToEnvironment(env);
    }


}
