package org.shiloh.shiro.permission;

import org.apache.shiro.authz.Permission;

import java.util.Objects;

/**
 * 自定义位运算权限，规则：
 * +资源字符串+权限位+实例ID，以+开头，中间通过+分割：
 * 权限位设定:
 * 0：所有权限
 * 1：新增：0001
 * 2: 修改：0010
 * 4：删除：0100
 * 8：查看：1000
 * <p>
 * 示例：+user+10，表示对user资源拥有修改、查看权限
 * <p>
 * 暂不考虑一些异常情况
 *
 * @author shiloh
 * @date 2022/2/10 16:24
 */
public class BitPermission implements Permission {

    /**
     * 资源标识符
     */
    private String resourceIdentify;

    /**
     * 权限位
     */
    private int permissionBit;

    /**
     * 实例ID
     */
    private String instanceId;

    /**
     * 通过传入的权限配置字符串构造{@link BitPermission}类实例
     *
     * @param permissionString 权限配置字符串
     * @author shiloh
     * @date 2022/2/10 16:28
     */
    public BitPermission(final String permissionString) {
        final String[] permissions = permissionString.split("\\+");

        // 设置资源标识符
        if (permissions.length > 1) {
            this.resourceIdentify = permissions[1];
        }

        if (Objects.isNull(this.resourceIdentify) || this.resourceIdentify.isEmpty()) {
            this.resourceIdentify = "*";
        }

        // 设置权限位
        if (permissions.length > 2) {
            this.permissionBit = Integer.parseInt(permissions[2]);
        }

        // 设置实例ID
        if (permissions.length > 3) {
            this.instanceId = permissions[3];
        }

        if (Objects.isNull(this.instanceId) || this.instanceId.isEmpty()) {
            this.instanceId = "*";
        }
    }

    /**
     * Returns {@code true} if this current instance <em>implies</em> all the functionality and/or resource access
     * described by the specified {@code Permission} argument, {@code false} otherwise.
     * <p/>
     * <p>That is, this current instance must be exactly equal to or a <em>superset</em> of the functionality
     * and/or resource access described by the given {@code Permission} argument.  Yet another way of saying this
     * would be:
     * <p/>
     * <p>If &quot;permission1 implies permission2&quot;, i.e. <code>permission1.implies(permission2)</code> ,
     * then any Subject granted {@code permission1} would have ability greater than or equal to that defined by
     * {@code permission2}.
     *
     * @param p the permission to check for behavior/functionality comparison.
     * @return {@code true} if this current instance <em>implies</em> all the functionality and/or resource access
     * described by the specified {@code Permission} argument, {@code false} otherwise.
     */
    @Override
    public boolean implies(Permission p) {
        if (!(p instanceof BitPermission)) {
            return false;
        }

        final BitPermission other = (BitPermission) p;
        if (!("*".equals(this.resourceIdentify) || this.resourceIdentify.equals(other.resourceIdentify))) {
            return false;
        }

        if (!(this.permissionBit == 0 || (this.permissionBit & other.permissionBit) != 0)) {
            return false;
        }

        return "*".equals(this.instanceId) || this.instanceId.equals(other.instanceId);
    }

    @Override
    public String toString() {
        return "BitPermission{" +
                "resourceIdentify='" + resourceIdentify + '\'' +
                ", permissionBit=" + permissionBit +
                ", instanceId='" + instanceId + '\'' +
                '}';
    }
}
