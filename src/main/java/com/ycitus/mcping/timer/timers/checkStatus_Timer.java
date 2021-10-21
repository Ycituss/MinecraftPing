package com.ycitus.mcping.timer.timers;

import com.ycitus.mcping.MinecraftPing;
import com.ycitus.mcping.debug.LoggerManager;
import com.ycitus.mcping.files.FileManager;
import com.ycitus.mcping.framework.MessageManager;
import com.ycitus.mcping.utils.DateUtil;
import com.ycitus.mcping.utils.FileUtil;
import com.ycitus.mcping.minecraftserverping.MCPing;
import com.ycitus.mcping.utils.Mc;
import com.ycitus.mcping.utils.Srv;
import net.mamoe.mirai.message.data.Image;
import net.mamoe.mirai.utils.ExternalResource;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class checkStatus_Timer extends checkTimer {

	private static checkStatus_Timer instance = null;
	private static int status = 6;
	private static int lastHour = 0;

	public checkStatus_Timer(String timerName, long firstTime, long delayTime) {
		super(timerName, firstTime, delayTime);
	}

	public static checkStatus_Timer getInstance() {
		if (instance == null) {
			instance = new checkStatus_Timer(
					"CheckStatus", 1000 * 5, 1000 * 60);
		}
		return instance;
	}


	@Override
	public void checkStatus() {

		int nowHour = DateUtil.getNowHour();
		int nowMinute = DateUtil.getNowMinute();

		if (nowHour == FileManager.applicationConfig_File.getSpecificDataInstance().TimeSet.temp_hour &&
				nowMinute == FileManager.applicationConfig_File.getSpecificDataInstance().TimeSet.temp_min) {
			FileManager.applicationConfig_File.getSpecificDataInstance().PlayerDate.PlayerTimeTemp.clear();
			HashMap<String, Integer> map = FileManager.applicationConfig_File.getSpecificDataInstance()
					.PlayerDate.PlayerTimeNow;
			for (String key : map.keySet()) {
				FileManager.applicationConfig_File.getSpecificDataInstance()
						.PlayerDate.PlayerTimeTemp.put(key, map.get(key));
			}
			FileManager.applicationConfig_File.getSpecificDataInstance().PlayerDate.PlayerTimeNow.clear();
			FileManager.applicationConfig_File.saveFile();
			FileManager.applicationConfig_File.reloadFile();

			LoggerManager.logDebug("playerStatus", "playerDate save successfully!", true);
		}

		if (!FileManager.applicationConfig_File.getSpecificDataInstance().Server.isCheckStatus) return;

		if (nowHour == FileManager.applicationConfig_File.getSpecificDataInstance().TimeSet.sentence_hour &&
				nowMinute == FileManager.applicationConfig_File.getSpecificDataInstance().TimeSet.sentence_min) {
			String sendMsg = "早安，今天是" + DateUtil.getNowYear() + "年" + DateUtil.getNowMonth()
					+ "月" + DateUtil.getNowDay() + "日！\n\n";

			HashMap<String, Integer> map = FileManager.applicationConfig_File.getSpecificDataInstance()
					.PlayerDate.PlayerTimeTemp;

			if (map.isEmpty()) {
				sendMsg = sendMsg + "昨天没有人上线(ŎдŎ；)";
			} else {
				sendMsg = sendMsg + "昨天大家的在线时长为：";
				List<HashMap.Entry<String, Integer>> list = new ArrayList<Map.Entry<String, Integer>>(map.entrySet());
				Collections.sort(list, new Comparator<Map.Entry<String, Integer>>() {
					//降序排序
					@Override
					public int compare(Map.Entry<String, Integer> o1, Map.Entry<String, Integer> o2) {
						//return o1.getValue().compareTo(o2.getValue());
						return o2.getValue().compareTo(o1.getValue());
					}
				});

				for (Map.Entry<String, Integer> mapping : list) {
					sendMsg = sendMsg + "\n" + String.format("%-16s", mapping.getKey()) + mapping.getValue() + "min";
				}
			}


			//发送信息到指定群
			MessageManager.sendMessageToQQGroup(966348161, sendMsg);
			MessageManager.sendMessageToQQGroup(264343654, sendMsg);


			LoggerManager.logDebug("playerStatus", "Send playerDate successfully!", true);
		}

		if (nowMinute%2 == 0){
			if (status == 0 && (nowHour < lastHour + 3 || nowHour + 21 < lastHour)) {
				LoggerManager.logDebug("ServerStatus", "Detection pause.", true);
				return;
			} else if (status == 0) {
				status = 6;
			}
			//需要检测的服务器,
			String host = "";
			String sendMessage = new String();
			String path = FileUtil.getJavaRunPath() + "data" + File.separator + "MinecraftPing" + File.separator;
			if (!new File(path).exists()) new File(path).mkdirs();
			File saveSrc = new File(path + host + ".png");
			int port = 25565;
			Srv srv = Srv.getSrv(host, Mc.MC_SRV);
			if (srv != null) {
				host = srv.getSrvHost();
				port = srv.getSrvPort();
			}

			try {
				MCPing ping = MCPing.getMotd(host, port);
				status = 6;

				HashMap<String, Integer> map = FileManager.applicationConfig_File.getSpecificDataInstance().PlayerDate.PlayerTimeNow;
				List<String> playerList = ping.getPlayerList();
				if (!playerList.isEmpty()) {
					for (String player : playerList) {
						if (map.containsKey(player)) {
							FileManager.applicationConfig_File.getSpecificDataInstance()
									.PlayerDate.PlayerTimeNow.put(player, map.get(player) + 2);
						}else {
							FileManager.applicationConfig_File.getSpecificDataInstance()
									.PlayerDate.PlayerTimeNow.put(player, 2);
						}
					}
					FileManager.applicationConfig_File.saveFile();
					FileManager.applicationConfig_File.reloadFile();
				}

				LoggerManager.logDebug("ServerStatus", "Minecraft Server Online.", true);
			} catch (IOException e) {
				status--;
				LoggerManager.logDebug("ServerStatus", "Minecraft Server Offline.", true);
			}

			if (status == 0) {
				if (saveSrc.exists()){
					Image uploadImage = ExternalResource.uploadAsImage(saveSrc,
							MinecraftPing.getCurrentBot().getGroups().stream().findAny().get());
					sendMessage = sendMessage + "[mirai:image:" + uploadImage.getImageId() + "]\n\n";
				}else {
					sendMessage = sendMessage + "[mirai:image:{AFF1174C-E94C-D340-AB05-3EE448F60443}.png]\n\n";
				}
				sendMessage = sendMessage + "[ 地址 ] " + host + "\n" + "[ 状态 ] " + "\uD83D\uDD34\n";

				//提醒管理人员服务器发生故障
				MessageManager.sendMessageBySituation(264343654L, 1390435486L, sendMessage);
				MessageManager.sendMessageBySituation(966348161L, 2799282971L, sendMessage);
				lastHour = nowHour;
			}
		}

	}
}
