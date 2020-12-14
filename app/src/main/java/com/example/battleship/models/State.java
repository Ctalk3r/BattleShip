package com.example.battleship.models;

import org.apache.commons.lang3.StringUtils;

import java.util.Date;

public class State {
    private String hostBoard;
    private String board;
    private int hostDestroyed;
    private int destroyed;
    private String hostName;
    private String name;
    private Date date;

    public State() {
        this.hostBoard = StringUtils.repeat("0", 100);
        this.board = StringUtils.repeat("0", 100);
        hostDestroyed = 0;
        destroyed = 0;
        date = new Date(0);
    }

    public State(String hostBoard, String board,
                 int hostDestroyed, int destroyed) {
        this.hostBoard = hostBoard;
        this.board = board;
        this.hostDestroyed = hostDestroyed;
        this.destroyed = destroyed;
        this.date = new Date();
    }

    public void setHostName(String hostName) {
        this.hostName = hostName;
    }

    public String getHostName() {
        return hostName;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setHostDestroyed(int hostDestroyed) {
        this.hostDestroyed = hostDestroyed;
    }

    public int getHostDestroyed() {
        return hostDestroyed;
    }

    public void setDestroyed(int destroyed) {
        this.destroyed = destroyed;
    }

    public int getDestroyed() {
        return destroyed;
    }

    public void setHostBoard(String hostBoard) {
        this.hostBoard = hostBoard;
    }

    public String getHostBoard() {
        return hostBoard;
    }

    public void setBoard(String board) {
        this.board = board;
    }

    public String getBoard() {
        return board;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Date getDate() {
        return date;
    }
}
