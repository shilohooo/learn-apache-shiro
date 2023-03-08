package org.shiloh.web.util;

import org.apache.shiro.codec.Base64;
import org.apache.shiro.session.Session;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 * 序列化工具
 *
 * @author shiloh
 * @date 2023/3/5 18:53
 */
public final class SerializableUtils {
    private SerializableUtils() {
    }

    /**
     * 将 {@link Session} 对象序列化为字符串
     *
     * @param session session 对象
     * @return session 序列化后的字符串内容
     * @author shiloh
     * @date 2023/3/5 18:54
     */
    public static String serialize(Session session) {
        try (
                final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                final ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream)
        ) {
            objectOutputStream.writeObject(session);
            return Base64.encodeToString(outputStream.toByteArray());
        } catch (Exception e) {
            throw new RuntimeException("Session 序列化出错：", e);
        }
    }

    /**
     * 将 {@link Session} 字符串内容反序列化为 {@link Session}对象
     *
     * @param sessionStr session 字符串内容
     * @return {@link Session}
     * @author shiloh
     * @date 2023/3/5 18:57
     */
    public static Session deserialize(String sessionStr) {
        try (
                final ByteArrayInputStream bis = new ByteArrayInputStream(Base64.decode(sessionStr));
                final ObjectInputStream objectInputStream = new ObjectInputStream(bis)
        ) {
            return (Session) objectInputStream.readObject();
        } catch (Exception e) {
            throw new RuntimeException("Session 反序列化出错：", e);
        }
    }
}
