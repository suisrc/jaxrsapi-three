package com.suisrc.three.core;

/**
 * 服务器绑定接口
 * 
 * 仅仅提供适配器功能
 * 
 * @author Y13
 *
 */
public interface ThreeBinding {
    
    /**
     * 安全类型转换
     * 
     * 如果类型无法转换，返回null
     * @param type
     * @return
     */
    @SuppressWarnings("unchecked")
    default <T> T as(Class<T> type) {
        return type.isAssignableFrom(getClass()) ? (T)this : null;
    }
    
    /**
     * 通过适配器获取需要的类型
     * @param named
     * @param type
     * @return
     */
    default <T> T getAdatper(String named, Class<T> type) {
        return null;
    }
    
    /**
     * 通过适配器设定需要的内容
     * @param type
     * @param obj
     */
    default <T> void setAdapter(String named, Class<T> type, T obj) {}
}
