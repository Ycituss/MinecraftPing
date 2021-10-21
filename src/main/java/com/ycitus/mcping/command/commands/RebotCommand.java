package com.ycitus.mcping.command.commands;

import com.ycitus.mcping.MinecraftPing;
import com.ycitus.mcping.command.RobotCommand;
import com.ycitus.mcping.command.RobotCommandChatType;
import com.ycitus.mcping.command.RobotCommandManager;
import com.ycitus.mcping.command.RobotCommandUser;
import com.ycitus.mcping.files.FileManager;
import com.ycitus.mcping.framework.MessageManager;
import net.mamoe.mirai.message.data.MessageChain;

public class RebotCommand extends RobotCommand {
    public RebotCommand(String rule) {
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

        MinecraftPing.commandManager = new RobotCommandManager();
        FileManager.applicationConfig_File.reloadFile();

        MessageManager.sendMessageBySituation(fromGroup, fromQQ, "Rebot Successfully!");
    }
}
