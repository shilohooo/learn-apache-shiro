package org.shiloh.web.service;

import org.shiloh.web.entity.Client;

import java.util.List;

/**
 * 客户端管理 Service
 *
 * @author shiloh
 * @date 2023/3/20 21:59
 */
public interface ClientService {
    /**
     * 新增客户端信息
     *
     * @param client 客户端实体
     * @return 带主键的客户端实体
     * @author shiloh
     * @date 2023/3/20 22:01
     */
    Client create(Client client);

    /**
     * 根据 id 删除客户端信息
     *
     * @param id ID
     * @author shiloh
     * @date 2023/3/20 22:01
     */
    void deleteById(Long id);

    /**
     * 修改客户端信息
     *
     * @param client 客户端实体
     * @return 客户端实体
     * @author shiloh
     * @date 2023/3/20 22:02
     */
    Client update(Client client);

    /**
     * 根据 ID 查询客户端信息
     *
     * @param id ID
     * @return 客户端实体
     * @author shiloh
     * @date 2023/3/20 22:02
     */
    Client findById(Long id);

    /**
     * 查询所有客户端信息
     *
     * @return 客户端实体列表
     * @author shiloh
     * @date 2023/3/20 22:02
     */
    List<Client> findAll();

    /**
     * 根据 clientId 查询客户端信息
     *
     * @param clientId clientId
     * @return 客户端实体
     * @author shiloh
     * @date 2023/3/20 22:02
     */
    Client findByClientId(String clientId);

    /**
     * 根据 clientSecret 查询客户端信息
     *
     * @param clientSecret clientSecret
     * @return 客户端实体
     * @author shiloh
     * @date 2023/3/20 22:03
     */
    Client findByClientSecret(String clientSecret);
}
