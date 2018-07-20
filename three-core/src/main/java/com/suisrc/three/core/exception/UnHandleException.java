package com.suisrc.three.core.exception;

/**
 * 系统无法处理异常
 * 
 * 发出警告信息
 * 
 * @author Y13
 *
 */
public class UnHandleException extends RuntimeException {
    private static final long serialVersionUID = -8309548164872801644L;
    
    /** 
     * 
     */
    public UnHandleException() {
        super();
    }

    /** 
     * 
     */
    public UnHandleException(String message) {
        super(message);
    }

    /**
     * 
     */
    public UnHandleException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * 
     */
    public UnHandleException(Throwable cause) {
        super(cause);
    }

    /**
     * 
     */
    protected UnHandleException(String message, Throwable cause,
                               boolean enableSuppression,
                               boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
