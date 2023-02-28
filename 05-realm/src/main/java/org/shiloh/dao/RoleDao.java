package org.shiloh.dao;

import org.shiloh.entity.Role;

/**
 * 角色信息 DAO
 *
 * @author shiloh
 * @date 2023/2/28 18:27
 */
public interface RoleDao {
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

    /**
     * 根据角色 ID 和权限 ID 查询角色与权限的关联关系是否存在
     *
     * @param roleId       角色 ID
     * @param permissionId 权限 ID
     * @return 如果角色与权限的关联关系存在则返回 {@code true}，否则返回 {@code false}
     * @author shiloh
     * @date 2023/2/28 22:46
     */
    Boolean isRolePermissionRefExists(Long roleId, Long permissionId);
}
