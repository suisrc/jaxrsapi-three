package com.suisrc.three.core.msg;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * 无法解析的内容，获取解析出错的内容
 * @author Y13
 *
 */
public class UnknowMessage implements IMessage {

    /**
     * 是否格式化为json
     * 
     * 该字段不参与格式数据内容
     */
    private boolean isJson = false;
    
    /**
     * 消息来自企业号或者公众号ID
     */
    private String targetAppId = null;
    
    /**
     * 原始数据内容
     */
    private String targetRawData = null;
    
    /**
     * 共通解析的消息内容
     * 
     * 该内容只有在消息内容无法解析的时候有效
     */
    @JsonIgnore
    private MsgNode targetRawNode = null;
    
    /**
     * 是否格式化为json
     */
    @Override
    public boolean isJson() {
        return isJson;
    }
    
    /**
     * 设定格式化为json，默认情况下格式化为xml
     * @param isJson
     */
    @Override
    public void setJson(boolean isJson) {
        this.isJson = isJson;
    }
    
    /**
     * 获取原始数据
     * 使用RawData2为了防止与微信以后内容重复
     */
    @Override
    public String getTargetRawData() {
        return targetRawData;
    }
    
    /**
     * 设定原始内容
     * 系统自动生成调用，最好不要调用该方法，以防止影响到原始数据
     */
    @Override
    @Deprecated
    public void setTargetRawData(String rawData) {
        this.targetRawData = rawData;
    }

    /**
     * 获取共通解析的消息内容 该内容只有在消息内容无法解析的时候有效
     * @return the rawNode2
     */
    @Override
    public MsgNode getTargetRawNode() {
        return targetRawNode;
    }

    /**
     * 设定共通解析的消息内容 该内容只有在消息内容无法解析的时候有效
     * 系统自动生成调用，最好不要调用该方法，以防止影响到原始数据
     * @param rawNode2 the rawNode2 to set
     */
    @Override
    @Deprecated
    public void setTargetRawNode(MsgNode rawNode) {
        this.targetRawNode = rawNode;
    }

    /**
     * 消息来自企业号或者公众号ID
     * @return the targetAppIdOrCropId
     */
    @Override
    public String getTargetAppId() {
        return targetAppId;
    }

    /**
     * 消息来自企业号或者公众号ID
     * 系统自动生成调用，最好不要调用该方法，以防止影响到原始数据
     * @param targetAppId the targetAppId to set
     */
    @Override
    @Deprecated
    public void setTargetAppId(String appId) {
        this.targetAppId = appId;
    }

    
}
