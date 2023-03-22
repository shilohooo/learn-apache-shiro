package org.shiloh.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 首页跳转
 *
 * @author shiloh
 * @date 2023/3/21 22:09
 */
@Controller
public class IndexController {

    /**
     * 跳转到首页
     *
     * @return 首页名称
     * @author shiloh
     * @date 2023/3/21 22:09
     */
    @RequestMapping("/")
    public String index() {
        return "index";
    }
}
