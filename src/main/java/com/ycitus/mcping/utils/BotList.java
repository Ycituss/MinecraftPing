package com.ycitus.mcping.utils;

import java.util.ArrayList;
import java.util.List;

public class BotList {
    public static List<String> getBotList(List<String> playerList){
        List<String> botList = new ArrayList<>();
        for (String player : playerList){
            if (player.contains("bot")){
                botList.add(player);
            }
        }
        return botList;
    }
}
