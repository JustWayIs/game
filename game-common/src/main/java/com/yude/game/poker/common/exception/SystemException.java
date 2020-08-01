package com.yude.game.poker.common.exception;


import com.yude.protocol.common.constant.StatusCodeI;
import com.yude.protocol.common.constant.StatusCodeEnum;

/**
 * @Author: HH
 * @Date: 2020/6/29 14:06
 * @Version: 1.0
 * @Declare:
 */
public class SystemException extends AbstractBaseException {

    /**
     * H2 这么多种不同的构造器，应该要考虑使用建造者模式
     * @param errMessage
     */
    public SystemException(String errMessage){
        super(errMessage);
        this.setErrorCode(StatusCodeEnum.FAIL);
    }

    public SystemException(StatusCodeI statusCodeI){
        super(statusCodeI.msg());
        this.setErrorCode(statusCodeI);
    }

    public SystemException(StatusCodeI errCode, String errMessage) {
        super(errMessage);
        this.setErrorCode(errCode);
    }

    public SystemException(String errMessage, Throwable e) {
        super(errMessage, e);
        this.setErrorCode(StatusCodeEnum.FAIL);
    }

    public SystemException(String errMessage, StatusCodeI errorCode, Throwable e) {
        super(errMessage, e);
        this.setErrorCode(errorCode);
    }
}
