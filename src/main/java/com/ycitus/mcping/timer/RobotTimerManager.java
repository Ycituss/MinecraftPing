package com.ycitus.mcping.timer;

import com.ycitus.mcping.debug.LoggerManager;
import com.ycitus.mcping.timer.timers.checkStatus_Timer;
import com.ycitus.mcping.timer.timers.checkVersion_Timer;

import java.util.ArrayList;
import java.util.Timer;

//用于管理机器人的所有Timer
public class RobotTimerManager extends Timer {

	private static final ArrayList<RobotAbstractTimer> tasks = new ArrayList<RobotAbstractTimer>();

	private static RobotTimerManager instance = null;

	public void init() {
		// 注册Timer
		LoggerManager.logDebug("TimerSystem", "Register Timers.");
//		tasks.add(checkStatus_Timer.getInstance());
		tasks.add(checkVersion_Timer.getInstance());

		// 启动所有Timer
		LoggerManager.logDebug("TimerSystem", "Start Timers.");
		registerAll();
	}


	public static RobotTimerManager getInstance() {
		if (instance == null) {
			instance = new RobotTimerManager();
		}
		return instance;
	}

	private RobotTimerManager() {
		this.init();
	}

	// 注册/开始所有任务
	public void registerAll() {
		tasks.forEach(task -> schedule(task, task.getFirstTime(), task.getDelayTime()));
	}

}
