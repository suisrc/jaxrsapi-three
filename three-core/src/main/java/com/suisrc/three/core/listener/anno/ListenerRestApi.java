package com.suisrc.three.core.listener.anno;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * 监听的rest接口
 * 
 * @author Y13
 */
@Target({TYPE})
@Retention(RUNTIME)
public @interface ListenerRestApi {

    /**
     * 监听的接口的类型
     */
    String value();

}
