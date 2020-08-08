package com.example.spring.mvc.hibernate.mysql.initializer;

import com.example.spring.mvc.hibernate.mysql.config.AppConfig;
import com.example.spring.mvc.hibernate.mysql.config.WebConfig;
import com.example.spring.mvc.hibernate.mysql.config.WebSecurityConfig;
import org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer;

/**
 * Created by Vincent Karuri on 08/08/2020
 */
public class WebMvcApplicationInitializer extends AbstractAnnotationConfigDispatcherServletInitializer {

	@Override
	protected Class<?>[] getRootConfigClasses() {
		return new Class[]{ AppConfig.class, WebSecurityConfig.class };
	}

	@Override
	protected Class<?>[] getServletConfigClasses() {
		return new Class[]{ WebConfig.class };
	}

	@Override
	protected String[] getServletMappings() {
		return new String[]{ "/" };
	}
}
