package org.shiloh.shiro.filter;

import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.web.servlet.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import java.io.IOException;

/**
 * 保证一次请求只会调用一次 {@link #doFilterInternal(ServletRequest, ServletResponse, FilterChain)}，
 * <p>
 * 即如内部的 forward 不会再多执行一次 {@link #doFilterInternal(ServletRequest, ServletResponse, FilterChain)}
 *
 * @author shiloh
 * @date 2023/3/4 16:41
 */
@Slf4j
@WebFilter(filterName = "myOncePerRequestFilter", urlPatterns = "/*")
public class MyOncePerRequestFilter extends OncePerRequestFilter {
    /**
     * Same contract as for
     * {@link #doFilter(ServletRequest, ServletResponse, FilterChain)},
     * but guaranteed to be invoked only once per request.
     *
     * @param request  incoming {@code ServletRequest}
     * @param response outgoing {@code ServletResponse}
     * @param chain    the {@code FilterChain} to execute
     * @throws ServletException if there is a problem processing the request
     * @throws IOException      if there is an I/O problem processing the request
     */
    @Override
    protected void doFilterInternal(ServletRequest request, ServletResponse response, FilterChain chain) throws ServletException, IOException {
        log.info("once per request filter");
        chain.doFilter(request, response);
    }
}
