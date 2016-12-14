package cw3.project;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.view.InternalResourceViewResolver;
import org.springframework.web.servlet.view.JstlView;


@Configuration
public class WebConfig extends WebMvcConfigurerAdapter {
    // Handles HTTP GET requests for /resources/** 
    // by efficiently serving up static 
    // resources in the ${webappRoot}/resources/ directory
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/resources/**")
            .addResourceLocations("/resources/");
    }

    /**Spring annotation.
     */
    @Bean
    /**Enables use of JSP files.
     * @return spring variable
     */
    public InternalResourceViewResolver viewResolver() {
        InternalResourceViewResolver viewResolver = 
                new InternalResourceViewResolver();
        viewResolver.setViewClass(JstlView.class);
        viewResolver.setPrefix("/WEB-INF/views/");
        viewResolver.setSuffix(".jsp");
        viewResolver.setOrder(2);
        return viewResolver;
    }
}

