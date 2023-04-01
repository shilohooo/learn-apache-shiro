package org.shiloh.web.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.shiloh.web.entity.base.BaseEntity;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * 系统用户实体
 *
 * @author shiloh
 * @date 2023/3/31 23:00
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class SysUser extends BaseEntity implements RowMapper<SysUser> {
    private static final long serialVersionUID = 9080132709488736239L;

    /**
     * 用户名
     */
    private String username;

    /**
     * 密码
     */
    private String password;

    /**
     * 盐
     */
    private String salt;

    /**
     * 帐号是否被锁定
     */
    private Boolean locked = Boolean.FALSE;

    /**
     * 获取用于加密密码的盐：username + salt
     *
     * @return 用于加密密码的盐
     * @author shiloh
     * @date 2023/3/31 23:02
     */
    public String getCredentialsSalt() {
        return this.username + this.salt;
    }

    /**
     * Spring Jdbc RowMapper 映射实现，将表中的行映射为数据
     *
     * @param rs     查询结果集
     * @param rowNum 当前行的行号
     * @author shiloh
     * @date 2023/3/31 23:03
     */
    @Override
    public SysUser mapRow(ResultSet rs, int rowNum) throws SQLException {
        final SysUser sysUser = new SysUser();
        sysUser.setId(rs.getLong("id"));
        sysUser.setUsername(rs.getString("username"));
        sysUser.setPassword(rs.getString("password"));
        sysUser.setSalt(rs.getString("salt"));
        sysUser.setLocked(rs.getBoolean("locked"));
        return sysUser;
    }
}
