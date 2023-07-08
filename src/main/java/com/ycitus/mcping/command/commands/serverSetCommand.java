package com.ycitus.mcping.command.commands;

import com.ycitus.mcping.command.RobotCommand;
import com.ycitus.mcping.command.RobotCommandChatType;
import com.ycitus.mcping.command.RobotCommandUser;
import com.ycitus.mcping.files.FileManager;
import com.ycitus.mcping.framework.MessageManager;
import net.mamoe.mirai.message.data.MessageChain;

import java.util.ArrayList;
import java.util.HashMap;

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
        if (strings.length > 3) {
            MessageManager.sendMessageBySituation(fromGroup, fromQQ, "输入格式有误");
            return;
        }
        int listNum = 0;
        if (!strings[0].replaceAll("serverset", "").isEmpty()) {
            listNum = Integer.valueOf(strings[0].replaceAll("serverset", ""));
        }
        ArrayList<String> serverAndSo = new ArrayList<String>();
        serverAndSo.add(strings[1]);
        if (strings.length == 3) serverAndSo.add(strings[2]);
        else serverAndSo.add("\uD83D\uDFE2");
        if (!FileManager.applicationConfig_File.getSpecificDataInstance().Server.serverList.containsKey(fromGroup)) {
            HashMap<Integer, ArrayList<String>> list = new HashMap<Integer, ArrayList<String>>();
            list.put(listNum, serverAndSo);
            FileManager.applicationConfig_File.getSpecificDataInstance().Server.serverList.put(fromGroup, list);
        } else {
            FileManager.applicationConfig_File.getSpecificDataInstance().Server.serverList.get(fromGroup).put(listNum, serverAndSo);
        }
        FileManager.applicationConfig_File.saveFile();
        FileManager.applicationConfig_File.reloadFile();
        MessageManager.sendMessageBySituation(fromGroup, fromQQ, "设置成功");
    }
}
