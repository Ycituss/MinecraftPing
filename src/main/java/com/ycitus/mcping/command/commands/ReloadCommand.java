package com.ycitus.mcping.command.commands;

import com.ycitus.mcping.command.RobotCommand;
import com.ycitus.mcping.command.RobotCommandChatType;
import com.ycitus.mcping.command.RobotCommandUser;
import com.ycitus.mcping.debug.LoggerManager;
import com.ycitus.mcping.files.FileManager;
import com.ycitus.mcping.framework.MessageManager;
import net.mamoe.mirai.message.data.MessageChain;

//从本地重新加载配置文件到内存
public class ReloadCommand extends RobotCommand {

	public ReloadCommand(String rule) {
		super(rule);
		getRange().add(RobotCommandChatType.FRIEND_CHAT);
		getRange().add(RobotCommandChatType.GROUP_TEMP_CHAT);
		getRange().add(RobotCommandChatType.GROUP_CHAT);
		getRange().add(RobotCommandChatType.STRANGER_CHAT);
		getRange().add(RobotCommandChatType.GroupMessageSync);

		getUser().add(RobotCommandUser.BOT_ADMINISTRATOR);
	}

	@Override
	public void runCommand(int msgType, int time, long fromGroup, long fromQQ, MessageChain messageChain) {

		try {
			FileManager.applicationConfig_File.reloadFile();
		} catch (IllegalArgumentException e) {
			LoggerManager.reportException(e);
		}

		MessageManager.sendMessageBySituation(fromGroup, fromQQ, "Reload Configs Successfully!");
	}
}
