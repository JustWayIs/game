package com.yude.game.common.model;

/**
 * @Author: HH
 * @Date: 2020/6/17 17:07
 * @Version: 1.0
 * @Declare:
 */
public abstract class AbstractSeatModel {
    protected Player player;
    protected int posId;
    /**
     * 是否托管
     */
    protected boolean isAutoOperation = false;
    protected volatile int serialTimeoutCount = 0;


    public AbstractSeatModel(Player player, int posId) {
        this.player = player;
        this.posId = posId;
        init();
    }

    public abstract void init();

    public abstract void clean();

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public int getPosId() {
        return posId;
    }

    public void setPosId(int posId) {
        this.posId = posId;
    }

    public Long getUserId(){
        return player.getUserId();
    }

    public boolean isAutoOperation() {
        return isAutoOperation;
    }

    public void serialTimeoutCountAdd(int limit) {
        serialTimeoutCount++;
        if (serialTimeoutCount > limit) {

        }
    }

    public AbstractSeatModel setAutoOperation(boolean autoOperation) {
        isAutoOperation = autoOperation;
        return this;
    }
}
