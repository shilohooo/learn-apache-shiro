package org.shiloh.web.entity.base;

import lombok.Data;

import java.io.Serializable;

/**
 * 实体基类
 *
 * @author shiloh
 * @date 2023/3/31 23:00
 */
@Data
public class BaseEntity implements Serializable {
    private static final long serialVersionUID = -2687419940964970888L;

    private Long id;
}
