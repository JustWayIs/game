package com.yude.game.exception;

import com.yude.protocol.common.constant.StatusCodeI;
import com.yude.protocol.common.constant.StatusCodeEnum;

/**
 * @Author: HH
 * @Date: 2020/6/29 14:05
 * @Version: 1.0
 * @Declare:
 */
public class BizException extends AbstractBaseException {

    public BizException(String errMessage){
        super(errMessage);
        setErrorCode(StatusCodeEnum.FAIL);
    }

    public BizException(StatusCodeI statusCodeI){
        super(statusCodeI.msg());
        setErrorCode(statusCodeI);
    }

    public BizException(String errMessage,StatusCodeI errCode){
        super(errMessage);
        setErrorCode(errCode);
    }

    public BizException(String errMessage, Throwable e) {
        super(errMessage, e);
        setErrorCode(StatusCodeEnum.FAIL);
    }
}
