package com.yude.protocol.common;

/**
 * @Author: HH
 * @Date: 2020/6/11 15:08
 * @Version 1.0
 * @Declare 消息类型/事件类型
 */
public enum  MessageType {
    /**
     * 登录
     */
    LOGIN(0x00,"login"),
    /**
     * 心跳
     */
    HEARTBEAT(0x01,"heartBeat"),
    /**
     * 业务
     */
    SERVICE(0x02,"service"),

    /**
     * 因为超时而发布事件
     */
    TIMEOUT(0x03,"timeout"),

    /**
     * 推送消息
     */
    PUSH_MESSAGE(0xFF,"push-message");


    private int type;
    private String name;

    MessageType(int type,String name) {
        this.type = type;
        this.name = name;
    }

    public static MessageType matchMessageTypeInstance(int type) throws Exception{
        for(MessageType messageType : MessageType.values()){
            if(messageType.type == type){
                return messageType;
            }
        }
        throw new Exception("不存在的消息类型");
    }

    public int value() {
        return type;
    }

    public String getName(){
        return name;
    }

    public int getType() {
        return type;
    }

    public void setType(byte type) {
        this.type = type;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "MessageType{" +
                "type=" + type +
                ", name='" + name + '\'' +
                '}';
    }

}
