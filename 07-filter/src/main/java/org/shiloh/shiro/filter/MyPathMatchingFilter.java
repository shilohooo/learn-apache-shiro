package org.shiloh.shiro.filter;

import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.web.filter.PathMatchingFilter;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import java.util.Arrays;

/**
 * {@link PathMatchingFilter} 提供了基于 Ant 风格的请求路径匹配功能以及拦截器参数解析功能，如：
 * <p>
 * "roles[admin, user]"，自动根据逗号 "," 分隔解析到一个路径参数并绑定到相应的路径
 *
 * @author shiloh
 * @date 2023/3/4 19:20
 */
@Slf4j
@WebFilter(filterName = "myPathMatchingFilter", urlPatterns = "/*")
public class MyPathMatchingFilter extends PathMatchingFilter {
    /**
     * 在 {@link #preHandle(ServletRequest, ServletResponse)} 中，当 {@link #pathsMatch(String, ServletRequest)}匹配到一个
     * 路径后，会调用此方法并将路径绑定参数配置传给 mappedValue 参数，然后可以在这个方法中进行一些验证（如：角色授权）
     * <p>
     * 如果验证失败可以返回 {@code false} 中断流程，默认返回 {@code true}
     * <p>
     * 如果没有 path 与请求路径匹配，默认是通过的（即 {@link #preHandle(ServletRequest, ServletResponse)} 返回 {@code true}）
     *
     * @param request     当前请求对象
     * @param response    当前响应对象
     * @param mappedValue 匹配的路径绑定参数配置
     * @author shiloh
     * @date 2023/3/4 19:24
     */
    @Override
    protected boolean onPreHandle(ServletRequest request, ServletResponse response, Object mappedValue) throws Exception {
        log.info("MyPathMatchingFilter.onPreHandle");
        log.info("path 与请求路径匹配，配置参数为：{}", Arrays.toString(((String[]) mappedValue)));
        // return super.onPreHandle(request, response, mappedValue);
        return true;
    }
}
