package org.shiloh.web.service.impl;

import lombok.RequiredArgsConstructor;
import org.shiloh.web.dao.ClientDao;
import org.shiloh.web.entity.Client;
import org.shiloh.web.service.ClientService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

/**
 * 客户端管理 Service Impl
 *
 * @author shiloh
 * @date 2023/3/20 22:41
 */
@Service
@RequiredArgsConstructor
public class ClientServiceImpl implements ClientService {
    private final ClientDao clientDao;

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
        // 使用随机的UUID作为新增客户端的 clientId 和 clientSecret
        client.setClientId(UUID.randomUUID().toString());
        client.setClientSecret(UUID.randomUUID().toString());
        return this.clientDao.create(client);
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
        this.clientDao.deleteById(id);
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
        return this.clientDao.update(client);
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
        return this.clientDao.findById(id);
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
        return this.clientDao.findAll();
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
        return this.clientDao.findByClientId(clientId);
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
        return this.clientDao.findByClientSecret(clientSecret);
    }
}
