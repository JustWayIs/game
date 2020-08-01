package com.yude.protocol.common.request;

/**
 * @Author: HH
 * @Date: 2020/7/7 18:25
 * @Version: 1.0
 * @Declare:
 */
public abstract class AbstractRequest implements Request{

    protected Long channelUserId;

    //要做取消快速出牌的话，需要在业务层知道消息类型：是超时执行还是玩家手动执行，手动执行就重置 玩家连续超时计数器
    //protected MessageType messageType;

    @Override
    public Long getUserIdByChannel() {
        return getChannelUserId();
    }

    @Override
    public void setUserIdByChannel(Long userId) {
        setChannelUserId(userId);
    }

    public Long getChannelUserId() {
        return channelUserId;
    }

    public void setChannelUserId(Long channelUserId) {
        this.channelUserId = channelUserId;
    }

    @Override
    public String toString() {
        return "AbstractRequest{" +
                "channelUserId=" + channelUserId +
                '}';
    }
}
