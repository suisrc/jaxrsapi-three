package com.suisrc.three.core.listener;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Set;

import com.google.common.collect.Sets;
import com.suisrc.core.Global;
import com.suisrc.core.reference.RefVal;
import com.suisrc.core.utils.ReflectionUtils;
import com.suisrc.three.core.MessageController;
import com.suisrc.three.core.listener.anno.ListenerInclude;
import com.suisrc.three.core.listener.anno.ListenerRestApi;
import com.suisrc.three.core.listener.inf.Listener;
import com.suisrc.three.core.listener.inf.ListenerInstance;
import com.suisrc.three.core.msg.IMessage;

/**
 * 监听事件管理器 用于管理所有回调监听
 * 
 * 监听器只能进行初始化构建（build）,如果监听内容发生变化，需要重新启动服务进行重新构建。
 * 
 * 可以通过build中的参数指定扫描器扫描的范围
 * 
 * @author Y13
 *
 */
@SuppressWarnings({"rawtypes", "unchecked"})
public class ListenerManager implements MessageController, Serializable {
    private static final long serialVersionUID = 2802236925094985275L;

    /**
     * 监听内容的索引
     */
    protected HashMap<Class, Listener[]> indexs = new HashMap<>();
    
    /**
     * 监控管理器的所有者
     * 如果所有者为null，为公共监听器，监听所有的内容
     */
    protected Object owner;
    
    /**
     * 所属类型，解决实体和接口标记的问题
     */
    protected String ownerKey;
    
    /**
     * 监听创建者
     */
    protected ListenerInstance instance = ListenerInstance.DEFAULT;
    
    /**
     * 构造
     * 
     * @param owner
     * @param ownerType 
     */
    public ListenerManager(Object owner, String ownerKey) {
        this.owner = owner;
        this.ownerKey = ownerKey;
    }
    
    /**
     * 创建构建器的实体
     * @param listenerCreater
     */
    public void setListenerInstance(ListenerInstance instance) {
        this.instance = instance;
    }
    
    /**
     * 接受消息对象, 执行内容
     * 
     * @param bean
     * @return
     */
    public Object accept(IMessage msg) {
        // 执行匹配
        Listener[] listeners = indexs.get(msg.getClass());
        if (listeners == null || listeners.length == 0) {
            return null;
        }
        // 执行结果
        RefVal<?> result = new RefVal<>();
        // 执行监听处理
        Arrays.asList(listeners).stream()
            .sorted((l, r) -> l.priority(msg).compareTo(r.priority(msg))) // 请求排序
            .anyMatch(listener -> listener.matches(result.get() != null, msg) // 是否进行处理
                    && !listener.accept(result, msg, owner)); // 执行处理
        // 返回执行结果
        return result.get();
    }

    /**
     * 获取监听的类型
     * 
     * @param clazz
     * @return
     */
    private Set<Class> getListenerKey(Class<? extends Listener> clazz) {
        ListenerRestApi restApi = clazz.getAnnotation(ListenerRestApi.class);
        if (restApi == null) {
            // 泛型监听
        } else if (ownerKey != null && ownerKey.equals(restApi.value())) {
            // 特定接口类型监听
        } else {
            return null; // 鉴别条件验证没有通过
        }
        Class genericType = ReflectionUtils.getGenericKey(clazz, Listener.class, 0);
        if (genericType == null) {
            // 查询监听的内容类型为空，无法实现增加监听
            return null;
        }
        // 类型集合
        Set types = new LinkedHashSet<>();
        // 查询包含关系
        ListenerInclude include = clazz.getAnnotation(ListenerInclude.class);
        if (include != null && include.value().length > 0) {
            for (Class type : include.value()) {
                if (genericType.isAssignableFrom(type)) {
                    // 监听的类型可以使用
                    types.add(type);
                } else {
                    // 指定的类型不可用
                    String error = String.format("listener type '%s' is not assignable form '%s' in [%s], ignore.", 
                            genericType.getCanonicalName(), type.getCanonicalName(), clazz.getCanonicalName());
                    Global.getLogger().warning(error);
                }
            }
        }
        // if (types.isEmpty() && (genericType.getModifiers() & (Modifier.ABSTRACT | Modifier.INTERFACE)) != 0) {
        if (!types.isEmpty()){
            return Sets.newHashSet(types);
        } else {
            return Sets.newHashSet(genericType);
        }
    }

