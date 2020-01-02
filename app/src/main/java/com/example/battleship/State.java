package com.example.battleship;

import org.apache.commons.lang3.StringUtils;

import java.util.UUID;

public class State {
    String hostBoard;
    String board;
    boolean hostChoice;
    int hostDestroyed;
    int destroyed;
    String hostName;
    String name;
    String gameId;

    public State() {
        this.hostBoard = StringUtils.repeat("0", 100);
        this.board = StringUtils.repeat("0", 100);
        hostChoice = true;
        hostDestroyed = 0;
        destroyed = 0;
        gameId = gameId.substring(0, gameId.length() - 10);
    }

    public State(String hostBoard, String board, boolean hostChoice,
                 int hostDestroyed, int destroyed) {
        this.hostBoard = hostBoard;
        this.board = board;
        this.hostChoice = hostChoice;
        this.hostDestroyed = hostDestroyed;
        this.destroyed = destroyed;
        gameId = UUID.randomUUID().toString();
    }

    public void setHostName(String hostName) {
        this.hostName = hostName;
    }

    public void setName(String name) {
        this.name = name;
    }
}
