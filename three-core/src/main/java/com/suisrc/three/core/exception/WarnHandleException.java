package com.suisrc.three.core.exception;

import java.io.PrintStream;
import java.util.logging.Logger;

/**
 * 警告异常，本身不具有什么意义
 * 
 * @author Y13
 *
 */
public class WarnHandleException extends RuntimeException {
    private static final long serialVersionUID = -8309548164872801644L;
    /***
     * 日志
     */
    private static final Logger logger = Logger.getLogger(WarnHandleException.class.getPackage().getName());
    
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
        printStackTrace((PrintStream)null);
    }
    
    /**
     * 
     */
    public void printStackTrace(PrintStream s) {
        if (s != null) {
            s.println(getMessage());
        } else {
            logger.warning(getMessage());
        }
    }
}
