package org.shiloh.web.service.impl;

import lombok.RequiredArgsConstructor;
import org.shiloh.web.dao.RoleDao;
import org.shiloh.web.entity.Role;
import org.shiloh.web.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

/**
 * 角色信息 Service Impl
 *
 * @author shiloh
 * @date 2023/3/1 16:39
 */
@Service
@RequiredArgsConstructor(onConstructor_ = {@Autowired, @Lazy})
public class RoleServiceImpl implements RoleService {
    /* ============================= INSTANCE FIELDS ============================== */

    private final RoleDao roleDao;

    /**
     * 新增角色信息
     *
     * @param role 待新增的角色信息
     * @return 包含主键的角色实体
     * @author shiloh
     * @date 2023/2/28 18:29
     */
    @Override
    public Role add(Role role) {
        return this.roleDao.add(role);
    }

    /**
     * 根据 ID 删除角色信息
     *
     * @param id ID
     * @author shiloh
     * @date 2023/2/28 18:25
     */
    @Override
    public void deleteById(Long id) {
        this.roleDao.deleteById(id);
    }

    /**
     * 添加角色与权限的关联关系
     *
     * @param roleId        角色 ID
     * @param permissionIds 权限 ID 可变参数，一个或多个
     * @author shiloh
     * @date 2023/2/28 18:25
     */
    @Override
    public void addPermissionRefs(Long roleId, Long... permissionIds) {
        this.roleDao.addPermissionRefs(roleId, permissionIds);
    }

    /**
     * 移除角色与权限的关联关系
     *
     * @param roleId        角色 ID
     * @param permissionIds 权限 ID 可变参数，一个或多个
     * @author shiloh
     * @date 2023/2/28 18:25
     */
    @Override
    public void removePermissionRefs(Long roleId, Long... permissionIds) {
        this.roleDao.removePermissionRefs(roleId, permissionIds);
    }
}
