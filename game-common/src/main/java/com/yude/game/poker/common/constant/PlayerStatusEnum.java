package com.yude.game.poker.common.constant;

/**
 * @Author: HH
 * @Date: 2020/7/7 20:43
 * @Version: 1.0
 * @Declare: 不应该放在这一层的
 */
public enum PlayerStatusEnum implements Status {
    /**
     * 游戏外
     */
    FREE_TIME,
    /**
     * 匹配中
     */
    MATCHING,
    /**
     * 游戏中
     */
    GAMEMING,
    ;



    @Override
    public int status() {

        return this.ordinal();
    }
}
