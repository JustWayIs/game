package com.yude.protocol.common.response;

import com.baidu.bjf.remoting.protobuf.annotation.EnableZigZap;
import com.baidu.bjf.remoting.protobuf.annotation.ProtobufClass;

/**
 * @Author: HH
 * @Date: 2020/6/29 20:16
 * @Version: 1.0
 * @Declare: 存在的意义主要是方便子类不用加注解，其子类需要提供无参构造器。这个isSuccess...跟对Controller的返回值的处理有关
 */
@ProtobufClass
@EnableZigZap
public class BaseResponse implements Response{
    @Override
    public boolean isSuccess() {
        return true;
    }
}
