package com.llnxs.log.utils;


import com.google.common.base.Strings;
import com.google.common.collect.Maps;
import com.llnxs.log.LogValue;
import com.llnxs.log.annotation.FieldInfo;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * 日志工具类
 *
 * @author llnxs
 * @date 20180/12/16
 */
public class LogUtil {


    public static final String param1 = "由：";
    public static final String param2 = "修改为：";
    public static final String param3 = "；";

    /**
     * 获取不同字符串
     *
     * @param oldObject
     * @param newObject
     * @return
     */
    public static String getDifferentLog(Object oldObject, Object newObject) {
        HashMap<String, LogValue> oldMap = getProperties(oldObject);
        HashMap<String, LogValue> newMap = getProperties(newObject);
        return comparatorObject(oldMap, newMap, param1, param2, param3);
    }

    /**
     * 获取不同字符串 并且自定义分隔符
     * name param1 oldValue param2 newValue param3
     * 名称 由：1 修改为 2 ;
     *
     * @param oldObject
     * @param newObject
     * @param param1
     * @param param2
     * @param param3
     * @return
     */
    public static String getDifferentLog(Object oldObject, Object newObject, String param1, String param2, String param3) {
        HashMap<String, LogValue> oldMap = getProperties(oldObject);
        HashMap<String, LogValue> newMap = getProperties(newObject);
        return comparatorObject(oldMap, newMap, param1, param2, param3);
    }

    /**
     * 获取对象的值
     *
     * @param obj
     * @return
     */
    private static HashMap<String, LogValue> getProperties(Object obj) {
        HashMap<String, LogValue> map = Maps.newHashMap();
        try {
            Class<?> objClass = obj.getClass();
            Field[] fields = objClass.getDeclaredFields();
            for (int i = 0; i < fields.length; i++) {
                Field field = fields[i];
                field.setAccessible(Boolean.TRUE);
                LogValue logValue;
                boolean annotationPresent = field.isAnnotationPresent(FieldInfo.class);
                if (annotationPresent) {
                    FieldInfo annotation = field.getAnnotation(FieldInfo.class);
                    String sourceCode = annotation.sourceCode();
                    if (Strings.isNullOrEmpty(sourceCode)) {
                        logValue = new LogValue(annotation.name(), field.get(obj));
                    } else {
                        String sourceCodeClassName = sourceCode.substring(0, sourceCode.lastIndexOf("."));
                        String sourceCodeMethodName = sourceCode.substring(sourceCode.lastIndexOf(".") + 1);
                        Class<?> sourceCodeClass = Class.forName(sourceCodeClassName);
                        Class<?> type = field.getType();
                        Object val = field.get(obj);
                        Object formatValue = sourceCodeClass.getMethod(sourceCodeMethodName, new Class[]{type}).invoke(obj, new Object[]{val});
                        logValue = new LogValue(annotation.name(), formatValue);
                    }
                    map.put(field.getName(), logValue);
                }
            }
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        return map;
    }

    /**
     * 比较对象值返回不同内容
     *
     * @param oldMap
     * @param newMap
     * @param param1
     * @param param2
     * @param param3
     * @return
     */
    private static String comparatorObject(HashMap<String, LogValue> oldMap, HashMap<String, LogValue> newMap,
                                           String param1, String param2, String param3) {
        StringBuffer sb = new StringBuffer();
        if (oldMap != null && !oldMap.isEmpty()) {
            for (Map.Entry<String, LogValue> entry : oldMap.entrySet()) {
                LogValue newVo = newMap.get(entry.getKey());
                LogValue oldVo = entry.getValue();
                if (!Objects.equals(newVo.getValue(), oldVo.getValue())) {
                    sb.append(getComparatorMsg(newVo.getName(), oldVo.getValue(), newVo.getValue(), param1, param2, param3));
                }
            }
        }
        return sb.toString();
    }

    /**
     * 返回不同信息字符串
     *
     * @param name
     * @param newValue
     * @param oldValue
     * @param param1
     * @param param2
     * @param param3
     * @return
     */
    private static String getComparatorMsg(String name, Object newValue, Object oldValue,
                                           String param1, String param2, String param3) {
        return name + param1 + oldValue + " " + param2 + newValue + param3;
    }


}
