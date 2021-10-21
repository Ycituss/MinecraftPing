package com.ycitus.mcping.command.commands;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.WebClient;
import com.ycitus.mcping.command.RobotCommand;
import com.ycitus.mcping.command.RobotCommandChatType;
import com.ycitus.mcping.command.RobotCommandUser;
import com.ycitus.mcping.framework.MessageManager;
import net.mamoe.mirai.message.data.MessageChain;

import javax.net.ssl.SSLHandshakeException;
import java.io.IOException;

public class TestCommand extends RobotCommand {
    public TestCommand(String rule) {
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
        String path = "https://feedback.minecraft.net/hc/en-us/sections/360001186971?page=1#articles";

        WebClient webClient = new WebClient(BrowserVersion.CHROME);
        try {
            webClient.getPage(path);
            webClient.waitForBackgroundJavaScriptStartingBefore(2000);
            webClient.waitForBackgroundJavaScript(15000);
        }catch (SSLHandshakeException e) {
            MessageManager.sendMessageBySituation(fromGroup, fromQQ, e.toString());
        }catch (IOException e) {
            e.printStackTrace();
        }
    }
}
