package com.umcs.enterprise;


import com.umcs.enterprise.seed.Seed;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@ComponentScan

@EnableJpaAuditing
public class EnterpriseApplication {

	public static void main(String[] args) {
		AnnotationConfigApplicationContext ctx = new AnnotationConfigApplicationContext(EnterpriseApplication.class);

		Seed seed = ctx.getBean("seed", Seed.class);
		seed.initDatabase().run();
	}
}
