package com.suisrc.three.core.msg;

import java.io.IOException;
import java.util.function.Function;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.suisrc.core.utils.Throwables;

/**
 * 消息节点数据
 * 
 * 使用DOM方式解析的内容
 * 
 * @author Y13
 *
 */
public final class MsgNode {

    /**
     * 创建一个
     * @param mapper
     * @param content
     * @return
     * @throws IOException 
     * @throws JsonProcessingException 
     */
    public static MsgNode create(ObjectMapper mapper, String content) {
        try {
            MsgNode rrn = new MsgNode();
            rrn.root = mapper.readTree(content);
            rrn.content = content;
            rrn.mapper = mapper;
            return rrn;
        } catch (Exception e) {
            throw Throwables.getRuntimeException(e);
        }
    }
    
    /**
     * 节点树
     */
    private JsonNode root;
    
    /**
     * 原始内容
     */
    private String content;
    
    /**
     * 解析器
     */
    private ObjectMapper mapper;

    /**
     * 只能通过create方法创建该对象
     */
    private MsgNode() {}
    
    /**
     * 获取节点树
     * @return the root
     */
    public JsonNode getRoot() {
        return root;
    }

    /**
     * 获取原始内容
     * @return the content
     */
    public String getContent() {
        return content;
    }

    /**
     * 获取解析器
     * @return the mapper
     */
    public ObjectMapper getMapper() {
        return mapper;
    }
    
    /**
     * 获取属性的内容
     * 
     * 如果不存在返回 null
     */
    public String getValueAsString(String fieldName) {
        JsonNode node = root.get(fieldName);
        return node == null ? null : node.asText(null);
    }
    
    /**
     * 获取属性的内容
     * 
     * getValueAsString方法的简写
     */
    public String getV(String fieldName) {
        JsonNode node = root.get(fieldName);
        return node == null ? null : node.asText(null);
    }

    /**
     * 转换为Bean结构的对象
     * @param type
     * @return
     * @throws JsonProcessingException 
     */
    public <T> T toBean(Class<T> type) throws JsonProcessingException {
        return mapper.treeToValue(root, type);
    }

    /**
     * 转换为Bean结构的对象
     * @param type 转换类型
     * @param back 回调，用于处理异常
     * @return
     */
    public <T> T toBeanHideException(Class<T> type, Function<T, T> back) {
        T bean = null;
        try {
            bean = toBean(type);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return back == null ? bean : back.apply(bean);
    }

    /**
     * 转换为Bean结构的对象
     * @param type
     * @return
     */
    public <T> T toBeanHideException(Class<T> type) {
        try {
            return toBean(type);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return null;
        }
    }

}
