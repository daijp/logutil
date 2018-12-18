package io.github.daijp;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * 注解
 */
@Data
@AllArgsConstructor
public class LogValue {

    /**
     * 字段名称
     */
    private String name;

    /**
     * 值
     */
    private Object value;
}