    /**
     * 增加监听对象
     * 
     * @param listener
     */
    private void addListeners(Listener listener, Set<Class> classes) {
        if (listener == null || !listener.load()) {
            // 监听失效
            return;
        }
        if (classes == null) {
            classes = getListenerKey((Class<Listener>)listener.getClass());
            if (classes == null) {
                // 无法找到监听对象
                return;
            }
        }
        classes.forEach(clazz -> addListener(listener, clazz));
    }

    /**
     * 增加监听对象 不推荐使用该方法，该方法对监听的有效性和监听内容可行性都没有判断 推荐使用addListeners(Listener, Set<Class>)作为代替
     * 
     * @param listener 监听对象
     * @param clazz 监听的类型
     * @return Listener 系统中已经存在的监听
     */
    private <T> Listener addListener(Listener<T> listener, Class<T> clazz) {
        Listener[] listeners = indexs.get(clazz); // 查看原有监听
        if (listeners == null) {
            indexs.put(clazz, new Listener[] {listener});
            return null;
        }
        // 监听重复检测
        for (int i = 0; i < listeners.length; i++) {
            if (listeners[i].getClass() == listener.getClass()) {
                // 类型相同，值保留同一个监听,这里保留后加入的，把旧的返回
                Listener olistener = listeners[i];
                listeners[i] = listener;
                if (olistener.priority(null) != listener.priority(null)) {
                    // 需要重新排序
                    Arrays.sort(listeners, (l, r) -> l.priority(null).compareTo(r.priority(null)));
                }
                return olistener;
            }
        }
        // 加入新数据
        listeners = Arrays.copyOf(listeners, listeners.length + 1);
        listeners[listeners.length - 1] = listener;
        Arrays.sort(listeners, (l, r) -> l.priority(null).compareTo(r.priority(null)));
        indexs.put(clazz, listeners);
        return null;
    }

    /**
     * 通过类型增加监听对象
     * 
     * @param clazz
     * @return
     */
    public boolean addListener(Class<? extends Listener> listenerClass) {
        try {
            Set<Class> classes = getListenerKey(listenerClass);
            if (classes == null || classes.isEmpty()) {
                // 无法找到监听对象
                return false;
            }
            Listener listener = instance.newInstance(listenerClass);
            addListeners(listener, classes);
            return true;
        } catch (RuntimeException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    /**
     * 通过包扫描，增加监听
     */
    public void scanPackages(String... pkgs) {
        if (pkgs == null || pkgs.length == 0) {
            return;
        }
        Collection<Class<? extends Listener>> listenters = ReflectionUtils.getSubclasses(Listener.class, pkgs);
        if (listenters == null || listenters.isEmpty()) {
            return;
        }
        listenters.forEach(this::addListener);
    }
    
    /**
     * 通过类扫描，增加监听
     */
    public void scanClasses(String... clss) {
        if (clss == null|| clss.length == 0) {
            return;
        }
        for (String cls : clss) {
            if (cls.isEmpty()) {
                continue;
            }
            try {
                // 加载类
                Class clazz = Thread.currentThread().getContextClassLoader().loadClass(cls);
                // 判断类是否可以使用
                if (Listener.class.isAssignableFrom(clazz)) {
                    addListener(clazz);
                } else {
                    String error = String.format("Type exception: [%s] can't convert to [%s]", cls, Listener.class.getName());
                    Global.getLogger().warning(error);
                }
            } catch (ClassNotFoundException e) {
                String error = String.format("Unable to load [%s], exception: [%s]", cls, e.getMessage());
                Global.getLogger().warning(error);
            }
        }
    }

}
