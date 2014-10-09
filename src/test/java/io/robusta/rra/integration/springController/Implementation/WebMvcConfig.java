package io.robusta.rra.integration.springController.Implementation;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@EnableWebMvc
@ComponentScan(basePackages="io.robusta.rra")
@Configuration
public class WebMvcConfig extends WebMvcConfigurerAdapter {
}