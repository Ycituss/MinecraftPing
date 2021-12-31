package com.ycitus.mcping.command.commands;

import com.ycitus.mcping.command.RobotCommand;
import com.ycitus.mcping.command.RobotCommandChatType;
import com.ycitus.mcping.command.RobotCommandUser;
import com.ycitus.mcping.files.FileManager;
import com.ycitus.mcping.framework.MessageManager;
import com.ycitus.mcping.timer.timers.checkStatus_Timer;
import net.mamoe.mirai.message.data.MessageChain;

import java.util.*;

public class McPlayerCommand extends RobotCommand {

    public McPlayerCommand(String rule) {
        super(rule);
        getRange().add(RobotCommandChatType.GROUP_CHAT);
        getRange().add(RobotCommandChatType.GroupMessageSync);

        getUser().add(RobotCommandUser.BOT_ADMINISTRATOR);
        getUser().add(RobotCommandUser.GROUP_OWNER);
        getUser().add(RobotCommandUser.GROUP_ADMINISTRATOR);
        getUser().add(RobotCommandUser.NORMAL_USER);
    }

    @Override
    public void runCommand(int msgType, int time, long fromGroup, long fromQQ, MessageChain messageChain) {
        if (fromGroup != 264343654 && fromGroup != 966348161) return;
        String sendMsg = "";
        HashMap<String, Integer> map = FileManager.applicationConfig_File.getSpecificDataInstance()
                .PlayerDate.PlayerTimeNow;

        if (checkStatus_Timer.getInstance().getStatus() < 5) {
            sendMsg = "服务器离线，无法检测。";
            MessageManager.sendMessageToQQGroup(fromGroup, sendMsg);
        }

        if (map.isEmpty()) {
            sendMsg = sendMsg + "今天没有人上线(ŎдŎ；)";
        } else {
            sendMsg = sendMsg + "今天大家的在线情况为：";
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

        MessageManager.sendMessageToQQGroup(fromGroup, sendMsg);
    }
}
