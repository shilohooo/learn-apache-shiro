package org.shiloh.web.oauth2.shiro.filter;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.authc.AuthenticatingFilter;
import org.apache.shiro.web.util.WebUtils;
import org.shiloh.web.oauth2.shiro.token.OAuth2Token;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

/**
 * OAuth2客户端身份验证过滤器
 * <p>
 * 用于 oauth2 客户端的身份验证控制；
 * <p>
 * 如果当前用户还没有身份验证，首先会判断 url 中是否有 code（服务端返回的 auth code），
 * <p>
 * 如果没有则重定向到服务端进行登录并授权，然后返回 auth code；
 * <p>
 * 接着 OAuth2AuthenticationFilter 会用 auth code 创建 OAuth2Token，然后提交给 Subject.login 进行登录；
 * <p>
 * 接着 OAuth2Realm 会根据 OAuth2Token 进行相应的登录逻辑。
 *
 * @author shiloh
 * @date 2023/3/23 22:31
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Slf4j
public class OAuth2AuthenticationFilter extends AuthenticatingFilter {
    /**
     * OAuth2 授权码参数名称
     */
    private String authCodeParam = "code";

    /**
     * OAuth2 授权错误信息参数名称
     */
    private String errorParam = "error";

    /**
     * OAuth2 授权错误描述参数名称
     */
    private String errorDescParam = "errorDesc";

    /**
     * 客户端ID
     */
    private String clientId;

    /**
     * 服务端登录成功 / 失败后重定向到的客户端地址
     */
    private String redirectUrl;

    /**
     * OAuth2 服务器响应类型
     */
    private String responseType = "code";

    /**
     * 服务器返回错误后的重定向地址
     */
    private String failureUrl;

    /**
     * 创建 token
     *
     * @param request  当前请求
     * @param response 当前响应
     * @return 自定义的OAuth2 Token
     * @author shiloh
     * @date 2023/3/23 22:36
     */
    @Override
    protected AuthenticationToken createToken(ServletRequest request, ServletResponse response) throws Exception {
        final HttpServletRequest req = (HttpServletRequest) request;
        // 获取请求中的 code 参数
        final String authCode = req.getParameter(this.authCodeParam);
        log.info("OAuth2 身份验证 - 根据 code：{} 创建 token", authCode);
        return new OAuth2Token(authCode);
    }

    /**
     * Determines whether the current subject should be allowed to make the current request.
     * <p/>
     * The default implementation returns <code>true</code> if the user is authenticated.  Will also return
     * <code>true</code> if the {@link #isLoginRequest} returns false and the &quot;permissive&quot; flag is set.
     *
     * @param request     当前请求
     * @param response    当前响应
     * @param mappedValue url 配置参数
     * @return <code>true</code> if request should be allowed access
     */
    @Override
    protected boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue) {
        log.info("OAuth2AuthenticationFilter.isAccessAllowed");
        return true;
    }

    /**
     * Processes requests where the subject was denied access as determined by the
     * {@link #isAccessAllowed(ServletRequest, ServletResponse, Object) isAccessAllowed}
     * method.
     *
     * @param request  the incoming <code>ServletRequest</code>
     * @param response the outgoing <code>ServletResponse</code>
     * @return <code>true</code> if the request should continue to be processed; false if the subclass will
     * handle/render the response directly.
     * @throws Exception if there is an error processing the request.
     */
    @Override
    protected boolean onAccessDenied(ServletRequest request, ServletResponse response) throws Exception {
        // 获取错误信息
        final HttpServletRequest req = (HttpServletRequest) request;
        final String error = req.getParameter(this.errorParam);
        final String errorDesc = req.getParameter(this.errorDescParam);
        if (StringUtils.isNoneBlank(error)) {
            // 如果服务端返回了错误
            final String errRedirectUrl = String.format(
                    "%s?%s=%s&%s=%s",
                    this.failureUrl, this.errorParam, error, this.errorDescParam, errorDesc
            );
            WebUtils.issueRedirect(request, response, errRedirectUrl);
            return false;
        }
        // 获取 Subject 对象
        final Subject subject = this.getSubject(request, response);
        // 判断是否登录
        if (!subject.isAuthenticated()) {
            // 没有进行身份验证，判断是否有授权码，如果没有则重定向到服务端授权
            if (StringUtils.isBlank(req.getParameter(this.authCodeParam))) {
                this.saveRequestAndRedirectToLogin(request, response);
                return false;
            }
        }
        // 执行登录操作
        return this.executeLogin(request, response);
    }

    /**
     * 登录成功后，重定向到成功页面
     *
     * @param token    token
     * @param subject  subject
     * @param request  当前请求
     * @param response 当前响应
     * @author shiloh
     * @date 2023/3/23 22:47
     */
    @Override
    protected boolean onLoginSuccess(AuthenticationToken token, Subject subject, ServletRequest request, ServletResponse response) throws Exception {
        log.info("OAuth2 身份验证 - 登录成功，准备重定向到成功页面");
        this.issueSuccessRedirect(request, response);
        return false;
    }

    /**
     * 登录失败后，重定向到失败页面
     *
     * @param token    token
     * @param e        身份验证异常
     * @param request  当前请求
     * @param response 当前响应
     * @author shiloh
     * @date 2023/3/23 22:48
     */
    @Override
    protected boolean onLoginFailure(AuthenticationToken token, AuthenticationException e, ServletRequest request, ServletResponse response) {
        log.warn("OAuth2 身份验证 - 登录失败，准备重定向到失败页面");
        // 获取 Subject
        final Subject subject = this.getSubject(request, response);
        // 判断是否已登录
        try {
            if (subject.isAuthenticated() || subject.isRemembered()) {
                this.issueSuccessRedirect(request, response);
            } else {
                WebUtils.issueRedirect(request, response, this.failureUrl);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return false;
    }
}
