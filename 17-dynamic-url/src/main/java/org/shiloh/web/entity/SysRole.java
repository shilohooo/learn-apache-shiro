package org.shiloh.web.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.shiloh.web.entity.base.BaseEntity;

/**
 * 系统角色实体
 *
 * @author shiloh
 * @date 2023/4/1 23:23
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class SysRole extends BaseEntity {
    private static final long serialVersionUID = -5887319132521022506L;

    /**
     * 角色名称
     */
    private String name;

    /**
     * 上级角色 ID，顶级角色该值为 {@code null} / 0
     */
    private Long parentId;

    /**
     * 是否有效
     */
    private Boolean available = Boolean.TRUE;
}
