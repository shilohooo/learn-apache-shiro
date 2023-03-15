package org.shiloh;

import org.shiloh.web.ComponentScanConfig;
import org.springframework.web.WebApplicationInitializer;
import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.filter.DelegatingFilterProxy;
import org.springframework.web.servlet.DispatcherServlet;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRegistration;

/**
 * @author shiloh
 * @date 2023/3/12 11:39
 */
public class WebApp implements WebApplicationInitializer {
    @Override
    public void onStartup(ServletContext servletContext) throws ServletException {
        // 创建根上下文
        final AnnotationConfigWebApplicationContext rootContext = new AnnotationConfigWebApplicationContext();
        rootContext.register(ComponentScanConfig.class);
        // 生命周期管理
        servletContext.addListener(new ContextLoaderListener(rootContext));
        // 设置 DispatcherServlet 上下文
        rootContext.setServletContext(servletContext);
        // 注册 DispatcherServlet
        final ServletRegistration.Dynamic dispatcher = servletContext.addServlet("dispatcher", new DispatcherServlet(rootContext));
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
}
