package com.yude.game.common.exception;

import com.yude.protocol.common.constant.StatusCodeI;

/**
 * @Author: HH
 * @Date: 2020/6/29 18:23
 * @Version: 1.0
 * @Declare:
 */
public abstract class AbstractBaseException extends RuntimeException{
    private StatusCodeI errorCode;

    public AbstractBaseException(String errMessage){
        super(errMessage);
    }

    public AbstractBaseException(String errMessage, Throwable e) {
        super(errMessage, e);
    }

    public StatusCodeI getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(StatusCodeI errorCode) {
        this.errorCode = errorCode;
    }
}
