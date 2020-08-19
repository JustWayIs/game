package com.yude.game.communication.dispatcher;

import com.yude.game.exception.BizException;
import com.yude.protocol.common.MessageType;

/**
 * @Author: HH
 * @Date: 2020/7/31 11:49
 * @Version: 1.0
 * @Declare:
 */
public interface IRequestMappingInfo {

     MessageType getMessageTypByCommand(Integer cmd) throws BizException;

     boolean canMultithreaded(Integer cmd) throws BizException;
}
