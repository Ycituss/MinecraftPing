package com.ycitus.mcping.command.commands;

import com.ycitus.mcping.command.RobotCommand;
import com.ycitus.mcping.command.RobotCommandChatType;
import com.ycitus.mcping.command.RobotCommandUser;
import com.ycitus.mcping.files.FileManager;
import com.ycitus.mcping.framework.MessageManager;
import net.mamoe.mirai.message.data.MessageChain;

public class serverSetCommand extends RobotCommand {
    public serverSetCommand(String rule) {
        super(rule);
        getRange().add(RobotCommandChatType.GROUP_CHAT);
        getRange().add(RobotCommandChatType.GroupMessageSync);

        getUser().add(RobotCommandUser.BOT_ADMINISTRATOR);
        getUser().add(RobotCommandUser.GROUP_OWNER);
        getUser().add(RobotCommandUser.GROUP_ADMINISTRATOR);
    }

    @Override
    public void runCommand(int msgType, int time, long fromGroup, long fromQQ, MessageChain messageChain) {
        String msg = messageChain.contentToString();
        String[] strings = msg.split(" ");
        if (strings.length != 2) {
            MessageManager.sendMessageBySituation(fromGroup, fromQQ, "输入格式有误");
            return;
        }
        FileManager.applicationConfig_File.getSpecificDataInstance()
                    .Server.serverList.put(fromGroup, strings[1]);
        FileManager.applicationConfig_File.saveFile();
        FileManager.applicationConfig_File.reloadFile();
        MessageManager.sendMessageBySituation(fromGroup, fromQQ, "设置成功");
    }
}
