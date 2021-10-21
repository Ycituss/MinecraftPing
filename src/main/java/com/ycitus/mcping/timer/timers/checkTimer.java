package com.ycitus.mcping.timer.timers;

import com.ycitus.mcping.timer.RobotAbstractTimer;
import com.ycitus.mcping.timer.TimerController;

public abstract class checkTimer extends RobotAbstractTimer implements TimerController {

    public checkTimer(String timerName, long firstTime, long delayTime) {
        super(timerName, firstTime, delayTime);
    }


    @Override
    public void run() {
        autoControlTimer();
    }

}
