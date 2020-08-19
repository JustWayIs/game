package com.yude.game.common.dispatcher;

import com.yude.protocol.common.MessageType;
import com.yude.protocol.common.request.Request;
import com.yude.protocol.common.response.Response;

import java.lang.reflect.Method;
import java.util.Arrays;

/**
 * @Author: HH
 * @Date: 2020/6/17 16:18
 * @Version: 1.0
 * @Declare:
 */
public class HandlerMethod {
    private int cmd;

    //H2 这个需要测试，如果有多个GameController，用application.getBean(instance)是不是拿到正确的。不正确的话应该是会报错
    private Object instance;

    private Method method;

    private Class<? extends Request>[] paramTypes;

    private Class<? extends Response> returnType;

    private MessageType messageType;

    private boolean canMultithreaded;

    public int getCmd() {
        return cmd;
    }

    public void setCmd(int cmd) {
        this.cmd = cmd;
    }

    public Object getInstance() {
        return instance;
    }

    public void setInstance(Object instance) {
        this.instance = instance;
    }

    public Method getMethod() {
        return method;
    }

    public void setMethod(Method method) {
        this.method = method;
    }

    public Class<? extends Request>[] getParamTypes() {
        return paramTypes;
    }

    public void setParamTypes(Class<? extends Request>[] paramTypes) {
        this.paramTypes = paramTypes;
    }

    public Class<? extends Response> getReturnType() {
        return returnType;
    }

    public void setReturnType(Class<? extends Response> returnType) {
        this.returnType = returnType;
    }

    public MessageType getMessageType() {
        return messageType;
    }

    public void setMessageType(MessageType messageType) {
        this.messageType = messageType;
    }

    public boolean isCanMultithreaded() {
        return canMultithreaded;
    }

    public HandlerMethod setCanMultithreaded(boolean canMultithreaded) {
        this.canMultithreaded = canMultithreaded;
        return this;
    }

    @Override
    public String toString() {
        return "HandlerMethod{" +
                "cmd=" + cmd +
                ", instance=" + instance +
                ", method=" + method +
                ", paramTypes=" + Arrays.toString(paramTypes) +
                ", returnType=" + returnType +
                ", messageType=" + messageType +
                ", canMultithreaded=" + canMultithreaded +
                '}';
    }
}
