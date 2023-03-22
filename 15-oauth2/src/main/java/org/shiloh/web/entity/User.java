package org.shiloh.web.entity;

import lombok.Data;
import org.springframework.jdbc.core.RowMapper;

import java.io.Serializable;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * 用户实体
 *
 * @author shiloh
 * @date 2023/3/19 14:18
 */
@Data
public class User implements Serializable, RowMapper<User> {
    private static final long serialVersionUID = 8843274037482403953L;

    /**
     * ID
     */
    private Long id;

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
     * 获取密码盐
     *
     * @author shiloh
     * @date 2023/3/19 14:23
     */
    public String getCredentialsSalt() {
        return this.username + this.salt;
    }

    @Override
    public User mapRow(ResultSet resultSet, int i) throws SQLException {
        final User user = new User();

        user.setId(resultSet.getLong("id"));
        user.setUsername(resultSet.getString("username"));
        user.setPassword(resultSet.getString("password"));
        user.setSalt(resultSet.getString("salt"));

        return user;
    }
}
