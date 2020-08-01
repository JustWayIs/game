package com.yude.protocol.common.request;

/**
 * @Author: HH
 * @Date: 2020/6/22 14:55
 * @Version: 1.0
 * @Declare: 请求的业务数据接口:成员变量需要添加 get 和 set 方法
 */
public interface Request {
    Long getUserIdByChannel();

    void setUserIdByChannel(Long userId);
}
