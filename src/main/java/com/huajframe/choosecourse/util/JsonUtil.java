package com.huajframe.choosecourse.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;

/**
 * Json工具类
 *
 *
 * @author Hua JFarmer
 */
public class JsonUtil {
    private static ObjectMapper objectMapper = new ObjectMapper();

    /**
     * 将对象转换成json字符串
     *
     * @param obj 对象
     * @return 字符串
     */
    public static String object2JsonStr(Object obj) {
        try {
            return objectMapper.writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            //打印异常信息
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 将字符串转换为对象
     *
     * @param jsonStr 对象字符串
     * @param clazz 对象类型
     * @param <T> 泛型
     * @return 对象
     */
    public static <T> T jsonStr2Object(String jsonStr, Class<T> clazz) {
        try {
            return objectMapper.readValue(jsonStr.getBytes(StandardCharsets.UTF_8), clazz);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 将json数据转换成pojo对象list
     *
     * @param jsonStr list字符串
     * @param beanType 对象类型
     * @param <T> 泛型
     * @return list集合
     */
    public static <T> List<T> jsonToList(String jsonStr, Class<T> beanType) {
        JavaType javaType = objectMapper.getTypeFactory().constructParametricType(List.class, beanType);
        try {
            return objectMapper.readValue(jsonStr, javaType);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }
}
