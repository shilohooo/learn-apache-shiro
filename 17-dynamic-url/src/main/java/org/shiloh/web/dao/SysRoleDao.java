package org.shiloh.web.dao;

import org.shiloh.web.entity.SysRole;

import java.util.List;

/**
 * 系统角色 DAO
 *
 * @author shiloh
 * @date 2023/4/1 23:25
 */
public interface SysRoleDao {
    /**
     * 新增角色
     *
     * @param sysRole 角色信息
     * @return 新增后带主键的角色信息
     * @author shiloh
     * @date 2023/4/1 23:28
     */
    SysRole add(SysRole sysRole);

    /**
     * 根据 ID 删除角色信息
     *
     * @param id ID
     * @author shiloh
     * @date 2023/4/1 23:28
     */
    void deleteById(Long id);

    /**
     * 根据 ID 修改角色信息
     *
     * @param sysRole 角色信息
     * @return 修改后的角色信息
     * @author shiloh
     * @date 2023/4/1 23:28
     */
    SysRole update(SysRole sysRole);

    /**
     * 根据 ID 查询角色信息
     *
     * @param id ID
     * @return 角色信息
     * @author shiloh
     * @date 2023/4/1 23:29
     */
    SysRole findById(Long id);

    /**
     * 查询所有角色信息
     *
     * @return 角色列表
     * @author shiloh
     * @date 2023/4/1 23:29
     */
    List<SysRole> findAll();
}
