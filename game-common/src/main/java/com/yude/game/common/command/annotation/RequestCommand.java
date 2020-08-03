package com.yude.game.common.command.annotation;


import com.yude.protocol.common.MessageType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @Author: HH
 * @Date: 2020/6/17 20:49
 * @Version: 1.0
 * @Declare:
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
public @interface RequestCommand {
    /**
     * 指令码
     * @return
     */
    int value();

    /**
     * 消息类型,绑定不同的disruptor
     * @return
     */
    MessageType messageType() default MessageType.SERVICE;
}
