package com.ycitus.mcping.command.commands;

import com.ycitus.mcping.command.RobotCommand;
import com.ycitus.mcping.command.RobotCommandChatType;
import com.ycitus.mcping.command.RobotCommandUser;
import com.ycitus.mcping.files.FileManager;
import com.ycitus.mcping.framework.MessageManager;
import net.mamoe.mirai.message.data.MessageChain;

import java.util.ArrayList;
import java.util.HashMap;

public class ServerListCommand extends RobotCommand {
    public ServerListCommand(String rule) {
        super(rule);
        getRange().add(RobotCommandChatType.GROUP_CHAT);
        getRange().add(RobotCommandChatType.GroupMessageSync);

        getUser().add(RobotCommandUser.BOT_ADMINISTRATOR);
        getUser().add(RobotCommandUser.GROUP_OWNER);
        getUser().add(RobotCommandUser.GROUP_ADMINISTRATOR);
    }

    @Override
    public void runCommand(int msgType, int time, long fromGroup, long fromQQ, MessageChain messageChain) {
        if (!FileManager.applicationConfig_File.getSpecificDataInstance().Server.serverList.containsKey(fromGroup)){
            MessageManager.sendMessageBySituation(fromGroup, fromQQ, "当前群未绑定服务器");
        } else {
            HashMap<Integer, ArrayList<String>> list = FileManager.applicationConfig_File.getSpecificDataInstance().Server.serverList.get(fromGroup);
            String msg = "编号 服务器    备注";
            for (int num : list.keySet()) {
                msg = msg + "\n" + num + "  " + list.get(num).get(0) + "  " + list.get(num).get(1);
            }
            MessageManager.sendMessageToQQGroup(fromGroup, msg);
        }
    }

}
