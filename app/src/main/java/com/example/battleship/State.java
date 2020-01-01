package com.example.battleship;

import org.apache.commons.lang3.StringUtils;

public class State {
    String hostBoard;
    String board;
    boolean hostChoice;
    int hostDestroyed;
    int destroyed;

    public State() {
        this.hostBoard = StringUtils.repeat("0", 100);
        this.board = StringUtils.repeat("0", 100);
        hostChoice = true;
        hostDestroyed = 0;
        destroyed = 0;
    }

    public State(String hostBoard, String board, boolean hostChoice,
                 int hostDestroyed, int destroyed) {
        this.hostBoard = hostBoard;
        this.board = board;
        this.hostChoice = hostChoice;
        this.hostDestroyed = hostDestroyed;
        this.destroyed = destroyed;
    }
}
