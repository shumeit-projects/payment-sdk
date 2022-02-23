/**
 * @Project: SNS_Platform
 * @Author: squll
 * @Date: 2010-12-30
 * @Copyright: (c) 2010 广州菠萝信息技术有限公司 All rights reserved.
 */
package com.shumeit.sdk.api.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.alibaba.fastjson.serializer.SerializeConfig;
import com.alibaba.fastjson.serializer.SerializerFeature;

import java.lang.reflect.Type;

public class JsonUtil {

    /**
     * 将一个对像转成一个json字符串
     */
    public static String toString(Object obj) {
        return JSON.toJSONString(obj, SerializeConfig.globalInstance, null, "yyyy-MM-dd HH:mm:ss", JSON.DEFAULT_GENERATE_FEATURE, SerializerFeature.WriteDateUseDateFormat);
    }

    /**
     * 把json字符串转换成指定Class的对象
     *
     * @param text  .
     * @param clazz .
     * @param <T>   .
     * @return .
     */
    public static <T> T toBean(String text, Class<T> clazz) {
        return JSON.parseObject(text, clazz);
    }


    /**
     * 把json字符串转换成指定类型的对象
     *
     * @param text .
     * @param type .
     * @param <T>  .
     * @return .
     */
    public static <T> T toBean(String text, Type type) {
        return JSON.parseObject(text, type);
    }

    /**
     * 把json字符串转换成指定类型的对象
     *
     * @param text .
     * @param type .
     * @param <T>  .
     * @return .
     */
    public static <T> T toBean(String text, TypeReference<T> type) {
        return JSON.parseObject(text, type);
    }

}
