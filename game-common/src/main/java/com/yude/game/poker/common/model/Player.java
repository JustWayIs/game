package com.yude.game.poker.common.model;

import com.yude.game.poker.common.constant.Status;

/**
 * @Author: HH
 * @Date: 2020/6/17 16:46
 * @Version: 1.0
 * @Declare:
 */
public class Player {
    private long userId;
    private String nickName;
    private String headUrl;
    private long score;

    private Status status;

    public Player(){};

    public Player(long userId,String nickName,String headUrl,long score){
        this.userId = userId;
        this.nickName = nickName;
        this.headUrl = headUrl;
        this.score = score;
    }

    public Player(long userId,String nickName,String headUrl,long score,Status status){
        this.userId = userId;
        this.nickName = nickName;
        this.headUrl = headUrl;
        this.score = score;
        this.status = status;
    }

    public void scoreSettle(int score){
        this.score += score;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getHeadUrl() {
        return headUrl;
    }

    public void setHeadUrl(String headUrl) {
        this.headUrl = headUrl;
    }

    public long getScore() {
        return score;
    }

    public void setScore(long score) {
        this.score = score;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "Player{" +
                "userId=" + userId +
                ", nickName='" + nickName + '\'' +
                ", headUrl='" + headUrl + '\'' +
                ", score=" + score +
                ", status=" + status +
                '}';
    }
}
