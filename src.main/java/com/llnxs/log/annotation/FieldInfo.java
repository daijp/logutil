package com.llnxs.log.annotation;


import java.lang.annotation.*;

/**
 * field注解
 * @author llnxs
 * @date 20180/12/16
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface FieldInfo {


    /**
     * 字段名称
     * @return
     */
    String name() default "";

    /**
     * 转换实现方式
     * @return
     */
    String sourceCode() default "";

}