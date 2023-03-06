package org.shiloh.service;

import org.shiloh.entity.Role;

/**
 * 角色信息 Service
 *
 * @author shiloh
 * @date 2023/3/1 16:36
 */
public interface RoleService {
    /**
     * 新增角色信息
     *
     * @param role 待新增的角色信息
     * @return 包含主键的角色实体
     * @author shiloh
     * @date 2023/2/28 18:29
     */
    Role add(Role role);

    /**
     * 根据 ID 删除角色信息
     *
     * @param id ID
     * @author shiloh
     * @date 2023/2/28 18:25
     */
    void deleteById(Long id);

    /**
     * 添加角色与权限的关联关系
     *
     * @param roleId        角色 ID
     * @param permissionIds 权限 ID 可变参数，一个或多个
     * @author shiloh
     * @date 2023/2/28 18:25
     */
    void addPermissionRefs(Long roleId, Long... permissionIds);

    /**
     * 移除角色与权限的关联关系
     *
     * @param roleId        角色 ID
     * @param permissionIds 权限 ID 可变参数，一个或多个
     * @author shiloh
     * @date 2023/2/28 18:25
     */
    void removePermissionRefs(Long roleId, Long... permissionIds);
}
