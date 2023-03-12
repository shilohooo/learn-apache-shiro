package org.shiloh.web.mvc.controller;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * @author shiloh
 * @date 2023/3/12 14:03
 */
@Controller
public class HelloController {
    /**
     * 权限校验测试
     *
     * @author shiloh
     * @date 2023/3/12 14:05
     */
    @GetMapping("/hello1")
    public String hello1() {
        SecurityUtils.getSubject().checkRole("admin");
        return "success";
    }

    /**
     * 权限校验注解测试
     *
     * @author shiloh
     * @date 2023/3/12 14:05
     */
    @GetMapping("/hello2")
    @RequiresRoles("admin")
    public String hello2() {
        return "success";
    }
}
