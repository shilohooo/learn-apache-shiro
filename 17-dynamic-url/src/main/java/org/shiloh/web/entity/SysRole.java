package org.shiloh.web.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.shiloh.web.entity.base.BaseEntity;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * 系统角色实体
 *
 * @author shiloh
 * @date 2023/4/1 23:23
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class SysRole extends BaseEntity implements RowMapper<SysRole> {
    private static final long serialVersionUID = -5887319132521022506L;

    /**
     * 角色名称
     */
    private String name;

    /**
     * 上级角色 ID，顶级角色该值为 {@code null} / 0
     */
    private Long parentId;

    /**
     * 是否有效
     */
    private Boolean available = Boolean.TRUE;

    /**
     * Spring Jdbc RowMapper 映射实现，将表中的行映射为数据
     *
     * @param rs     查询结果集
     * @param rowNum 当前行的行号
     * @author shiloh
     * @date 2023/4/1 23:23
     */
    @Override
    public SysRole mapRow(ResultSet rs, int rowNum) throws SQLException {
        final SysRole sysRole = new SysRole();
        sysRole.setId(rs.getLong("id"));
        sysRole.setName(rs.getString("name"));
        sysRole.setParentId(rs.getLong("parent_id"));
        sysRole.setAvailable(rs.getBoolean("available"));
        return sysRole;
    }
}
