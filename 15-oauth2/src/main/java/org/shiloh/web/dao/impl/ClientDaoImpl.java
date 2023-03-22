package org.shiloh.web.dao.impl;

import lombok.RequiredArgsConstructor;
import org.shiloh.web.dao.ClientDao;
import org.shiloh.web.entity.Client;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.util.List;
import java.util.Objects;

/**
 * Client Dao Impl
 *
 * @author shiloh
 * @date 2023/3/20 22:04
 */
@Repository
@RequiredArgsConstructor
public class ClientDaoImpl implements ClientDao {
    private final JdbcTemplate jdbcTemplate;

    /**
     * 新增客户端信息
     *
     * @param client 客户端实体
     * @return 带主键的客户端实体
     * @author shiloh
     * @date 2023/3/20 22:01
     */
    @Override
    public Client create(Client client) {
        final String sql = "insert into learn_shiro.oauth2_client(client_name, client_id, client_secret) values(?, ?, ?)";
        final GeneratedKeyHolder generatedKeyHolder = new GeneratedKeyHolder();
        this.jdbcTemplate.update(connection -> {
            final PreparedStatement statement = connection.prepareStatement(sql, new String[]{"id"});
            statement.setString(1, client.getClientName());
            statement.setString(2, client.getClientId());
            statement.setString(3, client.getClientSecret());
            return statement;
        }, generatedKeyHolder);

        client.setId(Objects.requireNonNull(generatedKeyHolder.getKey()).longValue());
        return client;
    }

    /**
     * 根据 ID 删除客户端信息
     *
     * @param id ID
     * @author shiloh
     * @date 2023/3/20 22:01
     */
    @Override
    public void deleteById(Long id) {
        final String sql = "delete from learn_shiro.oauth2_client where id = ?";
        this.jdbcTemplate.update(sql, id);
    }

    /**
     * 修改客户端信息
     *
     * @param client 客户端实体
     * @return 客户端实体
     * @author shiloh
     * @date 2023/3/20 22:02
     */
    @Override
    public Client update(Client client) {
        final String sql = "update learn_shiro.oauth2_client set client_name = ?, client_id = ?, client_secret = ? where id = ?";
        this.jdbcTemplate.update(
                sql, client.getClientName(), client.getClientId(), client.getClientSecret(), client.getId()
        );
        return client;
    }

    /**
     * 根据 ID 查询客户端信息
     *
     * @param id ID
     * @return 客户端实体
     * @author shiloh
     * @date 2023/3/20 22:02
     */
    @Override
    public Client findById(Long id) {
        try {
            final String sql = "select * from learn_shiro.oauth2_client where id = ?";
            return this.jdbcTemplate.queryForObject(sql, new Client(), id);
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    /**
     * 查询所有客户端信息
     *
     * @return 客户端实体列表
     * @author shiloh
     * @date 2023/3/20 22:02
     */
    @Override
    public List<Client> findAll() {
        final String sql = "select * from learn_shiro.oauth2_client";
        return this.jdbcTemplate.query(sql, new Client());
    }

    /**
     * 根据 clientId 查询客户端信息
     *
     * @param clientId clientId
     * @return 客户端实体
     * @author shiloh
     * @date 2023/3/20 22:02
     */
    @Override
    public Client findByClientId(String clientId) {
        try {
            final String sql = "select * from learn_shiro.oauth2_client where client_id = ?";
            return this.jdbcTemplate.queryForObject(sql, new Client(), clientId);
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    /**
     * 根据 clientSecret 查询客户端信息
     *
     * @param clientSecret clientSecret
     * @return 客户端实体
     * @author shiloh
     * @date 2023/3/20 22:03
     */
    @Override
    public Client findByClientSecret(String clientSecret) {
        try {
            final String sql = "select * from learn_shiro.oauth2_client where client_secret = ?";
            return this.jdbcTemplate.queryForObject(sql, new Client(), clientSecret);
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }
}
