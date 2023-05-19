package com.umcs.enterprise;

import com.umcs.enterprise.seed.Seed;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import org.apache.catalina.LifecycleException;
import org.apache.catalina.startup.Tomcat;
import org.slf4j.spi.SLF4JServiceProvider;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

public class EnterpriseApplication {

	public static void main(String[] args) throws LifecycleException, IOException {
		File baseDir = File.createTempFile("tomcat.", "." + 8080);
		baseDir.delete();
		baseDir.mkdir();
		baseDir.deleteOnExit();

		Tomcat server = new Tomcat();
		server.setBaseDir(baseDir.getAbsolutePath());
		server.setPort(8080);
		server.getHost().setAppBase(".");
		server.addWebapp("", ".");
		server.getConnector();
		server.start();
		server.getServer().await();
	}
}
