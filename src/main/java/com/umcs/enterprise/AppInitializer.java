package com.umcs.enterprise;

import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRegistration;
import org.slf4j.spi.SLF4JServiceProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.web.WebApplicationInitializer;
import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@Configuration
@ComponentScan
@EnableWebMvc
@EnableJpaAuditing
public class AppInitializer implements WebApplicationInitializer {
    @Override
    public void onStartup(ServletContext container) throws ServletException {
        // Create the 'root' Spring application context
        AnnotationConfigWebApplicationContext rootContext = new AnnotationConfigWebApplicationContext();
        rootContext.register(AppInitializer.class);

        // Manage the lifecycle of the root application context
        container.addListener(new ContextLoaderListener(rootContext));

        // Create the dispatcher servlet's Spring application context
        AnnotationConfigWebApplicationContext dispatcherContext = new AnnotationConfigWebApplicationContext();

        // Register and map the dispatcher servlet
        ServletRegistration.Dynamic dispatcher = container
                .addServlet("dispatcher", new DispatcherServlet(dispatcherContext));
        dispatcher.setLoadOnStartup(1);
        dispatcher.addMapping("/");

//        Seed seed = ctx.getBean("seed", Seed.class);
//        seed.initDatabase().run();
    }



    @Bean
    public SLF4JServiceProvider slf4JServiceProvider(){

        return new org.slf4j.simple.SimpleServiceProvider
                ();
    }
}
