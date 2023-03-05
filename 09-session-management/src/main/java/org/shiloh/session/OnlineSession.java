package org.shiloh.session;

import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.session.mgt.SimpleSession;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 * 自定义 Session 信息
 * <p>
 * 用于保存当前登录用户的在线状态，支持如离线等状态的控制。
 *
 * @author shiloh
 * @date 2023/3/5 21:33
 */
@Setter
@Getter
public class OnlineSession extends SimpleSession {
    private static final long serialVersionUID = -3065346370523048442L;

    private static int bitCountIndex = 0;

    /* ========================= CONSTANT ========================== */

    private static final int USER_AGENT_BIT_MASK = 1 << bitCountIndex++;

    private static final int STATUS_BIT_MASK = 1 << bitCountIndex++;

    /* ========================= INSTANCE FIELDS ========================== */

    /**
     * 用户浏览器类型
     */
    private String userAgent;

    /**
     * 状态
     */
    private OnlineStatus status = OnlineStatus.ONLINE;

    /**
     * 用户登录系统时的 Host
     */
    private String sysHost;

    /**
     * 属性是否改变，用于优化 Session 数据同步
     */
    private transient boolean attrChanged = false;

    /* ========================= INSTANCE METHODS ========================== */

    /**
     * 标识属性已改变
     *
     * @author shiloh
     * @date 2023/3/5 21:38
     */
    public void markAttrChange() {
        this.attrChanged = true;
    }

    /**
     * 重置属性已改变标识
     *
     * @author shiloh
     * @date 2023/3/5 21:39
     */
    public void resetAttrChanged() {
        this.attrChanged = false;
    }

    @Override
    public void setAttribute(Object key, Object value) {
        super.setAttribute(key, value);
    }

    @Override
    public Object getAttribute(Object key) {
        return super.getAttribute(key);
    }

    /**
     * 将对象序列化到指定的输出流
     *
     * @param outputStream 对象输出流
     * @author shiloh
     * @date 2023/3/5 21:52
     */
    private void writeObject(ObjectOutputStream outputStream) throws IOException {
        outputStream.defaultWriteObject();
        final short bitMask = this.getAlteredFieldsBitMask();
        outputStream.writeShort(bitMask);
        if (StringUtils.isNoneBlank(this.userAgent)) {
            outputStream.writeObject(this.userAgent);
        }
        if (this.status != null) {
            outputStream.writeObject(this.status);
        }
    }

    /**
     * 从给定的输入流中读取对象数据
     *
     * @param inputStream 对象输入流
     * @author shiloh
     * @date 2023/3/5 21:54
     */
    private void readObject(ObjectInputStream inputStream) throws IOException, ClassNotFoundException {
        inputStream.defaultReadObject();
        final short bitMask = inputStream.readShort();
        if (isFieldPresent(bitMask, USER_AGENT_BIT_MASK)) {
            this.userAgent = (String) inputStream.readObject();
        }
        if (isFieldPresent(bitMask, STATUS_BIT_MASK)) {
            this.status = (OnlineStatus) inputStream.readObject();
        }
    }

    /**
     * 获取字段位掩码，用于标识字段是否序列化，如果是则位掩码应为：1
     *
     * @return 字段位掩码
     * @author shiloh
     * @date 2023/3/5 21:51
     */
    private short getAlteredFieldsBitMask() {
        int bigMask = 0;
        bigMask = StringUtils.isNoneBlank(this.userAgent) ? bigMask | USER_AGENT_BIT_MASK : bigMask;
        bigMask = this.status != null ? bigMask | STATUS_BIT_MASK : bigMask;
        return (short) bigMask;
    }

    /**
     * 用于判断字段是否序列化
     *
     * @param bitMask      位掩码
     * @param fieldBitMask 字段位掩码
     * @return {@code true} 代表字段序列化，否则返回 {@code false}
     * @author shiloh
     * @date 2023/3/5 21:50
     */
    private static boolean isFieldPresent(short bitMask, int fieldBitMask) {
        return (bitMask & fieldBitMask) != 0;
    }

    @Getter
    public enum OnlineStatus {
        ONLINE("在线"),
        HIDDEN("隐身"),
        FORCE_LOGOUT("强制退出"),
        OFFLINE("离线");

        /**
         * 状态信息
         */
        private final String info;

        OnlineStatus(String info) {
            this.info = info;
        }
    }
}
