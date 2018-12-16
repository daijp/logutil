package com.llnxs.log;

import lombok.AllArgsConstructor;
import lombok.Data;

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
