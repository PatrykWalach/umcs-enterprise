package com.umcs.enterprise.seed;

import jakarta.annotation.PreDestroy;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import jakarta.transaction.TransactionManager;
import javax.sql.DataSource;
import org.h2.jdbcx.JdbcDataSource;
import org.hibernate.jpa.boot.spi.EntityManagerFactoryBuilder;
import org.hibernate.sql.Template;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.io.Resource;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.datasource.SimpleDriverDataSource;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;
import org.springframework.jndi.JndiTemplate;
import org.springframework.orm.hibernate5.HibernateTransactionManager;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
@EnableJpaRepositories("com.umcs.enterprise")
public class HibernateConfig {

	//
	//
	@Bean
	public DataSource dataSource() {
		return new EmbeddedDatabaseBuilder().setType(EmbeddedDatabaseType.H2).build();
		//
		//    JdbcDataSource dataSource = new JdbcDataSource();
		//
		//    dataSource.setUrl("jdbc:h2:mem:AZ");
		//
		//
		//
		//
		//    return dataSource;
	}

	//

	@Bean
	public LocalContainerEntityManagerFactoryBean localContainerEntityManagerFactoryBean(
		DataSource dataSource
	) {
		LocalContainerEntityManagerFactoryBean factory = new LocalContainerEntityManagerFactoryBean();
		factory.setJpaVendorAdapter(new HibernateJpaVendorAdapter());
		factory.setPackagesToScan("com.umcs.enterprise");
		factory.setDataSource(dataSource);
		return factory;
	}

	@Bean
	public EntityManagerFactory entityManagerFactory(LocalContainerEntityManagerFactoryBean factory) {
		return factory.getNativeEntityManagerFactory();
	}
	//
	//
	//
	//
	//
	//    @Bean public
	//    LocalSessionFactoryBean localSessionFactoryBean(DataSource dataSource){
	//        LocalSessionFactoryBean localSessionFactoryBean = new LocalSessionFactoryBean();
	//        localSessionFactoryBean.setDataSource(dataSource);
	//        localSessionFactoryBean.setPackagesToScan("com.umcs.enterprise");
	//        return localSessionFactoryBean;
	//    }

	//@Bean("myEntityManager") public  EntityManager entityManager(
	//        LocalContainerEntityManagerFactoryBean factoryBean
	//){

	//    return factoryBean.getNativeEntityManagerFactory().createEntityManager();
	//}
	//
	//@Bean public PlatformTransactionManager platformTransactionManager(EntityManagerFactory entityMangerFactory){
	//    JpaTransactionManager manager = new JpaTransactionManager();
	//    manager.setEntityManagerFactory(entityMangerFactory);
	//    return manager;
	//}

}
