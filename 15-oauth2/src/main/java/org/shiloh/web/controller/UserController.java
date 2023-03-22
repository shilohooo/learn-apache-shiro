package org.shiloh.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 用户管理接口
 *
 * @author shiloh
 * @date 2023/3/21 22:12
 */
@Controller
@RequestMapping("/user")
public class UserController {

    /**
     * 跳转到用户管理页面
     *
     * @return 用户管理页面
     * @author shiloh
     * @date 2023/3/21 22:13
     */
    @GetMapping
    public String list() {
        return "user/list";
    }
}
