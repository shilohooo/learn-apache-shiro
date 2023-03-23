package org.shiloh.web;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.http.MediaType;
import org.springframework.web.WebApplicationInitializer;
import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.filter.DelegatingFilterProxy;
import org.springframework.web.servlet.DispatcherServlet;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.view.InternalResourceViewResolver;
import org.springframework.web.servlet.view.JstlView;

import javax.servlet.ServletContext;
import javax.servlet.ServletRegistration;

/**
 * web 应用初始化
 *
 * @author shiloh
 * @date 2023/3/19 13:56
 */
@Configuration
@ComponentScan({"org.shiloh.web"})
@EnableAspectJAutoProxy
public class WebApp implements WebApplicationInitializer {
    /**
     * 创建应用上下文，添加前端控制器和 shiro 过滤器
     *
     * @param servletContext Servlet 上下文
     * @author shiloh
     * @date 2023/3/19 13:57
     */
    @Override
    public void onStartup(ServletContext servletContext) {
        // 创建根上下文
        final AnnotationConfigWebApplicationContext rootContext = new AnnotationConfigWebApplicationContext();
        rootContext.register(WebApp.class);
        // 生命周期管理
        servletContext.addListener(new ContextLoaderListener(rootContext));
        // 设置 DispatcherServlet 上下文
        rootContext.setServletContext(servletContext);
        // 注册 DispatcherServlet
        final ServletRegistration.Dynamic dispatcher = servletContext.addServlet(
                "dispatcher", new DispatcherServlet(rootContext)
        );
        // 设置初始化优先级，大于等于0
        dispatcher.setLoadOnStartup(1);
        // 映射路径
        dispatcher.addMapping("/");
        // 添加 Shiro 过滤器
        servletContext.addFilter(
                        "shiroFilter",
                        new DelegatingFilterProxy("shiroFilterFactoryBean", rootContext)
                )
                // 过滤器路径映射
                .addMappingForUrlPatterns(null, false, "/*");
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
