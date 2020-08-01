package com.yude.protocol.common.response;

/**
 * @Author: HH
 * @Date: 2020/6/24 17:03
 * @Version: 1.0
 * @Declare:
 */
public class HeartBeatResponse extends BaseResponse {
    /**
     * ServerChannelHandlerrInitializer里面定义的保持一致
     */
    private  int time = 2;

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }

    @Override
    public String toString() {
        return "HeartBeatResponse{" +
                "time=" + time +
                '}';
    }
}
