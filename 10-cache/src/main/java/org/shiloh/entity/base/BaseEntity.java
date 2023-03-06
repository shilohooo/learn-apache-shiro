package org.shiloh.entity.base;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.util.Objects;

/**
 * 实体基类
 *
 * @author shiloh
 * @date 2023/2/28 17:47
 */
@Setter
@Getter
@ToString
public class BaseEntity implements Serializable {
    private static final long serialVersionUID = 1819091402410221355L;

    /**
     * ID
     */
    private Long id;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final BaseEntity that = (BaseEntity) o;
        return Objects.equals(getId(), that.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }
}
