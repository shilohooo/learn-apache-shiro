package org.shiloh.web.service;

import org.shiloh.web.entity.Permission;

/**
 * 权限信息 Service
 *
 * @author shiloh
 * @date 2023/3/1 16:35
 */
public interface PermissionService {
    /**
     * 新增权限信息
     *
     * @param permission 待新增的权限信息
     * @return 包含主键的权限实体
     * @author shiloh
     * @date 2023/2/28 18:31
     */
    Permission add(Permission permission);

    /**
     * 根据 ID 删除权限信息
     *
     * @param id ID
     * @author shiloh
     * @date 2023/2/28 18:25
     */
    void deleteById(Long id);
}
