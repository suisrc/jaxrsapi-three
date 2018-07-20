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
@JsonIgnoreProperties({"json", "targetRawData", "targetRawNode", "targetAppId"})
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
     * 获取原始数据
     * 
     * 如果没有必要可以忽略
     */
    default String getTargetRawData() {
        return null;
    }
    
    /**
     * 获取原始数据解析的节点形式
     * 
     * 如果没有必要可以忽略
     */
    default MsgNode getTargetRawNode() {
        return null;
    }
    
    /**
     * 获取应用主键
     * 
     * 如果没有必要可以忽略
     */
    default String getTargetAppId() {
        return null;
    }

    /**
     * 设定消息类型（xml or json）
     * @param isJson
     */
    default void setJson(boolean isJson) {}

    /**
     * 设定消息原始数据
     * @param rawData
     */
    default void setTargetRawData(String rawData) {}

    /**
     * 设定消息原始数据
     * @param rawNode
     */
    default void setTargetRawNode(MsgNode rawNode) {}

    /**
     * 设定引用主键
     * @param appId
     */
    default void setTargetAppId(String appId) {}

}
