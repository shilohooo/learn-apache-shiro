package org.shiloh.shiro.filter;

import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.web.filter.AccessControlFilter;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;

/**
 * 自定义访问控制 Filter
 *
 * @author shiloh
 * @date 2023/3/4 19:34
 */
@Slf4j
@WebFilter(filterName = "myAccessControlFilter", urlPatterns = "/*")
public class MyAccessControlFilter extends AccessControlFilter {
    /**
     * 是否允许访问，如果是则返回 {@code true}
     *
     * @param request     当前请求对象
     * @param response    当前响应对象
     * @param mappedValue 匹配的 path 配置的参数
     * @author shiloh
     * @date 2023/3/4 19:35
     */
    @Override
    protected boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue) throws Exception {
        log.info("允许访问~~~");
        return true;
    }

    /**
     * 当访问拒绝时，是否需要自行处理：
     * <p>
     * 如果返回 {@code true} 表示自己不处理且继续拦截器链执行，返回 {@code false} 表示自行处理
     *
     * @param request  当前请求对象
     * @param response 当前响应对象
     * @return 不需要自行处理则返回 {@code true}，否则返回 {@code false} 自行处理访问拒绝的情况
     * @author shiloh
     * @date 2023/3/4 19:36
     */
    @Override
    protected boolean onAccessDenied(ServletRequest request, ServletResponse response) throws Exception {
        log.info("访问拒绝时不自行处理，继续拦截器链的执行");
        return true;
    }
}
