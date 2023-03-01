package org.shiloh.service.impl;

import org.shiloh.dao.PermissionDao;
import org.shiloh.dao.impl.PermissionDaoImpl;
import org.shiloh.entity.Permission;
import org.shiloh.service.PermissionService;

/**
 * 权限信息 Service Impl
 *
 * @author shiloh
 * @date 2023/3/1 16:38
 */
public class PermissionServiceImpl implements PermissionService {
    /* ============================= INSTANCE FIELDS ============================== */

    private final PermissionDao permissionDao;

    /* ============================= CONSTRUCTORS ============================== */

    public PermissionServiceImpl() {
        this.permissionDao = new PermissionDaoImpl();
    }

    /**
     * 新增权限信息
     *
     * @param permission 待新增的权限信息
     * @return 包含主键的权限实体
     * @author shiloh
     * @date 2023/2/28 18:31
     */
    @Override
    public Permission add(Permission permission) {
        return this.permissionDao.add(permission);
    }

    /**
     * 根据 ID 删除权限信息
     *
     * @param id ID
     * @author shiloh
     * @date 2023/2/28 18:25
     */
    @Override
    public void deleteById(Long id) {
        this.permissionDao.deleteById(id);
    }
}
