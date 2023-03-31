package org.shiloh;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.http.MediaType;
import org.springframework.web.WebApplicationInitializer;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.view.InternalResourceViewResolver;
import org.springframework.web.servlet.view.JstlView;

import javax.servlet.ServletContext;
import javax.servlet.ServletRegistration;

/**
 * @author shiloh
 * @date 2023/3/12 11:39
 */
@Configuration
@ComponentScan(
        basePackages = {"org.shiloh.web"},
        includeFilters = {@ComponentScan.Filter(classes = {ControllerAdvice.class})}
)
@EnableWebMvc
@EnableAspectJAutoProxy
public class WebApp implements WebApplicationInitializer {
    @Override
    public void onStartup(ServletContext servletContext) {
        // 创建根上下文
        final AnnotationConfigWebApplicationContext rootContext = new AnnotationConfigWebApplicationContext();
        // 生命周期管理
        servletContext.addListener(new ContextLoaderListener(rootContext));
        // 设置 DispatcherServlet 上下文
        rootContext.setServletContext(servletContext);
        rootContext.register(WebApp.class);
        rootContext.refresh();
        // 注册 DispatcherServlet
        final ServletRegistration.Dynamic dispatcher = servletContext.addServlet("dispatcher", new DispatcherServlet(rootContext));
        // 设置初始化优先级，大于等于0
        dispatcher.setLoadOnStartup(1);
        // 映射路径
        dispatcher.addMapping("/");
        // 添加 Shiro 过滤器
        // servletContext.addFilter(
        //                 "shiroFilter",
        //                 new DelegatingFilterProxy("shiroFilterFactoryBean", rootContext)
        //         )
        //         // 过滤器路径映射
        //         .addMappingForUrlPatterns(null, false, "/*");
    }

    /**
     * 视图解析器配置
     * @return {@link InternalResourceViewResolver}
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
