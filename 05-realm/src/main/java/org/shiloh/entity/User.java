package org.shiloh.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.shiloh.entity.base.BaseEntity;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Objects;

/**
 * 用户实体
 *
 * @author shiloh
 * @date 2023/2/28 17:45
 */
@Setter
@Getter
@ToString(callSuper = true)
public class User extends BaseEntity implements RowMapper<User> {
    private static final long serialVersionUID = 6201531861329400346L;

    /**
     * 用户名
     */
    private String username;

    /**
     * 密码
     */
    private String password;

    /**
     * 密码盐
     */
    private String salt;

    /**
     * 帐号是否被锁定，默认为：{@link Boolean#FALSE}
     */
    private Boolean locked = Boolean.FALSE;

    /**
     * 获取用于加密密码的盐
     *
     * @return 用于加密密码的盐
     * @author shiloh
     * @date 2023/2/28 18:14
     */
    public String getCredentialsSalt() {
        return this.username + this.salt;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        if (!super.equals(o)) {
            return false;
        }
        final User user = (User) o;
        return Objects.equals(getId(), user.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), getId());
    }

    /**
     * {@link org.springframework.jdbc.core.JdbcTemplate} 查询映射
     *
     * @param resultSet 查询结果集
     * @param i         当前下标
     * @return 用户信息实体
     * @author shiloh
     * @date 2023/2/28 18:17
     */
    @Override
    public User mapRow(ResultSet resultSet, int i) throws SQLException {
        final User user = new User();
        user.setId(resultSet.getLong("id"));
        user.setUsername(resultSet.getString("username"));
        user.setPassword(resultSet.getString("password"));
        user.setSalt(resultSet.getString("salt"));
        user.setLocked(resultSet.getBoolean("locked"));
        return user;
    }
}
