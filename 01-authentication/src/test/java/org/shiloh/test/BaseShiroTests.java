package org.shiloh.test;

import org.apache.shiro.util.ThreadContext;
import org.junit.After;
import org.slf4j.Logger;

import static org.slf4j.LoggerFactory.getLogger;

/**
 * @author shiloh
 * @date 2022/1/28 14:57
 */
public class BaseShiroTests {
    protected final Logger LOG = getLogger(BaseShiroTests.class);

    /**
     * 测试完成后解除绑定Subject到线程，避免对下次测试造成影响
     *
     * @author shiloh
     * @date 2022/1/28 14:34
     */
    @After
    public void unbindSubject() {
        LOG.info("清除当前线程中的Subject信息...");
        ThreadContext.unbindSubject();
    }
}
