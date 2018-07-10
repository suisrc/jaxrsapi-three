package com.suisrc.three.core.msg;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

/**
 * 消息, 通信时候处理使用的消息对象接口
 * 
 * json字段只负责传递数据格式，不参与序列化和反序列化
 * 
 * targetRawData字段不进行内容传递，最好不要使用，用于数据的后期验证需要
 * 
 * @author Y13
 *
 */
@JsonIgnoreProperties({"json", "targetRawData"})
@JsonInclude(Include.NON_NULL)
public interface IMessage {
    
    /**
     * 是否格式化为json
     */
    default boolean isJson() {
        // 默认使用xml格式
        return false;
    }
    
    /**
     * 设定格式化为json
     */
    default void setJson(boolean isJson) {
        // no operation
    }
    
    /**
     * 获取原始数据
     */
    default String getTargetRawData() {
        return null;
    }
    
    /**
     * 设定原始内容
     */
    default void setTargetRawData(String rawData) {
        // no operation
    }
}
