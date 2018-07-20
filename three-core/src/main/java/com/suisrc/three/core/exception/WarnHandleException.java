package com.suisrc.three.core.exception;

import java.io.PrintStream;

/**
 * 警告异常，本身不具有什么意义
 * 
 * @author Y13
 *
 */
public class WarnHandleException extends RuntimeException {
    private static final long serialVersionUID = -8309548164872801644L;
    
    /** 
     * 
     */
    public WarnHandleException() {
        super();
    }

    /** 
     * 
     */
    public WarnHandleException(String message) {
        super(message);
    }

    /**
     * 
     */
    public WarnHandleException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * 
     */
    public WarnHandleException(Throwable cause) {
        super(cause);
    }

    /**
     * 
     */
    protected WarnHandleException(String message, Throwable cause,
                               boolean enableSuppression,
                               boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
    
    /**
     * 
     */
    public void printStackTrace() {
        printStackTrace(System.out);
    }
    
    /**
     * 
     */
    public void printStackTrace(PrintStream s) {
        s.println(getMessage());
    }
}
