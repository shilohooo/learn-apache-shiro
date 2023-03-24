package org.shiloh.web;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.view.InternalResourceViewResolver;
import org.springframework.web.servlet.view.JstlView;

/**
 * @author shiloh
 * @date 2023/3/8 17:55
 */
@Configuration
@ComponentScan(
        basePackages = {"org.shiloh.web"},
        includeFilters = {@ComponentScan.Filter(classes = {ControllerAdvice.class})}
)
@EnableWebMvc
@EnableAspectJAutoProxy
public class ComponentScanConfig {
    /**
     * 视图解析器配置
     * @return {@link org.springframework.web.servlet.view.InternalResourceViewResolver}
     * @author shiloh
     * @date 2023/3/12 11:21
     */
    @Bean
    public ViewResolver defaultViewResolver() {
        final InternalResourceViewResolver viewResolver = new InternalResourceViewResolver();
        viewResolver.setViewClass(JstlView.class);
        viewResolver.setContentType(MediaType.TEXT_HTML.getType());
        viewResolver.setPrefix("/");
        // 默认使用 html，这里改为 jsp
        viewResolver.setSuffix(".jsp");
        return viewResolver;
    }
}
