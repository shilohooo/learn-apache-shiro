package org.shiloh.web.entity;

import lombok.Data;
import org.springframework.jdbc.core.RowMapper;

import java.io.Serializable;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * OAuth2 客户端实体
 *
 * @author shiloh
 * @date 2023/3/19 14:24
 */
@Data
public class Client implements Serializable, RowMapper<Client> {
    private static final long serialVersionUID = -3065469236460634609L;

    /**
     * ID
     */
    private Long id;

    /**
     * 客户端名称
     */
    private String clientName;

    /**
     * 客户端ID
     */
    private String clientId;

    /**
     * 客户端密钥
     */
    private String clientSecret;

    @Override
    public Client mapRow(ResultSet resultSet, int i) throws SQLException {
        final Client client = new Client();

        client.setId(resultSet.getLong("id"));
        client.setClientName(resultSet.getString("client_name"));
        client.setClientId(resultSet.getString("client_id"));
        client.setClientSecret(resultSet.getString("client_secret"));

        return client;
    }
}
