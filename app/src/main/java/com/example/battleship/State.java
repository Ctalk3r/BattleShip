package com.example.battleship;

import org.apache.commons.lang3.StringUtils;

public class State {
    String hostBoard;
    String board;
    int hostDestroyed;
    int destroyed;
    String hostName;
    String name;

    public State() {
        this.hostBoard = StringUtils.repeat("0", 100);
        this.board = StringUtils.repeat("0", 100);
        hostDestroyed = 0;
        destroyed = 0;
    }

    public State(String hostBoard, String board,
                 int hostDestroyed, int destroyed) {
        this.hostBoard = hostBoard;
        this.board = board;
        this.hostDestroyed = hostDestroyed;
        this.destroyed = destroyed;
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
}
