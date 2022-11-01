package com.ycitus.mcping.command.commands;

import com.ycitus.mcping.MinecraftPing;
import com.ycitus.mcping.command.RobotCommand;
import com.ycitus.mcping.command.RobotCommandChatType;
import com.ycitus.mcping.command.RobotCommandUser;
import com.ycitus.mcping.files.FileManager;
import com.ycitus.mcping.framework.MessageManager;
import com.ycitus.mcping.utils.BotList;
import com.ycitus.mcping.utils.FileUtil;
import com.ycitus.mcping.minecraftserverping.MCPing;
import com.ycitus.mcping.minecraftserverping.Util;
import com.ycitus.mcping.utils.Mc;
import com.ycitus.mcping.utils.Srv;
import net.mamoe.mirai.message.data.Image;
import net.mamoe.mirai.message.data.MessageChain;
import net.mamoe.mirai.utils.ExternalResource;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;

public class MinecraftPingCommand extends RobotCommand {
    public MinecraftPingCommand(String rule) {
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

        String msg = messageChain.contentToString();
        String[] strings = msg.split(" ");
        String host = new String();
        MCPing ping = null;

        String sendMessage = new String();

        String path = FileUtil.getJavaRunPath() + "data" + File.separator + "MinecraftPing" + File.separator;
        if (!new File(path).exists()) new File(path).mkdirs();

        if (strings.length >= 2){
            host = strings[1];
        }else {
            HashMap<Long, String> serverList = FileManager.applicationConfig_File
                    .getSpecificDataInstance().Server.serverList;
            if (!serverList.containsKey(fromGroup)){
                MessageManager.sendMessageBySituation(fromGroup, fromQQ, "当前群未绑定服务器");
                return;
            }

            host = serverList.get(fromGroup);
        }

        File saveSrc = new File(path + host + ".png");

        int port = 25565;
        Srv srv = Srv.getSrv(host,Mc.MC_SRV);
        if (srv != null) {
            host = srv.getSrvHost();
            port = srv.getSrvPort();
        }
        try {
            ping = MCPing.getMotd(host, port);
        } catch (IOException e) {
            if (FileManager.applicationConfig_File.getSpecificDataInstance().Server.isShowFavicon){
                if (saveSrc.exists()){
                    Image uploadImage = ExternalResource.uploadAsImage(saveSrc,
                            MinecraftPing.getCurrentBot().getGroups().stream().findAny().get());
                    sendMessage = sendMessage + "[mirai:image:" + uploadImage.getImageId() + "]\n\n";
                }else {
                    sendMessage = sendMessage + "[mirai:image:{AFF1174C-E94C-D340-AB05-3EE448F60443}.png]\n\n";
                }
            }
            sendMessage = sendMessage + "[ 地址 ] " + host + "\n" + "[ 状态 ] " + "\uD83D\uDD34\n";
            MessageManager.sendMessageToQQGroup(fromGroup, sendMessage);
            return;
        }

        List<String> playerlist = ping.getPlayerList();

        if (FileManager.applicationConfig_File.getSpecificDataInstance().Server.isShowFavicon){
            try {
                if (ping.getFavicon().equals("null")){
                    if (saveSrc.exists()){
                        Image uploadImage = ExternalResource.uploadAsImage(saveSrc,
                                MinecraftPing.getCurrentBot().getGroups().stream().findAny().get());
                        sendMessage = sendMessage + "[mirai:image:" + uploadImage.getImageId() + "]\n\n";
                    }else {
                        sendMessage = sendMessage + "[mirai:image:{AFF1174C-E94C-D340-AB05-3EE448F60443}.png]\n\n";
                    }
                } else {
                    if (!saveSrc.exists()) saveSrc.createNewFile();
                    try {
                        ImageIO.write(Util.base64ToImage(ping.getFavicon()), "png", saveSrc);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    Image uploadImage = ExternalResource.uploadAsImage(saveSrc,
                            MinecraftPing.getCurrentBot().getGroups().stream().findAny().get());
                    sendMessage = sendMessage + "[mirai:image:" + uploadImage.getImageId() + "]\n\n";
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        List<String> botList = BotList.getBotList(playerlist);
        sendMessage = sendMessage + "[ 地址 ] " + host + "\n"
                + "[ 状态 ] " + "\uD83D\uDFE2\n"
                + "[ 描述 ] " +ping.getDescription() + "\n"
                + "[ 版本 ] " + ping.getVersion_name() + "\n"
                + "[ 人数 ] " + (ping.getOnline_players() - botList.size()) + "/" + ping.getMax_players() + "\n";
        if (botList.size() > 0 ){
            sendMessage += "[ 在线bot ] ";
            for (String bot : botList) {
                sendMessage = sendMessage + bot + ",";
            }
        }
//        if (playerlist.size() > 0){
//            sendMessage += "[ 在线玩家 ] ";
//            for (String player : playerlist) {
//                sendMessage = sendMessage + player + ",";
//            }
//        }
        sendMessage = sendMessage.substring(0, sendMessage.length() - 1);
        MessageManager.sendMessageToQQGroup(fromGroup, sendMessage);
    }
}
