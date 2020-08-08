package com.example.spring.mvc.hibernate.mysql.config;

import com.example.spring.mvc.hibernate.mysql.entity.Authorities;
import com.example.spring.mvc.hibernate.mysql.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.orm.hibernate5.HibernateTransactionManager;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import java.util.Properties;

import static org.hibernate.cfg.AvailableSettings.*;

/**
 * Created by Vincent Karuri on 08/08/2020
 */

@Configuration
@PropertySource("classpath:db.properties")
@EnableTransactionManagement
@ComponentScan("com.example.spring.mvc.hibernate.mysql")
public class AppConfig {

	@Autowired
	private Environment environment;

	@Bean
	public LocalSessionFactoryBean getSessionFactory() {
		LocalSessionFactoryBean sessionFactoryBean = new LocalSessionFactoryBean();
		Properties properties = new Properties();

		// Setting JDBC properties
		properties.put(DRIVER, environment.getProperty("mysql.driver"));
		properties.put(URL, environment.getProperty("mysql.jdbc.url"));
		properties.put(USER, environment.getProperty("mysql.username"));
		properties.put(PASS, environment.getProperty("mysql.password"));

		// Setting Hibernate properties
		properties.put(SHOW_SQL, environment.getProperty("hibernate.show_sql"));
		properties.put(HBM2DDL_AUTO, environment.getProperty("hibernate.hbm2ddl.auto"));

		// Setting C3P0 properties
		properties.put(C3P0_MIN_SIZE, environment.getProperty("hibernate.c3p0.min_size"));
		properties.put(C3P0_MAX_SIZE, environment.getProperty("hibernate.c3p0.max_size"));
		properties.put(C3P0_ACQUIRE_INCREMENT, environment.getProperty("hibernate.c3p0.acquire_increment"));
		properties.put(C3P0_TIMEOUT, environment.getProperty("hibernate.c3p0.timeout"));
		properties.put(C3P0_MAX_STATEMENTS, environment.getProperty("hibernate.c3p0.max_statements"));

		sessionFactoryBean.setHibernateProperties(properties);
		sessionFactoryBean.setAnnotatedClasses(User.class, Authorities.class);

		return sessionFactoryBean;
	}

	@Bean
	public HibernateTransactionManager getTransactionManager() {
		HibernateTransactionManager hibernateTransactionManager = new HibernateTransactionManager();
		hibernateTransactionManager.setSessionFactory(getSessionFactory().getObject());
		return hibernateTransactionManager;
	}
}
