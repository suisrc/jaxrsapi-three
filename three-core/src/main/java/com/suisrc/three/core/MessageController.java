package com.suisrc.three.core;


import com.suisrc.three.core.msg.IMessage;

/**
 * 消息响应控制器
 * 
 * @author Y13
 *
 */
public interface MessageController {

    
    /**
     * 接受消息对象, 执行内容
     * 
     * @param bean
     * @return
     */
    Object accept(IMessage msg);

}
