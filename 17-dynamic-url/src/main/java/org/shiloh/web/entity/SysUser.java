package org.shiloh.web.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.shiloh.web.entity.base.BaseEntity;

/**
 * 系统用户实体
 *
 * @author shiloh
 * @date 2023/3/31 23:00
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class SysUser extends BaseEntity {
    private static final long serialVersionUID = 9080132709488736239L;

    /**
     * 用户名
     */
    private String username;

    /**
     * 密码
     */
    private String password;

    /**
     * 盐
     */
    private String salt;

    /**
     * 帐号是否被锁定
     */
    private Boolean locked = Boolean.FALSE;
}
