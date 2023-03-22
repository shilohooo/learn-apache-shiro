package org.shiloh.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 客户端管理接口
 *
 * @author shiloh
 * @date 2023/3/21 22:12
 */
@Controller
@RequestMapping("/client")
public class ClientController {

    /**
     * 跳转到客户端管理页面
     *
     * @return 客户端管理页面
     * @author shiloh
     * @date 2023/3/21 22:13
     */
    @GetMapping
    public String list() {
        return "client/list";
    }
}
