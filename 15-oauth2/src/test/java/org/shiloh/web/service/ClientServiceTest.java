package org.shiloh.web.service;

import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.shiloh.web.WebApp;
import org.shiloh.web.entity.Client;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = WebApp.class)
public class ClientServiceTest {
    @Autowired
    private ClientService clientService;

    /**
     * 新增客户端测试
     *
     * @author shiloh
     * @date 2023/3/20 23:15
     */
    @Test
    public void create() {
        Client client = new Client();
        client.setClientName("myClientApp01");
        client = this.clientService.create(client);
        Assertions.assertThat(client).isNotNull();
        Assertions.assertThat(client.getId()).isNotNull();

        client = this.clientService.findById(client.getId());
        Assertions.assertThat(client).isNotNull();
        Assertions.assertThat(client.getClientId()).isNotBlank();
        Assertions.assertThat(client.getClientSecret()).isNotBlank();
    }

    /**
     * 根据 ID 删除客户端测试
     *
     * @author shiloh
     * @date 2023/3/20 23:15
     */
    @Test
    public void deleteById() {
        final long id = 1L;
        this.clientService.deleteById(id);

        final Client client = this.clientService.findById(id);
        Assertions.assertThat(client).isNull();
    }

    /**
     * 修改客户端信息测试
     *
     * @author shiloh
     * @date 2023/3/20 23:15
     */
    @Test
    public void update() {
        final long id = 3L;
        Client client = this.clientService.findById(id);
        Assertions.assertThat(client).isNotNull();

        final String clientName = "OAuth2-client-app-01";
        client.setClientName(clientName);
        client = this.clientService.update(client);
        Assertions.assertThat(client).isNotNull();

        client = this.clientService.findById(id);
        Assertions.assertThat(clientName).isEqualTo(client.getClientName());
    }

    /**
     * 根据 ID 查询客户端测试
     *
     * @author shiloh
     * @date 2023/3/20 23:15
     */
    @Test
    public void findById() {
        final Client client = this.clientService.findById(3L);
        Assertions.assertThat(client).isNotNull();
    }

    /**
     * 查询所有客户端信息测试
     *
     * @author shiloh
     * @date 2023/3/20 23:15
     */
    @Test
    public void findAll() {
        final List<Client> clients = this.clientService.findAll();
        Assertions.assertThat(clients).isNotEmpty();
    }

    /**
     * 根据 clientId 查询客户端测试
     *
     * @author shiloh
     * @date 2023/3/20 23:15
     */
    @Test
    public void findByClientId() {
        final String clientId = "44f01332-ba92-4e99-9abc-f1bf33ee2cce";
        final Client client = this.clientService.findByClientId(clientId);
        Assertions.assertThat(client).isNotNull();
    }

    /**
     * 根据 clientSecret 查询客户端测试
     *
     * @author shiloh
     * @date 2023/3/20 23:15
     */
    @Test
    public void findByClientSecret() {
        final String clientSecret = "7134d6e3-d5cc-4dce-9bc5-68e95b5dad48";
        final Client client = this.clientService.findByClientSecret(clientSecret);
        Assertions.assertThat(client).isNotNull();
    }
}