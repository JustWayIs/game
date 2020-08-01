package com.yude.game.poker.common.mode.seat;

/**
 * @Author: HH
 * @Date: 2020/6/17 17:07
 * @Version: 1.0
 * @Declare:
 */
public abstract class AbstractSeatModel {
    protected Player player;
    protected int posId;


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
}
