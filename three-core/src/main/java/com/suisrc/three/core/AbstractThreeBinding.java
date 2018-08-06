package com.suisrc.three.core;

import java.time.LocalDateTime;
import java.util.function.Function;
import java.util.logging.Logger;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.suisrc.core.fasterxml.FasterFactory;
import com.suisrc.core.utils.Throwables;
import com.suisrc.three.core.exception.UnHandleException;
import com.suisrc.three.core.exception.WarnHandleException;
import com.suisrc.three.core.msg.IMessage;
import com.suisrc.three.core.msg.MsgNode;
import com.suisrc.three.core.msg.UnknowMessage;

/**
 * 服务器与第三方接口捆绑回调接口
 * 
 * @author Y13
 *
 */
public abstract class AbstractThreeBinding implements ThreeBinding {
    protected static final Logger logger = Logger.getLogger(ThreeBinding.class.getName());
    
    /**
     * 监听器
     */
    protected abstract MessageController getMessageController();
    
    /**
     * 需要初始化listenerManager
     * 
     * 构造后初始化
     */
    protected abstract void doPostConstruct();
    
    /**
     * 获取数据对应的类型
     * @param node
     * @return
     */
    protected abstract Class<? extends IMessage> getMsgClass(MsgNode node);
    
    /**
     * 释放资源
     */
    protected void doPreDestroy() {};
    
    /**
     * 发生异常，异常可以在系统内部抛出，如果返回为null,就忽略异常，不进行处理
     * 
     * @param log
     */
    protected RuntimeException throwException(Exception e) {
        return Throwables.getRuntimeException(e);
    }
    
    /**
     * 获取格式化工厂
     * @return
     */
    protected FasterFactory getFasterFactory() {
        return FasterFactory.getDefault();
    }

    /**
     * 把字符串转换为消息
     * @param xml
     * @return
     */
    protected IMessage str2Bean(String content, boolean isJson) {
        MsgNode node = getFasterFactory().str2Node(content, isJson, MsgNode::create);
        Class<? extends IMessage> msgType = getMsgClass(node);
        IMessage bean = null;
        if (msgType != null) {
            // 解析数据
            try {
                bean = node.toBean(msgType);
            } catch (Throwable e) {
                // 使用 node.toBean在某种情况下，比如@JacksonXmlElementWrapper是无法解析的
                String module = "Message content can not be resolved [%s]:%s";
                logger.warning(String.format(module, msgType.getCanonicalName(), e.getMessage()));
                // 尝试使用基础解析工具解析
                bean = getFasterFactory().str2Bean(content, msgType, isJson);
            }
        }
        if (bean == null) {
            logger.info(String.format("Message content can not be resolved, used UnknowMessage :%s", content));
            // 数据无法解析
            bean = new UnknowMessage();
        }
        // 给出原始数据的Node节点模型（可用于验证）
        bean.setTargetRawNode(node);
        // 给出数据的原始类型
        bean.setJson(isJson);
        // 给出原始数据内容--防止后面用于验证时候使用一些漏掉的信息
        bean.setTargetRawData(content);
        // 返回解析的结果
        return bean;
    }

    /**
     * 把消息转换为字符串
     * @param xml
     * @return
     */
    protected String bean2Str(Object bean, boolean isJson) {
        return getFasterFactory().bean2Str(bean, isJson);
    }

    /**
     * 微信回调请求绑定
     * 内部处理方法
     * @param content 消息的内容
     * @param isJson 消息的类型，这里只有两种，json or xml 如果不是json就必须是xml没有其他选择
     * @param appId 消息来自的应用的主键
     * @param resultAdapter
     * @param responseAdapter
     * @return
     */
    protected Response doInternalWork(String content, boolean isJson, String appId,
            Function<String, String> resultAdapter, Function<String, Response> responseAdapter) {
        // --------------------------------消息内容处理------------------------------------//
        // 解析消息内容
        IMessage message = str2Bean(content, isJson); // 转换为bean
        if (message == null) {
            // 无法解析消息是严重bug， 需要作出不能处理警告
            String error = String.format("Message content can not be resolved [%s]:%s", getClass(), content);
            // return Response.ok().entity(error).type(MediaType.TEXT_PLAIN).build();
            throw throwException(new UnHandleException(error));
        }
        // 赋值消息的信息
        message.setTargetAppId(appId);
        // 通过监听器处理消息内容
        Object bean = getMessageController().accept(message); // 得到处理的结构
        if (bean == null) {
            // 无法处理消息，仅仅是没有监听，作为不能应道警告
            String error = String.format("Message content can not be answered [%s]:%s", getClass(), content);
            // return Response.ok().entity(error).type(MediaType.TEXT_PLAIN).build();
            throw throwException(new WarnHandleException(error));
        }
        if (bean instanceof IMessage) {
            isJson = ((IMessage)bean).isJson(); // 用新的格式要求替换
        }
        // --------------------------------响应结果解析------------------------------------//
        // 分析结果
        String result = bean instanceof String ? bean.toString() : bean2Str(bean, isJson);
        if (resultAdapter != null) {
            result = resultAdapter.apply(result);
        }
        // --------------------------------返回处理的结果------------------------------------//
        if (responseAdapter != null) {
            Response response = responseAdapter.apply(result);
            if (response != null) {
                return response;
            }
        }
        return Response.ok().entity(result).type(isJson ? MediaType.APPLICATION_JSON : MediaType.APPLICATION_XML).build();
    }

    /**
     * 后台微信请求服务器运行状态
     */
    public String getServerInfo() {
        return "Server is Running! time:" + LocalDateTime.now();
    }
}
