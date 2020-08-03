package com.yude.game.common.dispatcher.event;

import com.yude.game.communication.dispatcher.IProducerWithTranslator;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @Author: HH
 * @Date: 2020/6/19 18:25
 * @Version: 1.0
 * @Declare:
 */
public class DisruptorLoadBalance {
    private AtomicInteger balance = new AtomicInteger(0);

    /**
     * 初始化完成后，只读
     */
    private List<IProducerWithTranslator> translatorList;


    public DisruptorLoadBalance(List<IProducerWithTranslator> translatorList) {
        this.translatorList = translatorList;
    }

    public void addTranslator(IProducerWithTranslator translator){
        translatorList.add(translator);
    }

    /**
     * 轮询获取 IProducerWithTranslator
     * @return
     */
    public IProducerWithTranslator getTranslatorByPolling(){
        int current = balance.get();
        for(;;){
            int next = (current + 1) % translatorList.size();
            if(balance.compareAndSet(current,next)){
                break;
            }
        }
        return translatorList.get(current);
    }
}
