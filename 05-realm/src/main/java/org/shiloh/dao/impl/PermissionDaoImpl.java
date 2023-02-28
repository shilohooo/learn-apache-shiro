package org.shiloh.dao.impl;

import org.shiloh.dao.PermissionDao;
import org.shiloh.entity.Permission;
import org.springframework.jdbc.support.GeneratedKeyHolder;

/**
 * 权限信息 DAO Impl
 *
 * @author shiloh
 * @date 2023/2/28 18:32
 */
public class PermissionDaoImpl implements PermissionDao {
    /**
     * 新增 SQL
     */
    private static final String INSERT_SQL = "insert into learn_shiro.sys_permissions(permission, description, available) values(?, ?, ?)";

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
        // 用于获取新增后的主键
        final GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();

        return null;
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

    }
}
