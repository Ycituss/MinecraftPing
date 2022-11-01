package com.ycitus.mcping;

import com.ycitus.mcping.command.RobotCommandChatType;
import com.ycitus.mcping.command.RobotCommandManager;
import com.ycitus.mcping.debug.LoggerManager;
import com.ycitus.mcping.files.FileManager;
import com.ycitus.mcping.timer.RobotTimerManager;
import net.mamoe.mirai.Bot;
import net.mamoe.mirai.console.plugin.jvm.JavaPlugin;
import net.mamoe.mirai.console.plugin.jvm.JvmPluginDescriptionBuilder;
import net.mamoe.mirai.event.GlobalEventChannel;
import net.mamoe.mirai.event.events.*;


public final class MinecraftPing extends JavaPlugin {
    public static final MinecraftPing INSTANCE = new MinecraftPing();
    private static boolean pluginLoaded = false;
    public static RobotCommandManager commandManager = null;
    private static Bot CURRENT_BOT = null;
    private static String version = "1.3";


    public static MinecraftPing getInstance() { return INSTANCE;  }

    public static Bot getCurrentBot() {
        return CURRENT_BOT;
    }

    public static boolean isPluginLoaded() {
        return pluginLoaded;
    }

    public static RobotCommandManager getCommandManager() {
        return commandManager;
    }
    private MinecraftPing() {
        super(new JvmPluginDescriptionBuilder("com.ycitus.MinecraftPing", "1.3")
                .name("MinecraftPing")
                .author("Ycitus")
                .build());
    }


    @Override
    public void onEnable() {

        pluginLoaded = true;
        LoggerManager.logDebug("MinecraftPing >> Enable.", true);
        LoggerManager.logDebug("Start Init...", true);

        // Init FileSystem.
        try {
            LoggerManager.logDebug("FileSystem", "Init FileSystem.", true);
            FileManager.getSingleInstance();
        } catch (IllegalArgumentException e) {
            LoggerManager.reportException(e);
        }

        // Init CommandSystem.
        LoggerManager.logDebug("CommandSystem", "Init CommandSystem.", true);
        commandManager = new RobotCommandManager();


        /** 接收群消息事件 **/
        LoggerManager.logDebug("EventSystem", "Start to subscribe events.");
        GlobalEventChannel.INSTANCE.subscribeAlways(GroupMessageEvent.class,
                event -> {
                    {
                        commandManager.receiveMessage(
                                RobotCommandChatType.GROUP_CHAT.getType(),
                                event.getTime(), event.getGroup().getId(),
                                event.getSender().getId(), event.getMessage());

                    }
                });


        /** 接收好友消息事件 **/
        GlobalEventChannel.INSTANCE.subscribeAlways(
                FriendMessageEvent.class,
                event -> {
                    {
                        commandManager.receiveMessage(
                                RobotCommandChatType.FRIEND_CHAT.getType(),
                                event.getTime(), -1, event.getSender().getId(),
                                event.getMessage());

                    }

                });

        /** 接收陌生人消息事件 **/
        GlobalEventChannel.INSTANCE.subscribeAlways(
                StrangerMessageEvent.class,
                event -> {
                    {
                        commandManager.receiveMessage(
                                RobotCommandChatType.STRANGER_CHAT.getType(),
                                event.getTime(), -1, event.getSender().getId(), event.getMessage());
                    }

                });


        /** 接收群临时消息事件 **/
        GlobalEventChannel.INSTANCE.subscribeAlways(
                GroupTempMessageEvent.class,
                event -> {
                    {

                        commandManager.receiveMessage(
                                RobotCommandChatType.GROUP_TEMP_CHAT
                                        .getType(), event.getTime(),
                                event.getGroup().getId(),
                                event.getSender().getId(), event.getMessage());


                    }
                });

        GlobalEventChannel.INSTANCE.subscribeAlways(
                GroupMessageSyncEvent.class,
                event -> {
                    {

                        commandManager.receiveMessage(
                                RobotCommandChatType.GroupMessageSync
                                        .getType(), event.getTime(),
                                event.getGroup().getId(),
                                event.getSender().getId(), event.getMessage());
                    }
                });


        /** 机器人登陆事件 **/
        GlobalEventChannel.INSTANCE.subscribeAlways(BotOnlineEvent.class, event -> {
            {
                /** 初始化Bot实例 **/
                tryInitBot(event.getBot());
            }
        });

        // 初始化时间任务系统
        LoggerManager.logDebug("TimerSystem", "Start TimerSystem.", true);
        RobotTimerManager.getInstance();

        LoggerManager.logDebug("McPing End Init...", true);
    }

    public void tryInitBot(Bot bot) {
        if (CURRENT_BOT == null) {
            CURRENT_BOT = bot;
        }
    }

    @Override
    public void onDisable() {
        super.getLogger().info("MinecraftPing >> Disable.");
    }
}