package org.shiloh.web.service.impl;

import lombok.RequiredArgsConstructor;
import org.shiloh.web.dao.SysRoleDao;
import org.shiloh.web.entity.SysRole;
import org.shiloh.web.service.SysRoleService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 系统角色 Service Impl
 *
 * @author shiloh
 * @date 2023/4/10 22:19
 */
@Service
@RequiredArgsConstructor
public class SysRoleServiceImpl implements SysRoleService {
    private final SysRoleDao sysRoleDao;

    /**
     * 新增角色
     *
     * @param sysRole 角色信息
     * @return 新增后带主键的角色信息
     * @author shiloh
     * @date 2023/4/10 22:19
     */
    @Override
    public SysRole add(SysRole sysRole) {
        return this.sysRoleDao.add(sysRole);
    }

    /**
     * 根据 ID 删除角色信息
     *
     * @param id ID
     * @author shiloh
     * @date 2023/4/10 22:19
     */
    @Override
    public void deleteById(Long id) {
        this.sysRoleDao.deleteById(id);
    }

    /**
     * 根据 ID 修改角色信息
     *
     * @param sysRole 角色信息
     * @return 修改后的角色信息
     * @author shiloh
     * @date 2023/4/10 22:19
     */
    @Override
    public SysRole update(SysRole sysRole) {
        return this.sysRoleDao.update(sysRole);
    }

    /**
     * 根据 ID 查询角色信息
     *
     * @param id ID
     * @return 角色信息
     * @author shiloh
     * @date 2023/4/10 22:19
     */
    @Override
    public SysRole getById(Long id) {
        return this.sysRoleDao.findById(id);
    }

    /**
     * 查询所有角色信息
     *
     * @return 角色列表
     * @author shiloh
     * @date 2023/4/10 22:19
     */
    @Override
    public List<SysRole> getAll() {
        return this.sysRoleDao.findAll();
    }
}
