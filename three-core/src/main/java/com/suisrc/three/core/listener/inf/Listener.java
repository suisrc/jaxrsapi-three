package com.suisrc.three.core.listener.inf;

import com.suisrc.core.reference.RefVal;

/***
 * 监听接口
 * 
 * 监听器使用的时候，需要通过@Named注解标记监听的对象的类型， 必须是类型的全名（包+类型） 
 * 
 * 使用String类型标记而不使用Class,是为了解除项目之间的强引用
 * 
 * @author Y13
 *
 */
//@Priority
public interface Listener<T> {
    
    /**
     * 接受监听对象
     * 
     * @param result 应答的结果
     * @param bean 应答的参数
     * @return 是否继续下一个应答
     */
    boolean accept(RefVal<?> result, T bean);

    /**
     * 接受监听对象
     * 
     * @param result 应答的结果
     * @param bean 应答的参数
     * @param owner 应答内容的请求者
     * @return 是否继续一下应答
     */
    default boolean accept(RefVal<?> result, T bean, Object owner) {
        return accept(result, bean);
    }

    /**
     * 是否加载该监听器
     * 
     * @return true 加载该监听器， false, 不加载该监听器
     */
    default boolean load() {
        return true;
    }

    /**
     * 
     * 是否确定使用该监听器处理内容
     * 
     * @param hasResult 执行应答前，是否已经有应答的结果
     * @param bean 应答的内容
     * @return true, 使用该监听器处理内容，false, 不使用该监听器处理内容
     */
    default boolean matches(boolean hasResult, final T bean) {
        return true;
    }
    
    /**
     * 决定里处理内容的优先级，默认为 "N"
     * 
     * 对于特殊内容的优先处理有用有作用
     * 
     * @param bean 空表示默认顺序，可以在系统内部存储排序，非空，表示处理对象的顺序
     * @return
     */
    default String priority(final T bean) {
        return "N";
    }

}
