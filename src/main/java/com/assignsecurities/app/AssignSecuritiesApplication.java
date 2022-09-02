package com.assignsecurities.app;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;


@SpringBootApplication
@ComponentScan({"com.assignsecurities.*"})
@EntityScan("com.assignsecurities.domain")
@EnableScheduling
public class AssignSecuritiesApplication extends SpringBootServletInitializer {
	 private static final Logger logger = LoggerFactory.getLogger(AssignSecuritiesApplication.class);

	 @Override
	 protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
	  return application.sources(AssignSecuritiesApplication.class);
	 }	

    public static void main(String[] args) {
        SpringApplication.run(AssignSecuritiesApplication.class, args);
        logger.info("com.assignsecurities Started........");
    }

    @Bean
    public FilterRegistrationBean corsFilter() {
        final FilterRegistrationBean filterRegBean = new FilterRegistrationBean();
        filterRegBean.setFilter(new CorsFilter());
        filterRegBean.addUrlPatterns("/*");
        filterRegBean.setEnabled(Boolean.TRUE);
        filterRegBean.setName("CorsFilter");
        filterRegBean.setAsyncSupported(Boolean.TRUE);
        filterRegBean.setOrder(1);
        return filterRegBean;
    }

    @Bean
    public FilterRegistrationBean tokenValidatorBean() {
        final FilterRegistrationBean filterRegBean = new FilterRegistrationBean();
        filterRegBean.setFilter(new TokenValidator());
        filterRegBean.addUrlPatterns("/*");
        filterRegBean.setEnabled(Boolean.TRUE);
        filterRegBean.setName("TokenValidator");
        filterRegBean.setAsyncSupported(Boolean.TRUE);
        filterRegBean.setOrder(2);
        return filterRegBean;
    }
}
