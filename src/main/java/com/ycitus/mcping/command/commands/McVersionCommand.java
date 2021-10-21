package com.ycitus.mcping.command.commands;

import com.ycitus.mcping.MinecraftPing;
import com.ycitus.mcping.command.RobotCommand;
import com.ycitus.mcping.command.RobotCommandChatType;
import com.ycitus.mcping.command.RobotCommandUser;
import com.ycitus.mcping.debug.LoggerManager;
import com.ycitus.mcping.files.FileManager;
import com.ycitus.mcping.framework.MessageManager;
import com.ycitus.mcping.utils.McVersion;
import com.ycitus.mcping.utils.SaveHtml2Image;
import net.mamoe.mirai.message.data.Image;
import net.mamoe.mirai.message.data.MessageChain;
import net.mamoe.mirai.utils.ExternalResource;

import java.io.FileInputStream;
import java.io.IOException;

public class McVersionCommand extends RobotCommand {
    public McVersionCommand(String rule) {
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

        String version = "";
        String url = "";

        if (messageChain.contentToString().equals("mcversion")){
            version = FileManager.applicationConfig_File.getSpecificDataInstance().Server.SnapshotVersion;
            url = FileManager.applicationConfig_File.getSpecificDataInstance().Server.SnapshotVersionUrl;
            if (FileManager.applicationConfig_File.getSpecificDataInstance().Server.isRelease) {
                version = FileManager.applicationConfig_File.getSpecificDataInstance().Server.ReleaseVersion;
                url = FileManager.applicationConfig_File.getSpecificDataInstance().Server.ReleaseVersionUrl;
            }
            String msg = "当前Java版minecraft最新版本为: " + version;
            msg = msg + "\n详细信息请查看" + url;
            MessageManager.sendMessageToQQGroup(fromGroup, msg);
            return;
        } else if (messageChain.contentToString().equals("mcversiontest")){
            McVersion mcVersion = new McVersion();
            version = mcVersion.getSnapshotVersion();
            url = mcVersion.getSnapshotVersionUrl();
            if (FileManager.applicationConfig_File.getSpecificDataInstance().Server.isRelease){
                version = mcVersion.getReleaseVersion();
                url = mcVersion.getReleaseVersionUrl();
            }
            String msg = "当前Java版minecraft最新版本为: " + version;
            msg = msg + "\n详细信息请查看" + url;
            MessageManager.sendMessageToQQGroup(fromGroup, msg);
            return;
        } else if (messageChain.contentToString().equals("mcversionjpg")){
            version = FileManager.applicationConfig_File.getSpecificDataInstance().Server.SnapshotVersion;
            url = FileManager.applicationConfig_File.getSpecificDataInstance().Server.SnapshotVersionUrl;
            if (FileManager.applicationConfig_File.getSpecificDataInstance().Server.isRelease) {
                version = FileManager.applicationConfig_File.getSpecificDataInstance().Server.ReleaseVersion;
                url = FileManager.applicationConfig_File.getSpecificDataInstance().Server.ReleaseVersionUrl;
            }
            LoggerManager.logDebug("McVersion", "url=" + url, true);
            SaveHtml2Image saveHtml2Image = new SaveHtml2Image();
            FileInputStream is = null;
            try {
                is = new FileInputStream(saveHtml2Image.html2Image(url, version));
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            Image uploadImage = ExternalResource.uploadAsImage(is,
                    MinecraftPing.getCurrentBot().getGroups().stream().findAny().get());
            String sendMessage =  "[mirai:image:" + uploadImage.getImageId() + "]";
            MessageManager.sendMessageToQQGroup(fromGroup, sendMessage);
            return;
        }





    }

}
