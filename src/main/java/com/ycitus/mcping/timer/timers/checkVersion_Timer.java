package com.ycitus.mcping.timer.timers;

import com.ycitus.mcping.debug.LoggerManager;
import com.ycitus.mcping.files.FileManager;
import com.ycitus.mcping.framework.BotManager;
import com.ycitus.mcping.framework.MessageManager;
import com.ycitus.mcping.utils.DateUtil;
import com.ycitus.mcping.utils.McVersion;
import net.mamoe.mirai.contact.ContactList;
import net.mamoe.mirai.contact.Group;

import java.util.ArrayList;
import java.util.HashMap;

public class checkVersion_Timer extends checkTimer{

    private static checkVersion_Timer instance = null;

    public checkVersion_Timer(String timerName, long firstTime, long delayTime) {
        super(timerName, firstTime, delayTime);
    }

    public static checkVersion_Timer getInstance() {
        if (instance == null) {
            instance = new checkVersion_Timer(
                    "CheckVersion", 1000 * 5, 1000 * 60);
        }
        return instance;
    }

    @Override
    public void checkStatus() {

        if (!FileManager.applicationConfig_File.getSpecificDataInstance().Server.isCheckVersion) return;

        int nowMinute = DateUtil.getNowMinute();

        if (nowMinute%5 == 0) {
            McVersion mcVersion = new McVersion();
            String snapshotVersion = FileManager.applicationConfig_File.getSpecificDataInstance().Server.SnapshotVersion;
            String releaseVersion = FileManager.applicationConfig_File.getSpecificDataInstance().Server.ReleaseVersion;
            if (!snapshotVersion.equals(mcVersion.getSnapshotVersion())) {
                FileManager.applicationConfig_File.getSpecificDataInstance().Server.SnapshotVersion = mcVersion.getSnapshotVersion();
                FileManager.applicationConfig_File.getSpecificDataInstance().Server.SnapshotVersionUrl = mcVersion.getSnapshotVersionUrl();
                FileManager.applicationConfig_File.getSpecificDataInstance().Server.isRelease = false;
                FileManager.applicationConfig_File.saveFile();
                FileManager.applicationConfig_File.reloadFile();

                LoggerManager.logDebug("mcVersion", "New SnapshotVersion!", true);

                String msg = "minecraft已发布新版本!";
                msg = msg + "\n" + mcVersion.getSnapshotVersion();
                msg = msg + "\n详细信息请查看" + mcVersion.getSnapshotVersionUrl();

                HashMap<Long, HashMap<Integer, ArrayList<String>>> serverList = FileManager.applicationConfig_File
                        .getSpecificDataInstance().Server.serverList;
                ContactList<Group> groups = BotManager.getAllQQGroups();
                for (Group group : groups) {
                    if (serverList.containsKey(group.getId())) MessageManager.sendMessageToQQGroup(group.getId(), msg);
                }
            }

            if (!releaseVersion.equals(mcVersion.getReleaseVersion())) {
                FileManager.applicationConfig_File.getSpecificDataInstance().Server.ReleaseVersion = mcVersion.getReleaseVersion();
                FileManager.applicationConfig_File.getSpecificDataInstance().Server.ReleaseVersionUrl = mcVersion.getReleaseVersionUrl();
                FileManager.applicationConfig_File.getSpecificDataInstance().Server.isRelease = true;
                FileManager.applicationConfig_File.saveFile();
                FileManager.applicationConfig_File.reloadFile();

                LoggerManager.logDebug("mcVersion", "New ReleaseVersion!", true);

                String msg = "minecraft已发布新版本!";
                msg = msg + "\n当前Java版minecraft最新版本为: " + mcVersion.getReleaseVersion();
                msg = msg + "\n详细信息请查看" + mcVersion.getReleaseVersionUrl();

                HashMap<Long, HashMap<Integer, ArrayList<String>>> serverList = FileManager.applicationConfig_File
                        .getSpecificDataInstance().Server.serverList;
                ContactList<Group> groups = BotManager.getAllQQGroups();
                for (Group group : groups) {
                    if (serverList.containsKey(group.getId())) MessageManager.sendMessageToQQGroup(group.getId(), msg);
                }
            }
        }
    }
}
