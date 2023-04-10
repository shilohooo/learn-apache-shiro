package org.shiloh.web.service;

import org.shiloh.web.entity.SysRole;

import java.util.List;

/**
 * 系统角色 Service
 *
 * @author shiloh
 * @date 2023/4/10 22:19
 */
public interface SysRoleService {
    /**
     * 新增角色
     *
     * @param sysRole 角色信息
     * @return 新增后带主键的角色信息
     * @author shiloh
     * @date 2023/4/10 22:19
     */
    SysRole add(SysRole sysRole);

    /**
     * 根据 ID 删除角色信息
     *
     * @param id ID
     * @author shiloh
     * @date 2023/4/10 22:19
     */
    void deleteById(Long id);

    /**
     * 根据 ID 修改角色信息
     *
     * @param sysRole 角色信息
     * @return 修改后的角色信息
     * @author shiloh
     * @date 2023/4/10 22:19
     */
    SysRole update(SysRole sysRole);

    /**
     * 根据 ID 查询角色信息
     *
     * @param id ID
     * @return 角色信息
     * @author shiloh
     * @date 2023/4/10 22:19
     */
    SysRole getById(Long id);

    /**
     * 查询所有角色信息
     *
     * @return 角色列表
     * @author shiloh
     * @date 2023/4/10 22:19
     */
    List<SysRole> getAll();
}
