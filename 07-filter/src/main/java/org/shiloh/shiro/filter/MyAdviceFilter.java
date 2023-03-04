package org.shiloh.shiro.filter;

import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.web.servlet.AdviceFilter;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;

/**
 * 提供 AOP 功能的过滤器：{@link AdviceFilter}
 * <p>
 * 它的实现类似于 SpringMvc 的拦截器
 *
 * @author shiloh
 * @date 2023/3/4 19:05
 */
@Slf4j
@WebFilter(filterName = "myAdviceFilter", urlPatterns = "/*")
public class MyAdviceFilter extends AdviceFilter {
    /**
     * 请求预处理，根据返回值决定是否继续处理，返回 {@code true} 代表继续执行过滤器链
     * <p>
     * 可以通过该方法实现权限控制，如果用户没有权限访问资源，则无需继续执行
     * <p>
     * 该方法类似 AOP 中的前置增强，在拦截器链执行前执行
     *
     * @param request  当前请求对象
     * @param response 当前响应对象
     * @return 需要继续处理则返回 {@code true}，否则返回 {@code false}
     * @author shiloh
     * @date 2023/3/4 19:06
     */
    @Override
    protected boolean preHandle(ServletRequest request, ServletResponse response) throws Exception {
        log.info("MyAdviceFilter.preHandle");
        // return super.preHandle(request, response);
        // 返回 false 表示不继续往下走了
        return true;
    }

    /**
     * 类似于 AOP 中的后置增强，在拦截器链执行完成后执行，进行后置处理，比如：记录执行时间
     *
     * @param request  当前请求对象
     * @param response 当前响应对象
     * @author shiloh
     * @date 2023/3/4 19:10
     */
    @Override
    protected void postHandle(ServletRequest request, ServletResponse response) throws Exception {
        log.info("MyAdviceFilter.postHandle");
        super.postHandle(request, response);
    }

    /**
     * 类似于 AOP 中的后置最终增强，即不管有没有异常都会执行，可以在此处进行资源清理，
     * <p>
     * 比如解除 {@link org.apache.shiro.subject.Subject} 实例与线程的绑定
     *
     * @param request   当前请求对象
     * @param response  当前响应对象
     * @param exception 异常对象
     * @author shiloh
     * @date 2023/3/4 19:11
     */
    @Override
    public void afterCompletion(ServletRequest request, ServletResponse response, Exception exception) throws Exception {
        log.info("MyAdviceFilter.afterCompletion");
        super.afterCompletion(request, response, exception);
    }
}
