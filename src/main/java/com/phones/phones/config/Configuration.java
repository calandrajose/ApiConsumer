package com.phones.phones.config;

import com.phones.phones.session.SessionBackOfficeFilter;
import com.phones.phones.session.SessionClientFilter;
import com.phones.phones.session.SessionFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@org.springframework.context.annotation.Configuration
@EnableSwagger2
public class Configuration {

    private final SessionFilter sessionFilter;
    private final SessionClientFilter sessionClientFilter;
    private final SessionBackOfficeFilter sessionBackOfficeFilter;

    @Autowired
    public Configuration(final SessionFilter sessionFilter,
                         final SessionClientFilter sessionClientFilter,
                         final SessionBackOfficeFilter sessionBackOfficeFilter) {
        this.sessionFilter = sessionFilter;
        this.sessionClientFilter = sessionClientFilter;
        this.sessionBackOfficeFilter = sessionBackOfficeFilter;
    }


    @Bean
    public FilterRegistrationBean filterBackOffice() {
        FilterRegistrationBean registration = new FilterRegistrationBean();
        registration.setFilter(sessionBackOfficeFilter);
        registration.addUrlPatterns("/api/backoffice/*");
        return registration;
    }

    @Bean
    public FilterRegistrationBean filterClient() {
        FilterRegistrationBean registration = new FilterRegistrationBean();
        registration.setFilter(sessionClientFilter);
        registration.addUrlPatterns("/api/clients/*");
        return registration;
    }

    @Bean
    public FilterRegistrationBean myFilter() {
        FilterRegistrationBean registration = new FilterRegistrationBean();
        registration.setFilter(sessionFilter);
        registration.addUrlPatterns("/api/*");
        return registration;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public Docket swaggerConfiguration(){
        return new Docket(DocumentationType.SWAGGER_2)
                .host("localhost:8080")
                .select()
                .paths(PathSelectors.ant("/api/backoffice/**"))
                //.paths(PathSelectors.ant("/api/clients/**/"))
                .apis(RequestHandlerSelectors.basePackage("com.phones.phones"))
                .build();
    }

}
