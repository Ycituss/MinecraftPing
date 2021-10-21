package com.ycitus.mcping.timer;

import java.util.TimerTask;

//用于描述机器人的一个具体的时间任务
public abstract class RobotAbstractTimer extends TimerTask {

	private String timerName = null;
	private long firstTime = 0;
	private long delayTime = 0;

	public RobotAbstractTimer(String timerName, long firstTime, long delayTime) {
		super();
		this.timerName = timerName;
		this.firstTime = firstTime;
		this.delayTime = delayTime;

	}

	public long getDelayTime() {
		return delayTime;
	}

	public long getFirstTime() {
		return firstTime;
	}

	public String getTimerName() {
		return timerName;
	}


}
