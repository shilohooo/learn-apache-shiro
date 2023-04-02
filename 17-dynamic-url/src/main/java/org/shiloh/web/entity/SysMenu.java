package org.shiloh.web.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.shiloh.web.entity.base.BaseEntity;

/**
 * 系统菜单实体
 *
 * @author shiloh
 * @date 2023/4/2 23:05
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class SysMenu extends BaseEntity {
    private static final long serialVersionUID = 3409519371416417862L;

    /**
     * 菜单名称
     */
    private String name;

    /**
     * 菜单类型：
     * <ul>
     *     <li>0 = 目录</li>
     *     <li>1 = 菜单</li>
     *     <li>2 = 按钮</li>
     * </ul>
     */
    private Integer type;

    /**
     * 排序号
     */
    private Integer orderNo;

    /**
     * 路由 / iframe 页面地址
     */
    private String url;

    /**
     * 上级菜单 ID，顶层菜单该值为 {@code null} / 0
     */
    private Long parentId;

    /**
     * 权限字符串
     */
    private String permission;

    /**
     * 是否有效
     */
    private Boolean available = Boolean.TRUE;

    /**
     * 判断该菜单是否为顶层菜单
     *
     * @return 如果该菜单是顶层菜单则返回 {@code true}，否则返回 {@code false}
     * @author shiloh
     * @date 2023/4/2 23:09
     */
    public Boolean isRootNode() {
        return this.parentId == null || this.parentId.equals(0L);
    }
}
