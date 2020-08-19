package com.yude.game.common.dispatcher;

import cn.hutool.core.util.ClassUtil;
import com.yude.game.common.command.BaseCommandCode;
import com.yude.game.common.command.annotation.RequestCommand;
import com.yude.game.common.command.annotation.RequestController;
import com.yude.game.communication.dispatcher.IRequestMappingInfo;
import com.yude.protocol.common.MessageType;
import com.yude.protocol.common.request.Request;
import com.yude.protocol.common.response.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Author: HH
 * @Date: 2020/6/17 16:17
 * @Version: 1.0
 * @Declare:
 */
@PropertySource("classpath:config/core.properties")
@Component
public class RequestMappingInfo implements IRequestMappingInfo,ApplicationContextAware {
    private static final Logger log = LoggerFactory.getLogger(RequestMappingInfo.class);
    /**
     * commandCode -> HandlerMethod
     */
    private static final Map<Integer, HandlerMethod> methodMappingMap = new ConcurrentHashMap<>();

    private ApplicationContext applicationContext;

    @Value("${method.scaner.package}")
    private String packageName;

    @PostConstruct
    public void init() throws Exception {
        Set<Class<?>> classes = ClassUtil.scanPackageByAnnotation(packageName, RequestController.class);
        if (classes == null) {
            log.warn("--------没有提供游戏服务接口---------");
            return;
        }
        for (Class curClass : classes) {
            Method[] methods = curClass.getMethods();
            if (methods.length == 0) {
                log.warn("--------没有提供游戏服务方法---------");
                return;
            }
            for (Method method : methods) {
                RequestCommand annotation = method.getAnnotation(RequestCommand.class);
                if (annotation == null) {
                    continue;
                }
                Integer commandCode = annotation.value();
                if (methodMappingMap.get(commandCode) != null) {
                    StringBuilder sd = new StringBuilder();
                    sd.append("一个命令码不能绑定到多个方法,").append(annotation.value()).append(":");
                    sd.append(curClass.getClass());
                    sd.append("#").append(method.getName());
                    sd.append(" ,  ").append(methodMappingMap.get(annotation.value()).getMethod().getDeclaringClass());
                    /*sd.append("#").append(methodMappingMap.get(annotation.value()).getClass());*/
                    log.error(sd.toString());
                    throw new Exception(sd.toString());
                }

                MessageType messageType = annotation.messageType();
                Object bean = applicationContext.getBean(curClass);
                Class<? extends Request>[] parameterTypes = (Class<? extends Request>[]) method.getParameterTypes();
                Class<? extends Response> returnType = (Class<? extends Response>) method.getReturnType();
                boolean multithreaded = annotation.multithreaded();

                HandlerMethod handlerMethod = new HandlerMethod();
                handlerMethod.setCmd(commandCode);
                handlerMethod.setInstance(bean);
                handlerMethod.setMethod(method);
                handlerMethod.setParamTypes(parameterTypes);
                handlerMethod.setReturnType(returnType);
                handlerMethod.setMessageType(messageType);
                handlerMethod.setCanMultithreaded(multithreaded);
                methodMappingMap.put(commandCode,handlerMethod);

            }
        }
        log.info("------------------游戏接口加载完成：{}",methodMappingMap);
    }

    public static HandlerMethod getHandlerMethodByCmd(Integer cmd) {
        return methodMappingMap.get(cmd);
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    @Override
    public MessageType getMessageTypByCommand(Integer cmd) {
        HandlerMethod handlerMethod = methodMappingMap.get(cmd);
        if(cmd.equals(BaseCommandCode.HEART_BEAT)){
            return MessageType.HEARTBEAT;
        }
        if(handlerMethod != null){
           return handlerMethod.getMessageType();
        }
        log.warn("该请求接口不存在：cmd = {}",Integer.toHexString(cmd));
        return null;
    }

    @Override
    public boolean canMultithreaded(Integer cmd) {
        HandlerMethod handlerMethod = methodMappingMap.get(cmd);
        if(handlerMethod != null){
            return handlerMethod.isCanMultithreaded();
        }
        log.warn("该请求接口不存在：cmd = {}",Integer.toHexString(cmd));
        return false;
    }
}
