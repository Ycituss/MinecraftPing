package com.ycitus.mcping.timer;

public interface TimerController {

    void checkStatus();

    default void autoControlTimer() {
        new Thread(() -> {
            checkStatus();
        }).start();
    }

}
