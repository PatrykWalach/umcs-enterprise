package com.umcs.enterprise.data;

import org.apache.commons.dbcp2.BasicDataSource;
import org.apache.commons.dbcp2.BasicDataSourceFactory;
import org.hibernate.SessionFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.orm.hibernate5.HibernateTransactionManager;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.orm.hibernate5.LocalSessionFactoryBuilder;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.LocalEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;
import java.util.Properties;

@Configuration

public class EntityManager {
@Bean public  DataSource dataSource(){

    BasicDataSource dataSource = new BasicDataSource();
    dataSource.setUrl("jdbc:h2:mem:AZ");
    dataSource.setDriverClassName("org.h2.Driver");
    return  dataSource;
}

    @Bean public  HibernateJpaVendorAdapter hibernateJpaVendorAdapter(){
        HibernateJpaVendorAdapter adapter = new HibernateJpaVendorAdapter();
//        adapter.setGenerateDdl(true);
        return  adapter;
    }


    @Bean public PlatformTransactionManager transactionManager(SessionFactory entityManagerFactory){
        var manager = new JpaTransactionManager();
        manager.setEntityManagerFactory(entityManagerFactory);
        return manager;
    }

    @Bean public LocalSessionFactoryBean localSessionFactoryBean(DataSource dataSource){
        Properties hibernateProperties = new Properties();
        hibernateProperties.setProperty("hibernate.dialect", "org.hibernate.dialect.H2Dialect");
        hibernateProperties.setProperty("hibernate.hbm2ddl.auto", "create");

        var factoryBean = new LocalSessionFactoryBean();
        factoryBean.setPackagesToScan("com.umcs.enterprise");
        factoryBean.setDataSource(dataSource);
        factoryBean.setHibernateProperties(hibernateProperties);
        factoryBean.setBootstrapExecutor(new SimpleAsyncTaskExecutor());
        return factoryBean;
    }

    @Bean public SessionFactory entityManagerFactory(LocalSessionFactoryBean localSessionFactoryBean){
    return  localSessionFactoryBean.getObject();
    }

//    @Bean public LocalContainerEntityManagerFactoryBean localContainerEntityManagerFactoryBean(DataSource dataSource,  HibernateJpaVendorAdapter adapter){
//        LocalContainerEntityManagerFactoryBean factoryBean = new LocalContainerEntityManagerFactoryBean();
//        factoryBean.setDataSource(dataSource);
//        factoryBean.setJpaVendorAdapter(adapter);
////        localContainerEntityManagerFactoryBean.setEntityManagerInitializer();
//        factoryBean.setBootstrapExecutor(new SimpleAsyncTaskExecutor());
//        return  factoryBean;
//    }



}
