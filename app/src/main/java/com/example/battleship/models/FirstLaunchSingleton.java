package com.example.battleship.models;

public final class FirstLaunchSingleton {
    private boolean isFirstLaunch;
    private static FirstLaunchSingleton instance;

    private FirstLaunchSingleton() {
        isFirstLaunch = true;
    }

    public static FirstLaunchSingleton getInstance() {
        if (instance == null) {
            instance = new FirstLaunchSingleton();
        }
        return instance;
    }

    public boolean getFirstLaunchStatus() {
        return isFirstLaunch;
    }

    public void setFirstLaunchStatus(boolean status) {
        isFirstLaunch = status;
    }
}

